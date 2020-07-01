package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entities.Backlog;
import entities.Project;
import exceptions.ProjectIdException;
import repositories.BackLogRepository;
import repositories.ProjectRepository;

@Service
public class ProjectService {

	// inject project repo that allows crud 
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private BackLogRepository backLogRepository;
	
	public Project saveOrUpdateProject(Project project) {
		
		try {
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			/*
			 * Create a backlog if the project has just been saved
			 * If you are updating a project the id is included
			 * JPA knows how to handle that
			 */
			if(project.getId() == null) {
				Backlog backLog = new Backlog();
				project.setBacklog(backLog);
				backLog.setProject(project);
				backLog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			}
			
			/*
			 *  updating project
			 *  But have to make sure it knows the backlog it has been assigned with which it'll be null
			 *  if the below isn't done
			 */
			if(project.getId() != null ) {
				Backlog bl = backLogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase());
				project.setBacklog(bl);
			}
			
			return projectRepository.save(project);
			
		} catch(Exception ex) {
			throw new ProjectIdException(" Project Id already in use (id = " + project.getProjectIdentifier().toUpperCase() + ") ");
		}
		
	}
	
	public Project findProjectById(String projectId) {
		
		Project project = projectRepository.findByProjectIdentifier(projectId);
		
		if(project == null) {
			throw new ProjectIdException("Project with id " + projectId + "  does not exist");
		}
		return projectRepository.findByProjectIdentifier(projectId.toUpperCase());
	}
	
	public Iterable<Project> findAllProjects(){
		
		return projectRepository.findAll();
		
	}
	
	public void deleteProjectById(String projectId) {
		Project project = projectRepository.findByProjectIdentifier(projectId);
		
		if(project == null) {
			throw new ProjectIdException("Cannot delete project roject with id " + projectId + " . It does not exist");
		}
		
		projectRepository.delete(project);
		
	}
}
