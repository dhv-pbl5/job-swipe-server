package org.dhv.pbl5server.constant_service.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.constant_service.enums.ConstantTypePrefix;
import org.dhv.pbl5server.constant_service.service.ConstantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/constants")
@RequiredArgsConstructor
public class ConstantController {
    private final ConstantService service;

    @GetMapping("/system-roles")
    public ResponseEntity<ApiDataResponse> getSystemRoles(@Nullable @RequestParam(name = "constant_id") String constantId) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getSystemRoles(constantId)));
    }

    @GetMapping("/types")
    public ResponseEntity<ApiDataResponse> getConstantTypes() {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getConstantTypes()));
    }

    @GetMapping("")
    public ResponseEntity<ApiDataResponse> getConstantsByType(
        @NotNull @RequestParam(name = "constant_type") String type,
        @Nullable @RequestParam(name = "is_prefix") Boolean prefix
    ) {
        if (prefix != null && prefix) {
            if (!Character.isDigit(type.charAt(0))) {
                var typePrefix = ConstantTypePrefix.valueOf(type.toUpperCase());
                return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getConstantsByTypePrefix(typePrefix)));
            } else if (type.length() >= 2) {
                var typePrefix = AbstractEnum.fromString(ConstantTypePrefix.values(), type.substring(0, 2));
                return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getConstantsByTypePrefix(typePrefix)));
            }
        }
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getConstantByType(type)));
    }

    @GetMapping("/{constant_id}")
    public ResponseEntity<ApiDataResponse> getConstantById(@PathVariable(name = "constant_id") String constantId) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getConstantById(UUID.fromString(constantId))));
    }

    @GetMapping("/prefixes")
    public ResponseEntity<ApiDataResponse> getAllPrefixes() {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getAllPrefixes()));
    }
}
