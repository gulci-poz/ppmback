package pl.gulci.ppmback.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import pl.gulci.ppmback.domain.Project;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CreateUpdateHelperService {

    private ProjectService projectService;

    CreateUpdateHelperService(ProjectService projectService) {
        this.projectService = projectService;
    }

    public ResponseEntity<?> helpCreateUpdateProject(Project project, BindingResult result) {

        ResponseEntity<?> errors = validateResult(result);

        if (errors != null) {
            // response contains errors
            return errors;
        }

        // in case of updating projectIdentifier we want to include a message in JSON
        // preparing the data
        Map<String, Object> projectData = prepareProjectData(project);

        // no validation errors
        Project savedProject = projectService.saveOrUpdateProject(project);

        // refining data to return
        savedProject = refineData(savedProject, projectData);

        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    private ResponseEntity<?> validateResult(BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }

        return null;
    }

    private Map<String, Object> prepareProjectData(Project project) {
        String oldProjectIdentifier = null;
        String newProjectIdentifier = project.getProjectIdentifier();
        Date createdAt = null;
        Long projectId = project.getId();
        Object newProject = false;
        Map<String, Object> projectData = new HashMap<>();

        if (projectId != null) {
            // id present in request body
            Iterable<Project> projectsById = projectService.getProjectById(projectId);
            if (projectsById.iterator().hasNext()) {
                // opearation is update
                Project existingProjectInstance = projectsById.iterator().next();
                oldProjectIdentifier = existingProjectInstance.getProjectIdentifier();
                createdAt = existingProjectInstance.getCreatedAt();
            } else {
                // operation is create
                newProject = true;
            }
        }

        projectData.put("projectId", projectId);
        projectData.put("newProject", newProject);
        projectData.put("oldProjectIdentifier", oldProjectIdentifier);
        projectData.put("newProjectIdentifier", newProjectIdentifier);
        projectData.put("createdAt", createdAt);

        return projectData;
    }

    private Project refineData(Project savedProject, Map<String, Object> projectData) {
        Project project = null;

        try {
            project = (Project) savedProject.clone();

            // inserting a message instead of updated projectIdentifier
            // we don't inform about attempt to change createAt (type mismatch - String and Date)
            if (projectData.get("projectId") != null
                    && !((Boolean) projectData.get("newProject"))) {
                // update situation, projectIdentifier change
                if (!projectData.get("oldProjectIdentifier").equals(projectData.get("newProjectIdentifier"))) {
                    project.setProjectIdentifier("Project identifier is not updatable");
                }
                // update situation, we don't want to see null createAt date in response
                project.setCreatedAt((Date) projectData.get("createdAt"));
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        if (project != null) {
            return project;
        }

        return savedProject;
    }
}
