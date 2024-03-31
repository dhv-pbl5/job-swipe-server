package org.dhv.pbl5server.constant_service.mapper;

import org.dhv.pbl5server.common_service.config.SpringMapStructConfig;
import org.dhv.pbl5server.constant_service.entity.Constant;
import org.dhv.pbl5server.constant_service.payload.ConstantResponse;
import org.mapstruct.Mapper;

@Mapper(config = SpringMapStructConfig.class)
public interface ConstantMapper {
    ConstantResponse toConstantResponse(Constant constant);
}
