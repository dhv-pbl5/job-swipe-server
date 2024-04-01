package org.dhv.pbl5server.constant_service.service;

import org.dhv.pbl5server.constant_service.enums.ConstantType;
import org.dhv.pbl5server.constant_service.payload.ConstantResponse;

import java.util.List;
import java.util.UUID;

public interface ConstantService {

    void checkConstantWithType(UUID id, ConstantType type);

    void checkConstantWithType(List<UUID> ids, ConstantType type);

    ConstantResponse getConstantById(UUID id);

    Object getSystemRoles(String constantId);

    List<ConstantResponse> getConstantsByType(String type);

}