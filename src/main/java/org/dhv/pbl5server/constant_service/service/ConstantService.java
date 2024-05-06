package org.dhv.pbl5server.constant_service.service;

import org.dhv.pbl5server.constant_service.enums.ConstantTypePrefix;
import org.dhv.pbl5server.constant_service.payload.ConstantResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ConstantService {

    void checkConstantWithType(UUID id, ConstantTypePrefix type);

    void checkConstantWithType(List<UUID> ids, ConstantTypePrefix type);

    ConstantResponse getConstantById(UUID id);

    Object getSystemRoles(String constantId);

    List<ConstantResponse> getConstantsByTypePrefix(ConstantTypePrefix prefix);

    ConstantResponse getConstantByType(String type);

    List<Object> getConstantTypes();

    List<Map<String, String>> getAllPrefixes();
}