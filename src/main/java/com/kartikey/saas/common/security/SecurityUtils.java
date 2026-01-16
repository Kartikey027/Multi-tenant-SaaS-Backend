package com.kartikey.saas.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {
    private SecurityUtils(){}

    public static String currentUserEmail(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        if (authentication==null || !authentication.isAuthenticated()){
            throw new IllegalStateException("No Authenticated user Found");
        }

        return authentication.getName();
    }
}
