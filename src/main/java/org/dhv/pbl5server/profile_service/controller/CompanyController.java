package org.dhv.pbl5server.profile_service.controller;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.profile_service.service.CompanyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/company")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService service;
}
