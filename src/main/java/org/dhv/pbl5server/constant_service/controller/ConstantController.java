package org.dhv.pbl5server.constant_service.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.constant_service.service.ConstantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ConstantController {
    private final ConstantService service;

    @GetMapping("/constant/system-roles")
    public ResponseEntity<ApiDataResponse> getSystemRoles(@Nullable @RequestParam(name = "constant_id") String constantId) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getSystemRoles(constantId)));
    }

    @GetMapping("/constant")
    public ResponseEntity<ApiDataResponse> getConstantsByType(@NotNull @RequestParam(name = "constant_type") String type) {
        try {
            return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getConstantsByType(type)));
        } catch (NumberFormatException e) {
            throw new BadRequestException(ErrorMessageConstant.CONSTANT_TYPE_MUST_BE_NUMBER);
        }
    }

    @GetMapping("/constant/{constant_id}")
    public ResponseEntity<ApiDataResponse> getConstantById(@PathVariable(name = "constant_id") String constantId) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getConstantById(UUID.fromString(constantId))));
    }

}
