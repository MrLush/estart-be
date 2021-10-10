package com.epam.estart.controller;

import com.epam.estart.dto.Project;
import com.epam.estart.model.FilterOptions;
import com.epam.estart.service.impl.ProjectService;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
  private final ProjectService projectService;

  @GetMapping("/{id}")
  public Project getById(@PathVariable UUID id) {
    return projectService.getById(id);
  }

  @PostMapping
  public Project create(@RequestBody Project project) {
    return projectService.create(project);
  }

  @PutMapping
  public Project update(@RequestBody Project project) {
    return projectService.updateAndReturn(project);
  }

  @GetMapping
  public Page<Project> getAllProjectsByFilter(@PageableDefault(size = 10) Pageable pageable,
                                              @RequestParam(defaultValue = "") Set<String> vacantPlaces,
                                              @RequestParam(defaultValue = "") Set<String> stages,
                                              @RequestParam(defaultValue = "") Set<String> tags) {
    return projectService.getAllProjectsByFilter(vacantPlaces, stages, tags, pageable);
  }

  @PostMapping("/filter")
  public Page<Project> postFiltersAndGetResult(@RequestBody FilterOptions options) {
    return projectService.getAllProjectsByFilter(
        options.getVacantPlaces(),
        options.getStages(),
        options.getTags(),
        options.getPageRequest()
    );
  }
}
