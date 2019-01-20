package pl.gulci.ppmback.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.gulci.ppmback.domain.Project;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateCheckService {

    private ProjectService projectService;

    @Autowired
    UpdateCheckService(ProjectService projectService) {
        this.projectService = projectService;
    }

    public Map<String, Object> prepareProjectData(Project project) {
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
}
