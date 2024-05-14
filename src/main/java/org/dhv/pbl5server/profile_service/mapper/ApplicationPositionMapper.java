package org.dhv.pbl5server.profile_service.mapper;

import org.dhv.pbl5server.common_service.config.SpringMapStructConfig;
import org.dhv.pbl5server.constant_service.mapper.ConstantMapper;
import org.dhv.pbl5server.profile_service.entity.ApplicationPosition;
import org.dhv.pbl5server.profile_service.payload.request.ApplicationPositionRequest;
import org.dhv.pbl5server.profile_service.payload.request.ApplicationSkillRequest;
import org.dhv.pbl5server.profile_service.payload.response.ApplicationPositionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

// git commit -m "PBL-536 user profile"
// git commit -m "PBL-526 position and skill"
// git commit -m "PBL-534 application position"

@Mapper(config = SpringMapStructConfig.class, uses = { ConstantMapper.class, ApplicationSkillMapper.class })
public interface ApplicationPositionMapper {
        public static final String NAMED_ToApplicationPositionResponse = "toApplicationPositionResponse";

        @Mapping(source = "skills", target = "skills")
        @Mapping(source = "applyPosition", target = "applyPosition")
        @Mapping(source = "salaryRange", target = "salaryRange")
        ApplicationPosition toApplicationPosition(ApplicationPositionRequest request);

        @Mapping(source = "request.id", target = "id")
        @Mapping(source = "request.status", target = "status")
        @Mapping(source = "request.skills", target = "skills")
        @Mapping(source = "request.applyPosition", target = "applyPosition")
        @Mapping(source = "request.salaryRange", target = "salaryRange")
        ApplicationPosition toApplicationPosition(ApplicationPosition applicationPosition,
                        ApplicationPositionRequest request);

        @Mapping(source = "request", target = "skills")
        @Mapping(source = "applicationPosition.applyPosition", target = "applyPosition")
        @Mapping(source = "applicationPosition.salaryRange", target = "salaryRange")
        ApplicationPosition toApplicationPosition(ApplicationPosition applicationPosition,
                        List<ApplicationSkillRequest> request);

        @Named(NAMED_ToApplicationPositionResponse)
        @Mapping(source = "skills", target = "skills")
        @Mapping(source = "applyPosition", target = "applyPosition")
        @Mapping(source = "salaryRange", target = "salaryRange")
        ApplicationPositionResponse toApplicationPositionResponse(ApplicationPosition applicationPosition);

        @Mapping(source = "skills", target = "skills")
        @Mapping(source = "applyPosition", target = "applyPosition")
        @Mapping(source = "salaryRange", target = "salaryRange")
        ApplicationPositionResponse toApplicationPositionResponseWithBasicInfoOnly(
                        ApplicationPosition applicationPosition);
}
