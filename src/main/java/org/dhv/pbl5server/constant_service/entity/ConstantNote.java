package org.dhv.pbl5server.constant_service.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConstantNote {
    private List<String> values;
    private ConstantNoteValidate validate;
}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
class ConstantNoteValidate {
    private Integer max;
    private Integer min;
    private Integer divisible;
    private Boolean required_points;
}
