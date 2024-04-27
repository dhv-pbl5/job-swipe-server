package org.dhv.pbl5server.constant_service.mapper;

import org.dhv.pbl5server.common_service.config.SpringMapStructConfig;
import org.dhv.pbl5server.constant_service.entity.Constant;
import org.dhv.pbl5server.constant_service.payload.ConstantResponse;
import org.dhv.pbl5server.constant_service.payload.ConstantSelectionRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(config = SpringMapStructConfig.class)
public interface ConstantMapper {

    @Mapping(source = "note", target = "note")
    ConstantResponse toConstantResponse(Constant constant);

    @Mapping(target = "constantId", expression = "java(convertStringToUUID(request.getConstantId()))")
    Constant toConstant(ConstantSelectionRequest request);

    default UUID convertStringToUUID(String uuid) {
        return UUID.fromString(uuid);
    }
}
