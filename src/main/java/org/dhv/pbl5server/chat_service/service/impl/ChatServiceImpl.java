package org.dhv.pbl5server.chat_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.chat_service.entity.Conversation;
import org.dhv.pbl5server.chat_service.entity.Message;
import org.dhv.pbl5server.chat_service.mapper.ConversationMapper;
import org.dhv.pbl5server.chat_service.mapper.MessageMapper;
import org.dhv.pbl5server.chat_service.payload.ConversationResponse;
import org.dhv.pbl5server.chat_service.payload.MessageResponse;
import org.dhv.pbl5server.chat_service.repository.ConversationRepository;
import org.dhv.pbl5server.chat_service.repository.MessageRepository;
import org.dhv.pbl5server.chat_service.service.ChatService;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.exception.NotFoundObjectException;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.common_service.utils.PageUtils;
import org.dhv.pbl5server.constant_service.enums.SystemRole;
import org.dhv.pbl5server.profile_service.entity.Company;
import org.dhv.pbl5server.profile_service.entity.User;
import org.dhv.pbl5server.realtime_service.service.RealtimeService;
import org.dhv.pbl5server.s3_service.service.S3Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final RealtimeService realtimeService;
    private final S3Service s3Service;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final ConversationMapper conversationMapper;
    private final MessageMapper messageMapper;

    @Override
    public ApiDataResponse getConversations(Account account, Pageable pageRequest) {
        var page = account.getSystemRole().getConstantName().equals(SystemRole.USER.name())
            ? conversationRepository.findAllByUserId(account.getAccountId(), pageRequest)
            : conversationRepository.findAllByCompanyId(account.getAccountId(), pageRequest);
        return ApiDataResponse.success(page
                .getContent()
                .stream()
                .map(e -> {
                    var lastMessage = messageRepository.findFirstByConversationId(
                            e.getId(),
                            Sort.by("createdAt").descending())
                        .orElse(null);
                    return conversationMapper.toConversationResponse(e, lastMessage);
                })
                .toList(),
            PageUtils.makePageInfo(page));
    }

    @Override
    public ConversationResponse getConversationById(Account account, String conversationId) {
        var conversatinUuid = UUID.fromString(conversationId);
        var conversationOptional = account.getSystemRole().getConstantName().equals(SystemRole.USER.name())
            ? conversationRepository.findByIdAndUserId(conversatinUuid, account.getAccountId())
            : conversationRepository.findByIdAndCompanyId(conversatinUuid, account.getAccountId());
        var conversation = conversationOptional.orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.CONVERSATION_NOT_FOUND));
        var lastMessage = messageRepository.findFirstByConversationId(conversatinUuid, Sort.by("createdAt").descending()).orElse(null);
        return conversationMapper.toConversationResponse(conversation, lastMessage);
    }

    @Override
    public ConversationResponse createConversation(User user, Company company) {
        var conversation = Conversation.builder()
            .user(user)
            .company(company)
            .build();
        conversation = conversationRepository.save(conversation);
        ConversationResponse response = conversationMapper.toConversationResponse(conversation, null);
        realtimeService.sendToClientWithPrefix(user.getAccountId().toString(), response);
        return response;
    }

    @Override
    public int getUnreadMessageCount(Account account, String conversationId) {
        return messageRepository.findAllByAccount_AccountIdAndConversationIdAndReadStatus(
                account.getAccountId(), UUID.fromString(conversationId), false)
            .size();
    }

    @Override
    public ApiDataResponse getMessages(Account account, String conversationId, Pageable pageRequest) {
        var page = messageRepository.findAllByAccountIdAndConversationId(account.getAccountId(), UUID.fromString(conversationId), pageRequest);
        return ApiDataResponse.success(page
                .getContent()
                .stream()
                .map(messageMapper::toMessageResponse)
                .toList(),
            PageUtils.makePageInfo(page));
    }

    @Override
    public MessageResponse getMessageById(Account account, String conversationId, String messageId) {
        var message = messageRepository.findByIdAndAccount_AccountIdAndConversationId(UUID.fromString(messageId), account.getAccountId(), UUID.fromString(conversationId))
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.MESSAGE_NOT_FOUND));
        return messageMapper.toMessageResponse(message);
    }

    @Override
    public Object sendMessage(Account account, String conversationId, List<MultipartFile> files, String content) {
        // Require at least one of content or file
        if (CommonUtils.isEmptyOrNullList(files) && CommonUtils.isEmptyOrNullString(content))
            throw new BadRequestException(ErrorMessageConstant.MESSAGE_MUST_HAVE_CONTENT_OR_FILE);
        // Check conversation
        var conversatinUuid = UUID.fromString(conversationId);
        var conversationOptional = account.getSystemRole().getConstantName().equals(SystemRole.USER.name())
            ? conversationRepository.findByIdAndUserId(conversatinUuid, account.getAccountId())
            : conversationRepository.findByIdAndCompanyId(conversatinUuid, account.getAccountId());
        var conversation = conversationOptional.orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.CONVERSATION_NOT_FOUND));
        // Save message
        var messages = new ArrayList<Message>();
        if (CommonUtils.isNotEmptyOrNullList(files)) {
            var urlFiles = s3Service.uploadFiles(files);
            for (var url : urlFiles) {
                var message = Message.builder()
                    .account(account)
                    .conversation(conversation)
                    .readStatus(false)
                    .urlFile(url)
                    .build();
                messages.add(message);
            }
        }
        if (CommonUtils.isNotEmptyOrNullString(content)) {
            var message = Message.builder()
                .account(account)
                .conversation(conversation)
                .readStatus(false)
                .content(content)
                .build();
            messages.add(message);
        }
        messageRepository.saveAll(messages);
        return messages.size() == 1
            ? messageMapper.toMessageResponse(messages.getFirst())
            : messages.stream().map(messageMapper::toMessageResponse).toList();
    }

    @Override
    public void readAllMessages(Account account, String conversationId) {
        List<Message> unreadMessages = messageRepository.findAllByAccount_AccountIdAndConversationIdAndReadStatus(
                account.getAccountId(), UUID.fromString(conversationId), false)
            .stream()
            .peek(e -> e.setReadStatus(true))
            .toList();
        messageRepository.saveAll(unreadMessages);
    }

    @Override
    public void readMessage(Account account, String conversationId, String messageId) {
        var message = messageRepository.findByIdAndAccount_AccountIdAndConversationId(UUID.fromString(messageId), account.getAccountId(), UUID.fromString(conversationId))
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.MESSAGE_NOT_FOUND));
        message.setReadStatus(true);
        messageRepository.save(message);
    }
}
