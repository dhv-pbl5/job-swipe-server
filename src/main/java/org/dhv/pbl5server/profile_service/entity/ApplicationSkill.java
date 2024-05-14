package org.dhv.pbl5server.profile_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.dhv.pbl5server.common_service.model.AbstractEntity;
import org.dhv.pbl5server.constant_service.entity.Constant;

import java.util.UUID;

// git commit -m "PBL-526 position and skill"
// git commit -m "PBL-536 user profile"
// git commit -m "PBL-534 application position"
// git commit -m "PBL-528 delete application position"

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "application_skills")
public class ApplicationSkill extends AbstractEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String note;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id")
    private Constant skill;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_position_id")
    private ApplicationPosition applicationPosition;
}
