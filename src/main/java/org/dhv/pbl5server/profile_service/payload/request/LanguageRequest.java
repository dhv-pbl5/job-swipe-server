package org.dhv.pbl5server.profile_service.payload.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.common_service.annotation.NotBlankStringValidation;
import org.dhv.pbl5server.constant_service.payload.ConstantSelectionRequest;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCaseNaming
public class LanguageRequest {
    private UUID id;
    @Valid
    @NotNull
    private ConstantSelectionRequest language;
    @NotBlankStringValidation
    private String score;
    private Timestamp certificateDate;
}
