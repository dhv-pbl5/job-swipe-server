package org.dhv.pbl5server.constant_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.common_service.constant.CommonConstant;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.exception.NotFoundObjectException;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.constant_service.entity.Constant;
import org.dhv.pbl5server.constant_service.enums.ConstantTypePrefix;
import org.dhv.pbl5server.constant_service.mapper.ConstantMapper;
import org.dhv.pbl5server.constant_service.payload.ConstantRequest;
import org.dhv.pbl5server.constant_service.payload.ConstantResponse;
import org.dhv.pbl5server.constant_service.repository.ConstantRepository;
import org.dhv.pbl5server.constant_service.service.ConstantService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConstantServiceImpl implements ConstantService {
    private final ConstantRepository repository;
    private final ConstantMapper mapper;

    @Override
    public void checkConstantWithType(UUID id, ConstantTypePrefix type) {
        var constant = repository.findById(id).orElseThrow(
            () -> new NotFoundObjectException(ErrorMessageConstant.CONSTANT_NOT_FOUND)
        );
        if (constant.getConstantType().equals(type.getValue())) return;
        throwErrorWithConstantType(type);
    }

    @Override
    public void checkConstantWithType(List<UUID> ids, ConstantTypePrefix type) {
        List<UUID> listIdsByConstantType = repository.findByConstantTypeStartsWith(type.getValue()).stream().map(Constant::getConstantId).toList();
        for (var id : ids) {
            if (listIdsByConstantType.contains(id)) continue;
            throwErrorWithConstantType(type);
            return;
        }
    }

    @Override
    public ConstantResponse getConstantById(UUID id) {
        var constant = repository.findById(id).orElseThrow(
            () -> new NotFoundObjectException(ErrorMessageConstant.CONSTANT_NOT_FOUND)
        );
        return mapper.toConstantResponse(constant);
    }

    @Override
    public Object getSystemRoles(String constantId) {
        // Get all system roles
        if (CommonUtils.isEmptyOrNullString(constantId))
            return repository.findByConstantTypeStartsWith(ConstantTypePrefix.SYSTEM_ROLES.getValue());
        // Get role by id
        Constant constant = repository.findById(UUID.fromString(constantId)).orElseThrow(
            () -> new NotFoundObjectException(ErrorMessageConstant.CONSTANT_NOT_FOUND)
        );
        if (constant == null || !constant.getConstantType().startsWith(ConstantTypePrefix.SYSTEM_ROLES.getValue()))
            throw new NotFoundObjectException(ErrorMessageConstant.SYSTEM_ROLE_NOT_FOUND);
        return constant;
    }

    @Override
    public List<ConstantResponse> getConstantsByTypePrefix(ConstantTypePrefix prefix) {
        return repository.findByConstantTypeStartsWith(prefix.getValue()).stream().map(mapper::toConstantResponse).toList();
    }

    @Override
    public ConstantResponse getConstantByType(String type) {
        return mapper.toConstantResponse(repository.findByConstantType(type)
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.CONSTANT_NOT_FOUND)));
    }

    @Override
    public List<Object> getConstantTypes() {
        return repository.getDistinctConstantType();
    }

    @Override
    public List<Map<String, String>> getAllPrefixes() {
        return Arrays.stream(ConstantTypePrefix.values()).map(e ->
            Map.of("prefix", CommonUtils.convertToCapitalCase(e.getEnumName()), "value", e.getValue())
        ).toList();
    }

    @Override
    public ConstantResponse createConstant(ConstantRequest request) {
        var constant = Constant
            .builder()
            .constantType(getConstantType(request.getConstantPrefix()))
            .constantName(request.getConstantName())
            .note(request.getNote())
            .build();
        return mapper.toConstantResponse(repository.save(constant));
    }

    @Override
    public void deleteConstant(List<String> ids) {
        for (var id : ids) {
            var constant = repository.findById(UUID.fromString(id)).orElseThrow(
                () -> new NotFoundObjectException(ErrorMessageConstant.CONSTANT_NOT_FOUND)
            );
            repository.delete(constant);
        }
    }

    private String getConstantType(String prefix) {
        var type = AbstractEnum.fromString(ConstantTypePrefix.values(), prefix);
        var constants = getConstantsByTypePrefix(type);
        var lastConstant = constants.get(constants.size() - 1);
        var typeInt = Integer.parseInt(lastConstant.getConstantType().substring(2));
        typeInt++;
        final int TYPE_DIGITS = CommonConstant.CONSTANT_LENGTH;
        int missingDigits = TYPE_DIGITS - Integer.toString(typeInt).length() - prefix.length();
        return prefix + "0".repeat(Math.max(0, missingDigits)) + typeInt;
    }

    private void throwErrorWithConstantType(ConstantTypePrefix type) {
        switch (type) {
            case APPLY_POSITIONS ->
                throw new BadRequestException(ErrorMessageConstant.CONSTANT_TYPE_MUST_BE_APPLY_POSITION);
            case SKILLS -> throw new BadRequestException(ErrorMessageConstant.CONSTANT_TYPE_MUST_BE_APPLY_SKILL);
            case EXPERIENCE_TYPES ->
                throw new BadRequestException(ErrorMessageConstant.CONSTANT_TYPE_MUST_BE_EXPERIENCE);
            case NOTIFICATIONS ->
                throw new BadRequestException(ErrorMessageConstant.CONSTANT_TYPE_MUST_BE_NOTIFICATION);
            case LANGUAGES -> throw new BadRequestException(ErrorMessageConstant.CONSTANT_TYPE_MUST_BE_LANGUAGE);
            case SALARY_RANGES ->
                throw new BadRequestException(ErrorMessageConstant.CONSTANT_TYPE_MUST_BE_SALARY_RANGE);
            default -> throw new BadRequestException(ErrorMessageConstant.CONSTANT_TYPE_MUST_BE_SYSTEM_ROLE);
        }
    }
}
