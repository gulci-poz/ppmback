package pl.gulci.ppmback.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.gulci.ppmback.domain.Project;
import pl.gulci.ppmback.services.ProjectService;
import pl.gulci.ppmback.services.ResultValidationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private ProjectService projectService;
    private ResultValidationService resultValidationService;

    @Autowired
    public ProjectController(ProjectService projectService, ResultValidationService resultValidationService) {
        this.projectService = projectService;
        this.resultValidationService = resultValidationService;
    }

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result) {

        ResponseEntity<?> errors = resultValidationService.validateResult(result);

        if (errors != null) {
            // response contains errors
            return errors;
        }

        // no errors
        Project savedProject = projectService.saveOrUpdateProject(project);
        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Project>> getAllProjects() {
        Iterable<Project> allProjects = projectService.getAllProjects();
        return new ResponseEntity<>(allProjects, HttpStatus.CREATED);
    }
}
