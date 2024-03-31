package org.dhv.pbl5server.profile_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.dhv.pbl5server.common_service.model.AbstractEntity;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Table(name = "companies")
public class Company extends AbstractEntity {
    @Id
    private UUID accountId;
    private String companyName;
    private String companyUrl;
    private Timestamp establishedDate;
}
