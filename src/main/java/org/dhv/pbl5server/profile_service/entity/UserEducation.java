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
@Table(name = "user_educations")
public class UserEducation extends AbstractEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String studyPlace;
    private Timestamp studyStartTime;
    private Timestamp studyEndTime;
    private String majority;
    private Double cpa;
    private String note;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private User user;
}
