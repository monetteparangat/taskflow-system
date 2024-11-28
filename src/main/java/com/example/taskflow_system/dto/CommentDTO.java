package com.example.taskflow_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

	private Long id;
	private String content;
	private Long taskId;
	private Long userId;
	
}
