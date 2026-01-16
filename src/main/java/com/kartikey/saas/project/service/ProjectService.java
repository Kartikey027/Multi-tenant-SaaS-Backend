package com.kartikey.saas.project.service;

import com.kartikey.saas.common.exception.ForbiddenOperationException;
import com.kartikey.saas.common.exception.ResourceNotFoundException;
import com.kartikey.saas.common.security.SecurityUtils;
import com.kartikey.saas.common.tenant.TenantContext;
import com.kartikey.saas.project.entity.Project;
import com.kartikey.saas.project.entity.ProjectStatus;
import com.kartikey.saas.project.policy.ProjectPolicy;
import com.kartikey.saas.project.repository.ProjectRepo;
import com.kartikey.saas.user.entity.User;
import com.kartikey.saas.user.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {

    private final ProjectRepo projectRepo;
    private final UserRepo userRepo;
    private final ProjectPolicy projectPolicy;

    public Project createProject(String name){
        UUID tenantId= TenantContext.getTenantId();

        User currentUser=userRepo
                .findByTenant_TenantIdAndEmail(
                        tenantId,
                        SecurityUtils.currentUserEmail()
                )
                .orElseThrow(()->
                        new IllegalStateException("Authenticated User not found")
                );
        if (!projectPolicy.canCreateProject(currentUser)){
            throw new ForbiddenOperationException("Not allowed to create new project");
        }

        Project project=new Project();
        project.setName(name);
        project.setTenant(currentUser.getTenant());
        project.setStatus(ProjectStatus.ACTIVE);

        return projectRepo.save(project);
    }

    public void archiveProject(Long projectId){
        UUID tenantId=TenantContext.getTenantId();

        Project project=projectRepo
                .findByTenant_TenantIdAndId(tenantId,projectId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Project Not Found")
                );
        project.setStatus(ProjectStatus.ARCHIVED);
    }
}
