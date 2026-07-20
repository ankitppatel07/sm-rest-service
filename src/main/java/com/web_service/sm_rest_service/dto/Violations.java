package com.web_service.sm_rest_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Violations {
	private String inputName;
	private String errorMessage;
}
