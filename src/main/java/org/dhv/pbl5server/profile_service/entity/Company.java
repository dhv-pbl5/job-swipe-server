package org.dhv.pbl5server.profile_service.entity;

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
@Table(name = "companies")
public class Company extends AbstractEntity {
    @Id
    private UUID accountId;
    private String companyName;
    private String companyUrl;
    private Timestamp establishedDate;
    @Type(JsonBinaryType.class)
    private List<OtherDescription> others;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name = "account_id", referencedColumnName = "account_id")
    private Account account;
}
