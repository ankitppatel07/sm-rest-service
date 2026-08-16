package com.web_service.sm_rest_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class UserResponse {
	
	private Integer id;
	private String name;

}
