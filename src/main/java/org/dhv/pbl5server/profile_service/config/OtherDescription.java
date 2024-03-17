package org.dhv.pbl5server.profile_service.config;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtherDescription {
    private String title;
    private String description;
    @CreationTimestamp
    private Timestamp createdAt;
}
