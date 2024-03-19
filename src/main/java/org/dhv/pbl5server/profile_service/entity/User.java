package org.dhv.pbl5server.profile_service.entity;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.*;
import org.dhv.pbl5server.common_service.model.AbstractEntity;
import org.dhv.pbl5server.profile_service.model.OtherDescription;
import org.dhv.pbl5server.profile_service.model.OtherDescriptionConverter;
import org.hibernate.annotations.Type;

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
    @Type(ListArrayType.class)
    private List<String> socialMediaLink;
    @Convert(converter = OtherDescriptionConverter.class)
    private List<OtherDescription> other;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserEducation> educations;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserAward> awards;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserExperience> experiences;
}
