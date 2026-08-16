package com.web_service.sm_rest_service.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.web_service.sm_rest_service.dto.UserResponse;
import com.web_service.sm_rest_service.entity.Post;
import com.web_service.sm_rest_service.entity.User;
import com.web_service.sm_rest_service.exception.UserNotFoundException;
import com.web_service.sm_rest_service.repository.PostRepository;
import com.web_service.sm_rest_service.repository.UserRepository;
import com.web_service.sm_rest_service.transformer.UserTransformer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
	
	private UserRepository userRepository;
	private PostRepository postRepository; 
	private UserTransformer userTransformer;
	
	
	public URI createUser(User user) {
		User savedUser = userRepository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
						.path("/{id}")
						.buildAndExpand(savedUser.getId())
						.toUri();  
		
		return location;
	}
	
	
	public List<UserResponse> retrieveAllUsers() {
		List<User> users = userRepository.findAll();
		return userTransformer.toUserResponses(users);
	}
	
	public EntityModel<User> retrieveUser(int id) {
		Optional<User> user = userRepository.findById(id);
		
		if(user.isEmpty())
			throw new UserNotFoundException("User with ID: "+id+" not found.");
		
		EntityModel<User> entityModel = EntityModel.of(user.get());
		
		WebMvcLinkBuilder link =  linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(link.withRel("all-users"));
		
		return entityModel;
	}
	
	
	public void deleteUser(int id) {
		userRepository.deleteById(id);
	}
	
	
	public List<Post> retrievePostsForUser(int id) {
		Optional<User> user = userRepository.findById(id);
		
		if(user.isEmpty())
			throw new UserNotFoundException("User with ID: "+id+" not found.");
		
		return user.get().getPosts();
	}
	
	public ResponseEntity<Object> createPostForUser(int id, Post post) {
		Optional<User> user = userRepository.findById(id);
		
		if(user.isEmpty())
			throw new UserNotFoundException("User with ID: "+id+" not found.");
		
		post.setUser(user.get());
		
		Post savedPost = postRepository.save(post);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedPost.getId())
				.toUri();   

		return ResponseEntity.created(location).build();
	}

}
