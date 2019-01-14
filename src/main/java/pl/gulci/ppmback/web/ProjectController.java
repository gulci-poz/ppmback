package pl.gulci.ppmback.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gulci.ppmback.domain.Project;
import pl.gulci.ppmback.services.ProjectService;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("")
    public ResponseEntity<Project> createNewProject(@RequestBody Project project) {
        projectService.saveOrUpdateProject(project);
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Project>> getAllProjects() {
        Iterable<Project> allProjects = projectService.getAllProjects();
        return new ResponseEntity<>(allProjects, HttpStatus.CREATED);
    }
}
