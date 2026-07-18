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
import com.web_service.sm_rest_service.entity.Post;
import com.web_service.sm_rest_service.entity.User;
import com.web_service.sm_rest_service.repository.PostRepository;
import com.web_service.sm_rest_service.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
	
	private UserRepository userRepository;
	private PostRepository postRepository; 
	
	
	public URI createUser(User user) {
		User savedUser = userRepository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
						.path("/{id}")
						.buildAndExpand(savedUser.getId())
						.toUri();  
		
		return location;
	}
	
	
	public List<User> retrieveAllUsers() {
		return userRepository.findAll();
	}
	
	public EntityModel<User> retrieveUser(int id) {
		Optional<User> user = userRepository.findById(id);
		
//		if(user.isEmpty())
//			throw new UserNotFoundException("id:"+id);
		
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
		
//		if(user.isEmpty())
//			throw new UserNotFoundException("id:"+id);
		
		return user.get().getPosts();
	}
	
	public ResponseEntity<Object> createPostForUser(int id, Post post) {
		Optional<User> user = userRepository.findById(id);
		
//		if(user.isEmpty())
//			throw new UserNotFoundException("id:"+id);
		
		post.setUser(user.get());
		
		Post savedPost = postRepository.save(post);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedPost.getId())
				.toUri();   

		return ResponseEntity.created(location).build();
	}

}
