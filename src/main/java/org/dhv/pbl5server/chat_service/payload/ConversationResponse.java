package org.dhv.pbl5server.chat_service.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.profile_service.payload.response.CompanyProfileResponse;
import org.dhv.pbl5server.profile_service.payload.response.UserProfileResponse;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonSnakeCaseNaming
public class ConversationResponse {
    private UUID id;
    private UserProfileResponse user;
    private CompanyProfileResponse company;
    private MessageResponse lastMessage;
    private boolean activeStatus;
    private String createdAt;
    private String updatedAt;
}
