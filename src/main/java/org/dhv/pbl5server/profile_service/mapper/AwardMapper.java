package org.dhv.pbl5server.profile_service.mapper;

import org.dhv.pbl5server.common_service.config.SpringMapStructConfig;
import org.dhv.pbl5server.profile_service.entity.UserAward;
import org.dhv.pbl5server.profile_service.payload.request.UserAwardRequest;
import org.dhv.pbl5server.profile_service.payload.response.UserAwardResponse;
import org.mapstruct.Mapper;

@Mapper(config = SpringMapStructConfig.class)
public interface AwardMapper {
    UserAward toUserAward(UserAwardRequest request);

    UserAwardResponse toUserAwardResponse(UserAward award);
}
