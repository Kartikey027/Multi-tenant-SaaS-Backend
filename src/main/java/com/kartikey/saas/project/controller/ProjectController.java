package com.kartikey.saas.project.controller;

import com.kartikey.saas.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public void createProject(@RequestBody CreateProjectRequest request){
        projectService.createProject(request.name());
    }

    @PostMapping("/{projectId}/archive")
    public void archiveProject(@PathVariable Long projectId){
        projectService.archiveProject(projectId);
    }
}
