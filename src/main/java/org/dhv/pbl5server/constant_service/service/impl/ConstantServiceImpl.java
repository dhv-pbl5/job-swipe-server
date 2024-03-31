package org.dhv.pbl5server.constant_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.common_service.constant.CommonConstant;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.exception.NotFoundObjectException;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.constant_service.entity.Constant;
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
}
