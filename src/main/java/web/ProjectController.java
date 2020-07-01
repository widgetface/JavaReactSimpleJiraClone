package web;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import antlr.collections.List;
import entities.Project;
import services.MapValidationErrorService;
import services.ProjectService;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

	
	@Autowired
	ProjectService projectService;

	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	/*
	 *see Controller
	 *Java\Notes\Notes on specific classes
	 *
	 */
	@PostMapping("")
	public ResponseEntity<?> createNewPorject(@Valid @RequestBody Project project, BindingResult result){
		
		ResponseEntity<?> errorMap =  mapValidationErrorService.MapValidationService(result);
		if(errorMap != null) return errorMap;
		Project project1 = projectService.saveOrUpdateProject(project);
		return new ResponseEntity<Project>(project1, HttpStatus.CREATED);
	}
	
	@GetMapping("/{projectId}")
	public ResponseEntity<?> getProjectById(@PathVariable String projectId){
		
		Project project = projectService.findProjectById(projectId.toUpperCase());
		return new ResponseEntity<Project>(project, HttpStatus.OK);
	}

	
	@GetMapping("/all")
	public Iterable<Project> getProjectById(){return  projectService.findAllProjects();}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProjectById(@PathVariable String projectId){
		projectService.deleteProjectById(projectId.toUpperCase());
		String resString = "Project with id " + projectId + " deleted";
		return new ResponseEntity<String>(resString, HttpStatus.OK);
	}
	
	
	
}
