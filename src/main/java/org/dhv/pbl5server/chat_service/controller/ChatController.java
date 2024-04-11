package org.dhv.pbl5server.chat_service.controller;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.annotation.CurrentAccount;
import org.dhv.pbl5server.authentication_service.annotation.PreAuthorizeSystemRoleWithoutAdmin;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.chat_service.service.ChatService;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.common_service.utils.PageUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService service;

    @PreAuthorizeSystemRoleWithoutAdmin
    @GetMapping("/conversations")
    public ResponseEntity<ApiDataResponse> getConversations(
        @Nullable @RequestParam("conversation_id") String conversationId,
        @Nullable @RequestParam("page") Integer page,
        @Nullable @RequestParam("paging") Integer paging,
        @CurrentAccount Account account
    ) {
        // Get conversation by id
        if (conversationId != null)
            return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getConversationById(account, conversationId)));
        // Get all conversations
        var pageRequest = PageUtils.makePageRequest("created_at", "desc", page, paging);
        return ResponseEntity.ok(service.getConversations(account, pageRequest));
    }

    @PreAuthorizeSystemRoleWithoutAdmin
    @GetMapping("/conversations/unread-count")
    public ResponseEntity<ApiDataResponse> getUnreadMessageCount(
        @RequestParam("conversation_id") String conversationId,
        @CurrentAccount Account account
    ) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getUnreadMessageCount(account, conversationId)));
    }


    @PreAuthorizeSystemRoleWithoutAdmin
    @GetMapping("/messages")
    public ResponseEntity<ApiDataResponse> getMessages(
        @RequestParam("conversation_id") String conversationId,
        @Nullable @RequestParam("message_id") String messageId,
        @Nullable @RequestParam("page") Integer page,
        @Nullable @RequestParam("paging") Integer paging,
        @CurrentAccount Account account
    ) {
        // Get message by id
        if (messageId != null)
            return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getMessageById(account, conversationId, messageId)));
        // Get all message in a conversation
        var pageRequest = PageUtils.makePageRequest("created_at", "desc", page, paging);
        return ResponseEntity.ok(service.getMessages(account, conversationId, pageRequest));
    }


    @PreAuthorizeSystemRoleWithoutAdmin
    @PostMapping("/messages")
    public ResponseEntity<ApiDataResponse> sendMessage(
        @RequestParam("conversation_id") String conversationId,
        @Nullable @RequestPart List<MultipartFile> files,
        @Nullable @RequestPart String content,
        @CurrentAccount Account account
    ) {
        if (CommonUtils.isEmptyOrNullList(files) && CommonUtils.isEmptyOrNullString(content))
            throw new BadRequestException(ErrorMessageConstant.MESSAGE_MUST_HAVE_CONTENT_OR_FILE);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.sendMessage(account, conversationId, files, content)));
    }

    @PreAuthorizeSystemRoleWithoutAdmin
    @PatchMapping("/messages")
    public ResponseEntity<ApiDataResponse> readMessage(
        @RequestParam("conversation_id") String conversationId,
        @Nullable @RequestParam("message_id") String messageId,
        @Nullable @RequestParam("is_all") Boolean isAll,
        @CurrentAccount Account account
    ) {
        // Read all messages in a conversation
        if (isAll != null && isAll) {
            service.readAllMessages(account, conversationId);
            return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
        }
        // Read a message by id
        if (!CommonUtils.isValidUuid(messageId))
            throw new BadRequestException(ErrorMessageConstant.INVALID_UUID);
        service.readMessage(account, conversationId, messageId);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }
}
