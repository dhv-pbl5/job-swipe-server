package org.dhv.pbl5server.profile_service.entity;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.model.AbstractEntity;
import org.dhv.pbl5server.profile_service.model.OtherDescription;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Table(name = "users")
public class User extends AbstractEntity {
    @Id
    private UUID accountId;
    private String firstName;
    private String lastName;
    private Boolean gender;
    private Timestamp dateOfBirth;
    private String summaryIntroduction;
    @Type(ListArrayType.class)
    private List<String> socialMediaLink;
    @Type(JsonBinaryType.class)
    private List<OtherDescription> others;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserEducation> educations;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserAward> awards;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserExperience> experiences;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name = "account_id", referencedColumnName = "account_id")
    private Account account;

    public String getFullName() {
        return lastName + " " + firstName;
    }
}
