package org.dhv.pbl5server.constant_service.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConstantRequest {
    @NotNull
    @Min(1)
    Integer type;
    @NotNull
    @NotBlank
    String name;
}
