package org.dhv.pbl5server.constant_service.payload;

import lombok.Getter;
import lombok.Setter;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;

import java.util.UUID;

@Getter
@Setter
@JsonSnakeCaseNaming
public class ConstantResponse {
    private UUID constantId;
    private String constantType;
    private String constantName;
}
