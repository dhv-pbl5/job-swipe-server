package org.dhv.pbl5server.constant_service.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.constant_service.enums.ConstantType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConstantRequest {
    @NotNull
    ConstantType type;
    @NotNull
    @NotBlank
    String name;
}
