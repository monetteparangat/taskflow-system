package com.example.taskflow_system.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Workflow {

	private Long id;
	
	@NotNull
	@Size(max = 255)
	private String name;
	
	@Size(max = 500)
	private String description;
	
}
