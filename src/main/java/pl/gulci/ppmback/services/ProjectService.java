package pl.gulci.ppmback.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.gulci.ppmback.domain.Project;
import pl.gulci.ppmback.repositories.ProjectRepository;

import java.util.Collections;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project saveOrUpdateProject(Project project) {
        return projectRepository.save(project);
    }

    public Iterable<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Iterable<Project> getProjectById(Long id) {
        return projectRepository.findAllById(Collections.singletonList(id));
    }
}
