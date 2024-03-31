package org.dhv.pbl5server.authentication_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.dhv.pbl5server.common_service.model.AbstractEntity;
import org.dhv.pbl5server.constant_service.entity.Constant;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account extends AbstractEntity implements UserDetails {
    @Id
    @GeneratedValue
    private UUID accountId;
    @Column(unique = true)
    private String email;
    @Column(columnDefinition = "boolean default true")
    private boolean accountStatus = true;
    private String address;
    private String avatar;
    private String password;
    private String phoneNumber;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "system_role")
    private Constant systemRole;
    private String refreshToken;
    private Timestamp deletedAt;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private List<ApplicationPosition> applicationPositions;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(systemRole.getConstantName()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return deletedAt == null;
    }
}
