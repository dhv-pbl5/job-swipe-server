package org.dhv.pbl5server.authentication_service.service;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.payload.request.ApplicationPositionRequest;
import org.dhv.pbl5server.authentication_service.payload.request.ApplicationSkillRequest;
import org.dhv.pbl5server.authentication_service.payload.response.AccountResponse;
import org.dhv.pbl5server.authentication_service.payload.response.ApplicationPositionResponse;

import java.util.List;

public interface ApplicationPositionService {
    AccountResponse insertApplicationPositions(Account account, List<ApplicationPositionRequest> request);

    AccountResponse insertApplicationSkills(Account account, List<ApplicationPositionRequest> request);

    AccountResponse updateApplicationPosition(Account account, String applicationPositionId, ApplicationPositionRequest request);

    AccountResponse updateApplicationSkills(Account account, String applicationPositionId, ApplicationSkillRequest request);

    List<ApplicationPositionResponse> getApplicationPositions(String accountId);

    ApplicationPositionResponse getApplicationPositionById(String accountId, String applicationPositionId);

    void deleteApplicationPosition(Account account, String applicationPositionId);

    void deleteApplicationSkill(Account account, String applicationPositionId, String applicationSkillId);
}
