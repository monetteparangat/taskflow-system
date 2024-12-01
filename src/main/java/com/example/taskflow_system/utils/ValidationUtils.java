package com.example.taskflow_system.utils;

import java.time.LocalDateTime;

import com.example.taskflow_system.dto.TaskDTO;

public class ValidationUtils {

	public boolean validateTaskDates(TaskDTO taskDTO) {
		boolean isValid = true;
		LocalDateTime startDate = taskDTO.getStartDate();
		LocalDateTime endDateTime = taskDTO.getEndDate();
		
		if(startDate.isBefore(endDateTime)) {
			isValid = false;
		}
			
		return isValid;
	}
}
