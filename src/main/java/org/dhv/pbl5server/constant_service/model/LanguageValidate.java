package org.dhv.pbl5server.constant_service.model;

import lombok.*;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonSnakeCaseNaming
public class LanguageValidate {
    private Integer max;
    private Integer min;
    private Integer divisible;
    private Boolean requiredPoints;
}

