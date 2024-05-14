package org.dhv.pbl5server.chat_service.mapper;

import org.dhv.pbl5server.authentication_service.mapper.AccountMapper;
import org.dhv.pbl5server.chat_service.entity.Message;
import org.dhv.pbl5server.chat_service.payload.MessageResponse;
import org.dhv.pbl5server.common_service.config.SpringMapStructConfig;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

// git commit -m "PBL-595 chat for user"
// git commit -m "PBL-596 chat for company"

@Mapper(config = SpringMapStructConfig.class, uses = { AccountMapper.class })
public interface MessageMapper {
    public static String NAMED_DecodeContent = "decodeContent";
    public static String NAMED_DecodeUrl = "decodeUrl";

    @Mapping(source = "account.accountId", target = "senderId")
    @Mapping(source = "conversation.id", target = "conversationId")
    @Mapping(source = "content", target = "content", qualifiedByName = NAMED_DecodeContent)
    @Mapping(source = "urlFile", target = "urlFile", qualifiedByName = NAMED_DecodeUrl)
    MessageResponse toMessageResponse(Message message);

    @Named("decodeContent")
    default String decodeContent(String encodedContent) {
        return CommonUtils.decodeBase64(encodedContent);
    }

    @Named("decodeUrl")
    default String decodeUrl(String encodedUrl) {
        return CommonUtils.decodeUrlBase64(encodedUrl);
    }
}
