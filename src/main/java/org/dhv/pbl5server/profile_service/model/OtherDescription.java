package org.dhv.pbl5server.profile_service.model;

import lombok.Getter;
import lombok.Setter;
import org.dhv.pbl5server.common_service.utils.CommonUtils;

@Getter
@Setter
public class OtherDescription {
    private String title;
    private String description;
    private String createdAt;

    public OtherDescription(String title, String description) {
        this.title = title;
        this.description = description;
        this.createdAt = CommonUtils.getCurrentTimestamp().toString();
    }
}
