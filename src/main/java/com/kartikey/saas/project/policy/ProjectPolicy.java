package com.kartikey.saas.project.policy;

import com.kartikey.saas.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ProjectPolicy {
    public boolean canCreateProject(User currentUser){
        return true;
    }
    public boolean canArchiveProject(User currentUser){
        return true;
    }
}
