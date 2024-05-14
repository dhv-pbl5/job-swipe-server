package org.dhv.pbl5server.profile_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.dhv.pbl5server.common_service.model.AbstractEntity;

import java.sql.Timestamp;
import java.util.UUID;

// git commit -m "PBL-536 user profile"
// git commit -m "PBL-563 user award"

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "user_awards")
public class UserAward extends AbstractEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private Timestamp certificateTime;
    private String certificateName;
    private String note;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private User user;
}
