package org.dhv.pbl5server.constant_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.exception.NotFoundObjectException;
import org.dhv.pbl5server.constant_service.entity.Constant;
import org.dhv.pbl5server.constant_service.enums.ConstantType;
import org.dhv.pbl5server.constant_service.payload.ConstantRequest;
import org.dhv.pbl5server.constant_service.repository.ConstantRepository;
import org.dhv.pbl5server.constant_service.service.ConstantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConstantServiceImpl implements ConstantService {
    private final ConstantRepository repository;

    @Override
    public Constant getConstantById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.CONSTANT_NOT_FOUND));
    }

    @Override
    public Object getSystemRoles(String constantId) {
        if (constantId == null || constantId.isBlank()) {
            return repository.findByConstantType(ConstantType.SYSTEM_ROLE.name());
        }
        Constant constant = getConstantById(UUID.fromString(constantId));
        if (constant == null || !constant.getConstantType().equalsIgnoreCase(ConstantType.SYSTEM_ROLE.name()))
            throw new NotFoundObjectException(ErrorMessageConstant.SYSTEM_ROLE_NOT_FOUND);
        return constant;
    }

    @Override
    public List<Constant> getConstantsByType(String type) {
        return repository.findByConstantType(type);
    }

    @Override
    public Constant create(ConstantRequest request) {
        return repository.save(Constant.builder()
            .constantType(request.getType().name())
            .constantName(request.getName())
            .build());
    }
}
