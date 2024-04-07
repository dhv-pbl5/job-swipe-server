package org.dhv.pbl5server.chat_service.mapper;

import org.dhv.pbl5server.authentication_service.mapper.AccountMapper;
import org.dhv.pbl5server.chat_service.entity.Message;
import org.dhv.pbl5server.chat_service.payload.MessageResponse;
import org.dhv.pbl5server.common_service.config.SpringMapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapStructConfig.class, uses = {AccountMapper.class})
public interface MessageMapper {
    @Mapping(source = "account", target = "account", qualifiedByName = AccountMapper.NAMED_ToAccountResponse)
    @Mapping(source = "conversation.id", target = "conversationId")
    MessageResponse toMessageResponse(Message message);
}
