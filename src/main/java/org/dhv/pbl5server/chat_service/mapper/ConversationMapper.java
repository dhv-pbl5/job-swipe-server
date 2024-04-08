package org.dhv.pbl5server.chat_service.mapper;

import org.dhv.pbl5server.chat_service.entity.Conversation;
import org.dhv.pbl5server.chat_service.entity.Message;
import org.dhv.pbl5server.chat_service.payload.ConversationResponse;
import org.dhv.pbl5server.common_service.config.SpringMapStructConfig;
import org.dhv.pbl5server.profile_service.mapper.CompanyMapper;
import org.dhv.pbl5server.profile_service.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    config = SpringMapStructConfig.class,
    uses = {
        MessageMapper.class,
        UserMapper.class,
        CompanyMapper.class
    })
public interface ConversationMapper {
    @Mapping(source = "conversation.user", target = "user", qualifiedByName = UserMapper.NAMED_ToUserProfileResponseWithBasicInfoOnly)
    @Mapping(source = "conversation.company", target = "company", qualifiedByName = CompanyMapper.NAMED_ToCompanyResponseWithBasicInfoOnly)
    @Mapping(source = "lastMessage", target = "lastMessage")
    @Mapping(source = "conversation.id", target = "id")
    @Mapping(source = "conversation.createdAt", target = "createdAt")
    @Mapping(source = "conversation.updatedAt", target = "updatedAt")
    ConversationResponse toConversationResponse(Conversation conversation, Message lastMessage);
}
