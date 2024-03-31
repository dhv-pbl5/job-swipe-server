package org.dhv.pbl5server.authentication_service.mapper;

import org.dhv.pbl5server.authentication_service.entity.ApplicationPosition;
import org.dhv.pbl5server.authentication_service.payload.request.ApplicationPositionRequest;
import org.dhv.pbl5server.authentication_service.payload.response.ApplicationPositionResponse;
import org.dhv.pbl5server.common_service.config.SpringMapStructConfig;
import org.dhv.pbl5server.constant_service.mapper.ConstantMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapStructConfig.class, uses = {ConstantMapper.class, ApplicationPositionMapper.class})
public interface ApplicationPositionMapper {
    @Mapping(source = "skills", target = "skills")
    @Mapping(source = "applyPosition", target = "applyPosition")
    ApplicationPosition toApplicationPosition(ApplicationPositionRequest request);

    @Mapping(source = "request.status", target = "status")
    @Mapping(source = "request.skills", target = "skills")
    @Mapping(source = "request.applyPosition", target = "applyPosition")
    ApplicationPosition toApplicationPosition(ApplicationPosition applicationPosition, ApplicationPositionRequest request);

    @Mapping(source = "skills", target = "skills")
    @Mapping(source = "applyPosition", target = "applyPosition")
    ApplicationPositionResponse toApplicationPositionResponse(ApplicationPosition applicationPosition);


}
