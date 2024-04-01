package org.dhv.pbl5server.constant_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.common_service.constant.CommonConstant;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.exception.NotFoundObjectException;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.constant_service.entity.Constant;
import org.dhv.pbl5server.constant_service.enums.ConstantType;
import org.dhv.pbl5server.constant_service.mapper.ConstantMapper;
import org.dhv.pbl5server.constant_service.payload.ConstantResponse;
import org.dhv.pbl5server.constant_service.repository.ConstantRepository;
import org.dhv.pbl5server.constant_service.service.ConstantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConstantServiceImpl implements ConstantService {
    private final ConstantRepository repository;
    private final ConstantMapper mapper;

    @Override
    public void checkConstantWithType(UUID id, ConstantType type) {
        var constant = repository.findById(id).orElseThrow(
            () -> new NotFoundObjectException(ErrorMessageConstant.CONSTANT_NOT_FOUND)
        );
        if (constant.getConstantType().equals(type.getValue())) return;
        throwErrorWithConstantType(type);
    }

    @Override
    public void checkConstantWithType(List<UUID> ids, ConstantType type) {
        List<UUID> listIdsByConstantType = repository.findByConstantType(type.getValue()).stream().map(Constant::getConstantId).toList();
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
            return repository.findByConstantTypeStartsWith(CommonConstant.SYSTEM_ROLE_PREFIX);
        // Get role by id
        Constant constant = repository.findById(UUID.fromString(constantId)).orElseThrow(
            () -> new NotFoundObjectException(ErrorMessageConstant.CONSTANT_NOT_FOUND)
        );
        if (constant == null || !constant.getConstantType().startsWith(CommonConstant.SYSTEM_ROLE_PREFIX))
            throw new NotFoundObjectException(ErrorMessageConstant.SYSTEM_ROLE_NOT_FOUND);
        return constant;
    }

    @Override
    public List<ConstantResponse> getConstantsByType(String type) {
        return repository.findByConstantType(type).stream().map(mapper::toConstantResponse).toList();
    }

    private void throwErrorWithConstantType(ConstantType type) {
        switch (type) {
            case APPLY_POSITION ->
                throw new BadRequestException(ErrorMessageConstant.CONSTANT_TYPE_MUST_BE_APPLY_POSITION);
            case SKILL -> throw new BadRequestException(ErrorMessageConstant.CONSTANT_TYPE_MUST_BE_APPLY_SKILL);
            case EXPERIENCE_TYPE ->
                throw new BadRequestException(ErrorMessageConstant.CONSTANT_TYPE_MUST_BE_EXPERIENCE);
            case NOTIFICATION_TYPE ->
                throw new BadRequestException(ErrorMessageConstant.CONSTANT_TYPE_MUST_BE_NOTIFICATION);
            default -> throw new BadRequestException(ErrorMessageConstant.CONSTANT_TYPE_MUST_BE_SYSTEM_ROLE);
        }
    }
}
