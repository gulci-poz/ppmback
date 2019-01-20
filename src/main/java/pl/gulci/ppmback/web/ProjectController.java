package pl.gulci.ppmback.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.gulci.ppmback.domain.Project;
import pl.gulci.ppmback.services.ProjectService;
import pl.gulci.ppmback.services.ResultValidationService;
import pl.gulci.ppmback.services.UpdateCheckService;

import javax.validation.Valid;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private ProjectService projectService;
    private ResultValidationService resultValidationService;
    private UpdateCheckService updateCheckService;

    @Autowired
    public ProjectController(ProjectService projectService,
                             ResultValidationService resultValidationService,
                             UpdateCheckService updateCheckService) {
        this.projectService = projectService;
        this.resultValidationService = resultValidationService;
        this.updateCheckService = updateCheckService;
    }

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result) {

        ResponseEntity<?> errors = resultValidationService.validateResult(result);

        if (errors != null) {
            // response contains errors
            return errors;
        }

        // in case of updating projectIdentifier we want to include a message in JSON
        // preparing the data
        Map<String, Object> projectData = updateCheckService.prepareProjectData(project);

        // no validation errors
        Project savedProject = projectService.saveOrUpdateProject(project);

        // inserting a message instead of updated projectIdentifier
        // we don't inform about attempt to change createAt (type mismatch - String and Date)
        // TODO: 20.01.19 extract to method - correctFields
        if (projectData.get("projectId") != null
                && !((Boolean) projectData.get("newProject"))) {
            // update situation, projectIdentifier change
            if (!projectData.get("oldProjectIdentifier").equals(projectData.get("newProjectIdentifier"))) {
                savedProject.setProjectIdentifier("Project identifier is not updatable");
            }
            // update situation, we don't want to see null createAt date in response
            savedProject.setCreatedAt((Date) projectData.get("createdAt"));
        }

        // TODO: 20.01.19 unique project id exception - id not unique (next id shouldn't be omitted) (continue 9min)
        // TODO: 20.01.19 create CreateUpdateService and move the logic there (also ResultValidation and UpdateCheck)

        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Project>> getAllProjects() {
        Iterable<Project> allProjects = projectService.getAllProjects();
        return new ResponseEntity<>(allProjects, HttpStatus.CREATED);
    }
}
