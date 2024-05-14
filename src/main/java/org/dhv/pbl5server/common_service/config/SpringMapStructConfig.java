// git commit -m "PBL-850 set up base"

package org.dhv.pbl5server.common_service.config;

import org.mapstruct.MapperConfig;
import org.mapstruct.NullValueCheckStrategy;

@MapperConfig(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface SpringMapStructConfig {
}
