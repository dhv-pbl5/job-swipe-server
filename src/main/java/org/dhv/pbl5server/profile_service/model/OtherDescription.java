package org.dhv.pbl5server.profile_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.common_service.model.DbJsonArrayModel;

import java.util.UUID;

// git commit -m "PBL-513 register for company"

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonSnakeCaseNaming
public class OtherDescription extends DbJsonArrayModel<UUID> {
    private String title;
    private String description;

    @Override
    public UUID generateId() {
        return UUID.randomUUID();
    }
}
