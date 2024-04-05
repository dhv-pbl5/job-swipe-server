package org.dhv.pbl5server.profile_service.mapper;

import org.dhv.pbl5server.common_service.config.SpringMapStructConfig;
import org.dhv.pbl5server.constant_service.mapper.ConstantMapper;
import org.dhv.pbl5server.profile_service.entity.UserExperience;
import org.dhv.pbl5server.profile_service.payload.request.UserExperienceRequest;
import org.dhv.pbl5server.profile_service.payload.response.UserExperienceResponse;
import org.mapstruct.Mapper;

@Mapper(config = SpringMapStructConfig.class, uses = {ConstantMapper.class})
public interface ExperienceMapper {
    UserExperience toUserExperience(UserExperienceRequest request);

    UserExperienceResponse toUserExperienceResponse(UserExperience experience);
}
