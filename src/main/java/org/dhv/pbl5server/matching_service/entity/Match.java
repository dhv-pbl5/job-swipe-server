package org.dhv.pbl5server.matching_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.dhv.pbl5server.common_service.model.AbstractEntity;
import org.dhv.pbl5server.profile_service.entity.Company;
import org.dhv.pbl5server.profile_service.entity.User;

import java.sql.Timestamp;
import java.util.UUID;

// git commit -m "PBL-594 realtime matching for company"

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "matches")
public class Match extends AbstractEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private Timestamp matchedTime;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    private Boolean userMatched;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company;
    private Boolean companyMatched;

    public boolean isCompleted() {
        return userMatched != null && userMatched && companyMatched != null && companyMatched;
    }

    public boolean isInvalidMatch() {
        return userMatched == null && companyMatched == null;
    }

    public boolean isUserMatched() {
        return userMatched != null && userMatched;
    }

    public boolean isUserMatchedNull() {
        return userMatched == null;
    }

    public boolean isCompanyMatched() {
        return companyMatched != null && companyMatched;
    }

    public boolean isCompanyMatchedNull() {
        return companyMatched == null;
    }
}
