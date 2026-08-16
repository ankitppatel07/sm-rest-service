package com.web_service.sm_rest_service.controller;

import java.net.URI;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web_service.sm_rest_service.dto.UserResponse;
import com.web_service.sm_rest_service.entity.Post;
import com.web_service.sm_rest_service.entity.User;
import com.web_service.sm_rest_service.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/app")
@Tag(name = "Social Media Service", description = "APIs for performing CRUD operations on Users and their Posts"
		)
public class UserController {
	
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<UserResponse>> retrieveAllUsers() {
		log.info("Request to retrieve all the users is received.");
		List<UserResponse> response = userService.retrieveAllUsers();
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		log.info("Request to create the user is received.");
		URI location = userService.createUser(user);   	
		return ResponseEntity.created(location).build();
	}

	@GetMapping("/users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable int id) {
		log.info("Request to retrieve the user {} is received.", id);
		EntityModel<User> entityModel = userService.retrieveUser(id);
		return entityModel;
	}
	
	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable int id) {
		log.info("Request to delete the user {} is received.", id);
		userService.deleteUser(id);
	}
	
	@GetMapping("/users/{id}/posts")
	public List<Post> retrievePostsForUser(@PathVariable int id) {
		log.info("Request to retrieve posts for the user {} is received.", id);
		return userService.retrievePostsForUser(id);
	}
	
	@PostMapping("/users/{id}/posts")
	public ResponseEntity<Object> createPostForUser(@PathVariable int id, @Valid @RequestBody Post post) {
		log.info("Request to create post for the user {} is received.", id);
		return userService.createPostForUser(id, post);
	}

}