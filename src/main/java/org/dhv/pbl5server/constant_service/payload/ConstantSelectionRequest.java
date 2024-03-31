package org.dhv.pbl5server.constant_service.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.common_service.annotation.UuidValidation;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@JsonSnakeCaseNaming
public class ConstantSelectionRequest {
    @UuidValidation
    private UUID constantId;
}
