package org.dhv.pbl5server.notification_service.mapper;

import org.dhv.pbl5server.authentication_service.mapper.AccountMapper;
import org.dhv.pbl5server.common_service.config.SpringMapStructConfig;
import org.dhv.pbl5server.constant_service.mapper.ConstantMapper;
import org.dhv.pbl5server.notification_service.entity.Notification;
import org.dhv.pbl5server.notification_service.payload.NotificationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    config = SpringMapStructConfig.class,
    uses = {
        AccountMapper.class,
        ConstantMapper.class
    })
public interface NotificationMapper {
    @Mapping(source = "type", target = "type")
    @Mapping(source = "sender", target = "sender", qualifiedByName = AccountMapper.NAMED_ToAccountResponse)
    @Mapping(source = "receiver", target = "receiver", qualifiedByName = AccountMapper.NAMED_ToAccountResponse)
    NotificationResponse toNotificationResponse(Notification notification);
}
