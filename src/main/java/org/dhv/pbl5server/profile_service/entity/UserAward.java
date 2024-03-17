package org.dhv.pbl5server.profile_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.dhv.pbl5server.common_service.model.AbstractEntity;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_awards")
public class UserAward extends AbstractEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private Timestamp certificateTime;
    private String certificateName;
    private String note;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private User user;
}
