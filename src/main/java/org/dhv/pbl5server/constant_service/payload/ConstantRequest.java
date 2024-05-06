package org.dhv.pbl5server.constant_service.payload;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;
import org.dhv.pbl5server.common_service.annotation.NotBlankStringValidation;

@Getter
@Setter
@Builder
@JsonSnakeCaseNaming
public class ConstantRequest {
    @NotBlankStringValidation
    @Pattern(regexp = "^\\d{2}$")
    private String constantPrefix;
    @NotBlankStringValidation
    private String constantName;
    private Object note;
}
