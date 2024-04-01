package org.dhv.pbl5server.constant_service.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.common_service.annotation.UuidValidation;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCaseNaming
public class ConstantSelectionRequest {
    @UuidValidation
    private String constantId;
}
