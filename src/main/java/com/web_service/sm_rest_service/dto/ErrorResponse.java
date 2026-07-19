package com.web_service.sm_rest_service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {

	private LocalDateTime timestamp;
	private String message;
	private String details;

}