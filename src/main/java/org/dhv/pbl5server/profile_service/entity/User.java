package org.dhv.pbl5server.profile_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.dhv.pbl5server.common_service.model.AbstractEntity;
import org.dhv.pbl5server.profile_service.config.JsonConverter;
import org.dhv.pbl5server.profile_service.config.OtherDescription;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "users")
public class User extends AbstractEntity {
    @Id
    private UUID accountId;
    private String firstName;
    private String lastName;
    private Boolean gender;
    private Timestamp dateOfBirth;
    private String summaryIntroduction;
    @ElementCollection
    private List<String> socialMediaLink;
    @ElementCollection
    @Convert(converter = JsonConverter.class)
    private List<OtherDescription> other;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserEducation> educations;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserAward> awards;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserExperience> experiences;
}
