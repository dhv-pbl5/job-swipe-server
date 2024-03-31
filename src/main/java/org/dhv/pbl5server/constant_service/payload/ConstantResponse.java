package org.dhv.pbl5server.constant_service.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ConstantResponse {
    private UUID constantId;
    private String constantType;
    private String constantName;
}
