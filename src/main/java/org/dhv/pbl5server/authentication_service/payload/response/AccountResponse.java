package org.dhv.pbl5server.authentication_service.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import org.dhv.pbl5server.constant_service.entity.Constant;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponse {
    private UUID accountId;
    private String email;
    private Boolean accountStatus;
    private String address;
    private String avatar;
    private String phoneNumber;
    private Constant systemRole;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;
}
