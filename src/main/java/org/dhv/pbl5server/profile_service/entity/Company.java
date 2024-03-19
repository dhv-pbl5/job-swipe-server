package org.dhv.pbl5server.profile_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.sql.Timestamp;
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
//    private List<OtherDescription> other;
}
