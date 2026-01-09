package com.kartikey.saas.user.policy;

import com.kartikey.saas.user.entity.User;

public class UserActionContext {

    private final User currentUser;
    private final User targetUser;

    public UserActionContext(User currentUser,User targetUser){
        this.currentUser=currentUser;
        this.targetUser=targetUser;
    }

    public User getCurrentUser(){
        return currentUser;
    }

    public User getTargetUser(){
        return targetUser;
    }
}
