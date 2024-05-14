package org.dhv.pbl5server.profile_service.service;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.profile_service.payload.request.ApplicationPositionRequest;
import org.dhv.pbl5server.profile_service.payload.request.ApplicationSkillRequest;
import org.dhv.pbl5server.profile_service.payload.response.ApplicationPositionResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

// git commit -m "PBL-538 company profile"
// git commit -m "PBL-526 position and skill"
// git commit -m "PBL-534 application position"
// git commit -m "PBL-528 delete application position"

public interface ApplicationPositionService {

        List<ApplicationPositionResponse> insertApplicationPositions(Account account,
                        List<ApplicationPositionRequest> request);

        List<ApplicationPositionResponse> updateApplicationPosition(Account account,
                        List<ApplicationPositionRequest> requests);

        ApplicationPositionResponse insertOrUpdateApplicationSkills(Account account, String applicationPositionId,
                        List<ApplicationSkillRequest> requests);

        List<ApplicationPositionResponse> getApplicationPositions(String accountId);

        ApiDataResponse getApplicationPositions(String accountId, Pageable pageable);

        ApplicationPositionResponse getApplicationPositionById(String accountId, String applicationPositionId);

        void deleteApplicationPositions(Account account, List<String> ids);

        void deleteApplicationSkills(Account account, String applicationPositionId, List<String> ids);

        Account getAccountWithAllApplicationPositions(UUID id);
}
