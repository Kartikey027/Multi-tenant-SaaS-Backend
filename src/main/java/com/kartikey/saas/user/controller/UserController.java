package com.kartikey.saas.user.controller;

import com.kartikey.saas.user.controller.dto.CreateUserRequest;
import com.kartikey.saas.user.controller.dto.UserResponse;
import com.kartikey.saas.user.entity.User;
import com.kartikey.saas.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/internal/admin/tenants/{tenantId}/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(
            @PathVariable UUID tenantId,
            @RequestBody CreateUserRequest request
            ){
            User user= userService.createUser(
                    tenantId,
                    request.getEmail(),
                    request.getPassword()
            );

            return UserResponse.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .status(user.getStatus())
                    .build();
    }

    @PostMapping("/{userId}/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disableUser(
            @PathVariable UUID tenantId,
            @PathVariable Long userId
    ){
        userService.disableUser(tenantId,userId);
    }

    @PostMapping("/{userId}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enableUser(
            @PathVariable UUID tenantId,
            @PathVariable Long userId
    ){
        userService.enableUser(tenantId,userId);
    }
}
