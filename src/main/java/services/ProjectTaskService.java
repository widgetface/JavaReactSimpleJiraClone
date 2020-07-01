package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entities.Backlog;
import entities.Project;
import entities.ProjectTask;
import exceptions.ProjectIdException;
import exceptions.ProjectNotFoundException;
import repositories.BackLogRepository;
import repositories.ProjectRepository;
import repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {

	@Autowired
	private BackLogRepository backlogRepository;
	
	@Autowired 
	private ProjectTaskRepository projectTaskRepository;
	
	
	@Autowired
	private ProjectRepository projectRepository;
	
	public ProjectTask addProjectTask(String projectId, ProjectTask projectTask) {
		
		try {
			Backlog bl = backlogRepository.findByProjectIdentifier(projectId);
			
			projectTask.setBacklog(bl);
			
			Integer backlogSequence = bl.getPTSequence();
			backlogSequence++;
			bl.setPTSequence(backlogSequence);
			
			projectTask.setProjectSequence(projectId + "-"+backlogSequence.toString());
			projectTask.setProjectIdentifier(projectId);
			Integer priority = projectTask.getPriority();
			String status = projectTask.getStatus();
			
			if(priority == null){
				projectTask.setPriority(3);
			}
			
			// use enums here
			if(status == null || status == null){
				projectTask.setStatus("TODO");
			}
			
			return projectTaskRepository.save(projectTask);
		} catch (Exception e) {
			
				throw new ProjectNotFoundException("Project with id " + projectId + " not found");

		}
		
	}
	
	
	public Iterable<ProjectTask>findBacklogById(String id){
        
		Project project = projectRepository.findByProjectIdentifier(id);
		
		if(project == null) {
			String message = "Project with " + id + " does not exist";
			throw new ProjectNotFoundException(message);
		}
		
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }
	
    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id){

        //make sure we are searching on an existing backlog
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if(backlog==null){
            throw new ProjectNotFoundException("Project with ID: '"+backlog_id+"' does not exist");
        }

        //make sure that our task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);

        if(projectTask == null){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"' not found");
        }

        //make sure that the backlog/project id in the path corresponds to the right project
        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"' does not exist in project: '"+backlog_id);
        }


        return projectTask;
    }
    
    public ProjectTask updateProjectTask(ProjectTask updatedTask){
    	
    	ProjectTask projectTask = projectTaskRepository.findByProjectSequence(updatedTask.getProjectSequence());
        
		if(projectTask  == null) {
			String message = "Project task with id" + updatedTask.getProjectSequence() + " does not exist";
			throw new ProjectNotFoundException(message);
		}
		
    	projectTask = updatedTask;
    	
    	return projectTaskRepository.save(projectTask);
    }
    
    public void deleteProjectTask(String backlog_id, String pt_id) {
     ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);
     projectTaskRepository.delete(projectTask);
    }
}
