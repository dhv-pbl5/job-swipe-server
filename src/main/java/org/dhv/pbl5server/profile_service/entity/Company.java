package org.dhv.pbl5server.profile_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.dhv.pbl5server.profile_service.config.JsonConverter;
import org.dhv.pbl5server.profile_service.config.OtherDescription;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "companies")
public class Company {
    @Id
    private UUID accountId;
    private String companyName;
    private String companyUrl;
    private Timestamp establishedDate;
    @ElementCollection
    @Convert(converter = JsonConverter.class)
    private List<OtherDescription> other;
}
