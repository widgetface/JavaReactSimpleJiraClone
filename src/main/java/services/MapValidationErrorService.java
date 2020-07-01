package services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class MapValidationErrorService {

	public ResponseEntity<?> MapValidationService(BindingResult bindingResult){
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			for(org.springframework.validation.FieldError error: bindingResult.getFieldErrors() ) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
		}
		
		return null;
	}
}
