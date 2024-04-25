package org.dhv.pbl5server.chat_service.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;

import java.sql.Timestamp;
import java.util.UUID;

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
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
