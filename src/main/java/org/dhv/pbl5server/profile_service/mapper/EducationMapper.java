package org.dhv.pbl5server.profile_service.mapper;

import org.dhv.pbl5server.profile_service.entity.UserEducation;
import org.dhv.pbl5server.profile_service.payload.request.UserEducationRequest;
import org.dhv.pbl5server.profile_service.payload.response.UserEducationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface EducationMapper {
    UserEducation toUserEducation(UserEducationRequest request);

    UserEducationResponse toUserEducationResponse(UserEducation education);
}
