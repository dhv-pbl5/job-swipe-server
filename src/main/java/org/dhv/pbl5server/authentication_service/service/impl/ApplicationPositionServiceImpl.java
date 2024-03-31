package org.dhv.pbl5server.authentication_service.service.impl;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.payload.request.ApplicationPositionRequest;
import org.dhv.pbl5server.authentication_service.payload.request.ApplicationSkillRequest;
import org.dhv.pbl5server.authentication_service.payload.response.AccountResponse;
import org.dhv.pbl5server.authentication_service.payload.response.ApplicationPositionResponse;
import org.dhv.pbl5server.authentication_service.service.ApplicationPositionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationPositionServiceImpl implements ApplicationPositionService {
    @Override
    public AccountResponse insertApplicationPositions(Account account, List<ApplicationPositionRequest> request) {
        return null;
    }

    @Override
    public AccountResponse insertApplicationSkills(Account account, List<ApplicationPositionRequest> request) {
        return null;
    }

    @Override
    public AccountResponse updateApplicationPosition(Account account, String applicationPositionId, ApplicationPositionRequest request) {
        return null;
    }

    @Override
    public AccountResponse updateApplicationSkills(Account account, String applicationPositionId, ApplicationSkillRequest request) {
        return null;
    }

    @Override
    public List<ApplicationPositionResponse> getApplicationPositions(String accountId) {
        return null;
    }

    @Override
    public ApplicationPositionResponse getApplicationPositionById(String accountId, String applicationPositionId) {
        return null;
    }

    @Override
    public void deleteApplicationPosition(Account account, String applicationPositionId) {

    }

    @Override
    public void deleteApplicationSkill(Account account, String applicationPositionId, String applicationSkillId) {

    }
}
