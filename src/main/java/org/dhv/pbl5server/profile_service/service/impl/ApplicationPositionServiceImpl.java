package org.dhv.pbl5server.profile_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.repository.AccountRepository;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.exception.NotFoundObjectException;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.constant_service.enums.ConstantType;
import org.dhv.pbl5server.constant_service.service.ConstantService;
import org.dhv.pbl5server.profile_service.entity.ApplicationPosition;
import org.dhv.pbl5server.profile_service.mapper.ApplicationPositionMapper;
import org.dhv.pbl5server.profile_service.payload.request.ApplicationPositionRequest;
import org.dhv.pbl5server.profile_service.payload.request.ApplicationSkillRequest;
import org.dhv.pbl5server.profile_service.payload.response.ApplicationPositionResponse;
import org.dhv.pbl5server.profile_service.repository.ApplicationPositionRepository;
import org.dhv.pbl5server.profile_service.repository.ApplicationSkillRepository;
import org.dhv.pbl5server.profile_service.service.ApplicationPositionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationPositionServiceImpl implements ApplicationPositionService {
    private final ApplicationPositionRepository repository;
    private final ApplicationSkillRepository applicationSkillRepository;
    private final AccountRepository accountRepository;
    private final ApplicationPositionMapper mapper;
    private final ConstantService constantService;


    @Override
    public List<ApplicationPositionResponse> insertApplicationPositions(Account account, List<ApplicationPositionRequest> request) {
        var aps = request.stream().map(mapper::toApplicationPosition).toList();
        // Check constant type of apply_position and apply_skill and set dependency
        aps = checkConstantType(account, aps, true);
        return repository.saveAll(aps).stream().map(mapper::toApplicationPositionResponse).toList();
    }

    @Override
    public ApplicationPositionResponse updateApplicationPosition(Account account, ApplicationPositionRequest request) {
        if (request.getId() == null)
            throw new BadRequestException(ErrorMessageConstant.APPLICATION_POSITION_ID_REQUIRED);
        var ap = repository.findByIdAndAccountId(account.getAccountId(), request.getId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.APPLICATION_POSITION_NOT_FOUND));
        ap = mapper.toApplicationPosition(ap, request);
        // Check constant type of apply_position and apply_skill and set dependency
        ap = checkConstantType(account, List.of(ap), false).getFirst();
        repository.save(ap);
        return mapper.toApplicationPositionResponse(repository.fetchAllDataApplicationSkillById(ap.getId()).orElseThrow());
    }

    @Override
    public ApplicationPositionResponse insertOrUpdateApplicationSkills(Account account, String applicationPositionId, List<ApplicationSkillRequest> request) {
        if (applicationPositionId == null)
            throw new BadRequestException(ErrorMessageConstant.APPLICATION_POSITION_ID_REQUIRED);
        var ap = repository.findByIdAndAccountId(account.getAccountId(), UUID.fromString(applicationPositionId))
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.APPLICATION_POSITION_NOT_FOUND));
        ap = mapper.toApplicationPosition(ap, request);
        // Check constant type of apply_position and apply_skill and set dependency
        ap = checkConstantType(account, List.of(ap), false).getFirst();
        repository.save(ap);
        return mapper.toApplicationPositionResponse(repository.fetchAllDataApplicationSkillById(ap.getId()).orElseThrow());
    }

    @Override
    public List<ApplicationPositionResponse> getApplicationPositions(String accountId) {
        var aps = repository.findAllByAccountId(UUID.fromString(accountId));
        if (aps == null || aps.isEmpty()) return Collections.emptyList();
        for (var ap : aps)
            ap.setSkills(applicationSkillRepository.findAllByApplicationPositionId(ap.getId()));
        return aps.stream().map(mapper::toApplicationPositionResponse).toList();
    }

    @Override
    public ApplicationPositionResponse getApplicationPositionById(String accountId, String applicationPositionId) {
        var ap = repository.findByIdAndAccountId(UUID.fromString(accountId), UUID.fromString(applicationPositionId))
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.APPLICATION_POSITION_NOT_FOUND));
        ap.setSkills(applicationSkillRepository.findAllByApplicationPositionId(ap.getId()));
        return mapper.toApplicationPositionResponse(ap);
    }

    @Override
    public void deleteApplicationPositions(Account account, List<String> ids) {
        for (var id : ids) {
            var ap = repository.fetchAllDataApplicationSkillById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.APPLICATION_POSITION_NOT_FOUND));
            if (account.getAccountId().compareTo(ap.getAccount().getAccountId()) != 0)
                throw new NotFoundObjectException(ErrorMessageConstant.APPLICATION_POSITION_NOT_FOUND);
            repository.delete(ap);
        }

    }

    @Override
    public void deleteApplicationSkills(Account account, String applicationPositionId, List<String> ids) {
        for (var id : ids) {
            var ap = repository.findByIdAndAccountId(account.getAccountId(), UUID.fromString(applicationPositionId))
                .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.APPLICATION_POSITION_NOT_FOUND));
            var aps = applicationSkillRepository.findByIdAndApplicationPositionId(ap.getId(), UUID.fromString(id))
                .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.APPLICATION_SKILL_NOT_FOUND));
            applicationSkillRepository.delete(aps);
        }
    }

    @Override
    public Account getAccountWithAllApplicationPositions(UUID id) {
        var account = accountRepository.fetchAllDataApplicationPositionByAccountId(id).orElseThrow(
            () -> new NotFoundObjectException(ErrorMessageConstant.ACCOUNT_NOT_FOUND)
        );
        for (var ap : account.getApplicationPositions()) {
            ap.setSkills(applicationSkillRepository.findAllByApplicationPositionId(ap.getId()));
        }
        return account;
    }

    private List<ApplicationPosition> checkConstantType(Account account, List<ApplicationPosition> aps, boolean isInsert) {
        List<UUID> applyPositionIds = new ArrayList<>();
        List<UUID> applySkillIds = new ArrayList<>();
        var result = aps.stream().peek(ap -> {
            applyPositionIds.add(ap.getApplyPosition().getConstantId());
            if (isInsert) ap.setId(null);
            ap.setAccount(account);
            if (CommonUtils.isNotEmptyOrNullList(ap.getSkills()))
                ap.setSkills(ap.getSkills().stream().peek(skill -> {
                    applySkillIds.add(skill.getSkill().getConstantId());
                    if (isInsert) skill.setId(null);
                    skill.setApplicationPosition(ap);
                }).toList());
        }).toList();
        // Check all apply_position constant type
        constantService.checkConstantWithType(applyPositionIds, ConstantType.APPLY_POSITION);
        // Check all apply_skill constant type
        constantService.checkConstantWithType(applySkillIds, ConstantType.SKILL);
        return result;
    }
}
