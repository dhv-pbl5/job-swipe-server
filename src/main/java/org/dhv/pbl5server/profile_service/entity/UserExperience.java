package org.dhv.pbl5server.profile_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.dhv.pbl5server.common_service.model.AbstractEntity;
import org.dhv.pbl5server.constant_service.entity.Constant;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_experiences")
public class UserExperience extends AbstractEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private Timestamp experienceStartTime;
    private Timestamp experienceEndTime;
    @ManyToOne
    @JoinColumn(name = "experience_type")
    private Constant experienceType;
    private String workPlace;
    private String position;
    private String note;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private User user;
}
