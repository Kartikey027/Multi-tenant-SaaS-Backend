package com.kartikey.saas.user.controller.dto;

import com.kartikey.saas.user.entity.UserStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private Long userId;
    private String email;
    private UserStatus status;
}
