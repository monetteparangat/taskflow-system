package com.example.taskflow_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

	private Long id;
	@NotNull
	private String name;
	@Email
	@NotNull
	private String email;

	@NotNull
	private String password;

	private Long roleId;

}
