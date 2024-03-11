package org.dhv.pbl5server.profile_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.dhv.pbl5server.common_service.model.AbstractEntity;

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
    @GeneratedValue
    private UUID accountId;
    private String firstName;
    private String lastName;
    private Boolean gender;
    private Timestamp dateOfBirth;
    private String summaryIntroduction;
    @ElementCollection
    @Column(columnDefinition = "String[]")
    private List<String> skills;
}
