package pl.gulci.ppmback.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.gulci.ppmback.domain.Project;
import pl.gulci.ppmback.services.CreateUpdateHelperService;
import pl.gulci.ppmback.services.ProjectService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private ProjectService projectService;
    private CreateUpdateHelperService createUpdateHelperService;

    @Autowired
    public ProjectController(ProjectService projectService,
                             CreateUpdateHelperService createUpdateHelperService) {
        this.projectService = projectService;
        this.createUpdateHelperService = createUpdateHelperService;
    }

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result) {
        return createUpdateHelperService.helpCreateUpdateProject(project, result);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Project>> getAllProjects() {
        return new ResponseEntity<>(projectService.getAllProjects(), HttpStatus.CREATED);
    }
}
