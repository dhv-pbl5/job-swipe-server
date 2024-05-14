package org.dhv.pbl5server.chat_service.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;

import java.util.UUID;

// git commit -m "PBL-595 chat for user"

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonSnakeCaseNaming
public class MessageResponse {
    private UUID id;
    private String content;
    private UUID senderId;
    private UUID conversationId;
    private boolean readStatus;
    private String urlFile;
    private String createdAt;
    private String updatedAt;
}
