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
    @ManyToOne
    @JoinColumn(name = "apply_position")
    private Constant applyPosition;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "applicationPosition")
    private List<ApplicationSkill> skills;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

}
