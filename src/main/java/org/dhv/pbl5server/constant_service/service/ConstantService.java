package org.dhv.pbl5server.constant_service.service;

import org.dhv.pbl5server.constant_service.entity.Constant;
import org.dhv.pbl5server.constant_service.payload.ConstantRequest;

import java.util.List;
import java.util.UUID;

public interface ConstantService {
    Constant getConstantById(UUID id);

    Object getSystemRoles(String constantId);

    List<Constant> getConstantsByType(String type);

    Constant create(ConstantRequest request);
}