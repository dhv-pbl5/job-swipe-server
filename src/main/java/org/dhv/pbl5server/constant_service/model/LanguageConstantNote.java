package org.dhv.pbl5server.constant_service.model;

import lombok.*;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonSnakeCaseNaming
public class LanguageConstantNote {
    private List<String> values;
    private LanguageValidate validate;
}

