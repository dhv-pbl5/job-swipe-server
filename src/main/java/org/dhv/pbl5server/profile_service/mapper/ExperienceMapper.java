package org.dhv.pbl5server.profile_service.mapper;

import org.dhv.pbl5server.profile_service.entity.UserExperience;
import org.dhv.pbl5server.profile_service.payload.request.UserExperienceRequest;
import org.dhv.pbl5server.profile_service.payload.response.UserExperienceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ExperienceMapper {
    UserExperience toUserExperience(UserExperienceRequest request);

    UserExperienceResponse toUserExperienceResponse(UserExperience experience);
}
