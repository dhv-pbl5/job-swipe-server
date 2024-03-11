package org.dhv.pbl5server.profile_service.entity;

import lombok.*;
import org.dhv.pbl5server.common_service.model.JsonConverter;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Other implements JsonConverter<Other> {
    private String title;
    private String description;
    @CreationTimestamp
    private LocalDateTime createdAt;

    public String toJson() {
        return CommonUtils.convertToJson(this);
    }

    public Other fromJson(String json) {
        return CommonUtils.decodeJson(json, Other.class);
    }
}
