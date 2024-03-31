package org.dhv.pbl5server.authentication_service.controller;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.service.ApplicationPositionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/account/application-positions")
@RequiredArgsConstructor
public class ApplicationPositionController {
    private final ApplicationPositionService service;
}
