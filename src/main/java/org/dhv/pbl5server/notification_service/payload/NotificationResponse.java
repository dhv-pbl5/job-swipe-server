package org.dhv.pbl5server.notification_service.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.authentication_service.payload.response.AccountResponse;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.constant_service.payload.ConstantResponse;

import java.util.UUID;

// git commit -m "PBL-597 realtime conversation"

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonSnakeCaseNaming
public class NotificationResponse {
    private UUID id;
    private UUID objectId;
    private String content;
    private ConstantResponse type;
    private boolean readStatus;
    private AccountResponse receiver;
    private AccountResponse sender;
    private String createdAt;
    private String updatedAt;
}
