package org.dhv.pbl5server.profile_service.controller;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.profile_service.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
}
