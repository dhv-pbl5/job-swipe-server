package org.dhv.pbl5server.chat_service.service;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.chat_service.payload.ConversationResponse;
import org.dhv.pbl5server.chat_service.payload.MessageResponse;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.profile_service.entity.Company;
import org.dhv.pbl5server.profile_service.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// git commit -m "PBL-595 chat for user"
// git commit -m "PBL-596 chat for company"
// git commit -m "PBL-597 realtime conversation"
// git commit -m "PBL-598 realtime conversation for company"
// git commit -m "PBL-601 chat for user"
// git commit -m "PBL-602 chat for company"

public interface ChatService {
    ApiDataResponse getConversations(Account account, Pageable pageRequest);

    ConversationResponse getConversationById(Account account, String conversationId);

    void createConversation(User user, Company company);

    void changeConversationStatus(User user, Company company, boolean status);

    int getUnreadMessageCount(Account account, String conversationId);

    ApiDataResponse getMessages(Account account, String conversationId, Pageable pageRequest);

    MessageResponse getMessageById(Account account, String conversationId, String messageId);

    Object sendMessage(Account account, String conversationId, List<MultipartFile> files, String content);

    void readAllMessages(Account account, String conversationId);

    void readMessage(Account account, String conversationId, String messageId);
}
