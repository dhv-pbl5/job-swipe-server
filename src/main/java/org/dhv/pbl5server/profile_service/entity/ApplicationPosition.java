package org.dhv.pbl5server.profile_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.model.AbstractEntity;
import org.dhv.pbl5server.constant_service.entity.Constant;

import java.util.List;
import java.util.UUID;

// git commit -m "PBL-526 position and skill"
// git commit -m "PBL-536 user profile"

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "application_positions")
public class ApplicationPosition extends AbstractEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private Boolean status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "apply_position")
    private Constant applyPosition;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "salary_range")
    private Constant salaryRange;
    private String note;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "applicationPosition")
    private List<ApplicationSkill> skills;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}
