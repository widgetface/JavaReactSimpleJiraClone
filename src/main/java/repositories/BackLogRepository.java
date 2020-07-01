package repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import entities.Backlog;
import entities.Project;
import entities.ProjectTask;


@Repository
public interface BackLogRepository extends CrudRepository<ProjectTask, Long> {
 // any property of backlog can be used to findBy and return the matching baclog instance
	// JPA providES this POWER
	Backlog findByProjectIdentifier(String projectId);
	
}
	
	
	

