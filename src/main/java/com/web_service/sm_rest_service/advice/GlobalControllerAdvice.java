package com.web_service.sm_rest_service.advice;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.web_service.sm_rest_service.dto.ErrorResponse;
import com.web_service.sm_rest_service.dto.Violations;
import com.web_service.sm_rest_service.exception.UserNotFoundException;
import com.web_service.sm_rest_service.util.AppConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler{
	
	//to extract request details(Method and URI) for logging purposes.
	private String getRequestDetails(WebRequest request) {
		String desc = request.getDescription(false);
		//remove the prefix "uri=" from description
		return desc.replace("uri=", "");
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) throws Exception {
		log.info("{} Handling all other exceptions. Request {}", AppConstants.SM_5001, getRequestDetails(request));

		String message = StringUtils.hasText(ex.getMessage())
				? ex.getMessage()
						: "An unexpected error occurred.";
		
		log.error("{} UnhandledExceptionType: {} | ErrorMessage: {}", AppConstants.SM_5001, ex.getClass().getName(), message, ex);

		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ErrorResponse.builder()
						.status(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
						.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
						.message(AppConstants.SM_5001 + message)
						.timestamp(new Timestamp(System.currentTimeMillis()))
						.build());
	}

	@ExceptionHandler(UserNotFoundException.class)
	public final ResponseEntity<ErrorResponse> handleUserNotFoundException(Exception ex, WebRequest request) throws Exception {
		log.info("{} Handling UserNotFoundException. Request {}", AppConstants.SM_5001, getRequestDetails(request));
		log.error("{} {}", AppConstants.SM_5001, ex.getMessage());
		
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(ErrorResponse.builder()
						.status(HttpStatus.NOT_FOUND.getReasonPhrase())
						.code(HttpStatus.NOT_FOUND.value())
						.message(AppConstants.SM_5001 + "User Not Found.")
						.timestamp(new Timestamp(System.currentTimeMillis()))
						.build());
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, 
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.info("{} Handling MethodArgumentNotValidException. Request {}", AppConstants.SM_5002, getRequestDetails(request));
		
		List<Violations> violations = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(error -> new Violations(error.getField(), error.getDefaultMessage()))
				.collect(Collectors.toList());
		
		//globalErrors: validation errors that are not tied to a field(e.g: class-lvl constraints)
		ex.getBindingResult()
			.getGlobalErrors()
			.forEach(error -> violations.add(
					new Violations(error.getObjectName(), error.getDefaultMessage())
			));
		
		log.error("{} Validation failed. ViolationCount: {} | Violations: {}", AppConstants.SM_5002, violations.size(), violations);
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.builder()
						.status(HttpStatus.BAD_REQUEST.getReasonPhrase())
						.code(HttpStatus.BAD_REQUEST.value())
						.message(AppConstants.SM_5002 + "Validation failed for input parameter(s)")
						.timestamp(new Timestamp(System.currentTimeMillis()))
						.violations(violations)
						.build());
		
	}

	
}