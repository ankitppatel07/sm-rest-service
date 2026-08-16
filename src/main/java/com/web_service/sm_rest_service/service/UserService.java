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
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

	private UserRepository userRepository;
	private PostRepository postRepository;
	private UserTransformer userTransformer;

	public URI createUser(User user) {
		User savedUser = userRepository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();
		return location;
	}

	public List<UserResponse> retrieveAllUsers() {
		log.info("Retrieving all the Users.");

		List<User> users = userRepository.findAll();

		return userTransformer.toUserResponses(users);
	}

	/* Demonstrating HATEOAS */
	public EntityModel<User> retrieveUser(int id) {
		log.info("Retrieving User with ID: {}.", id);
		
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User with ID: " + id + " not found."));

		EntityModel<User> entityModel = EntityModel.of(user);

		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(link.withRel("all-users"));

		return entityModel;
	}

	public void deleteUser(int id) {
		log.info("Deleting User with ID: {}.", id);
		
		userRepository.deleteById(id);
	}

	public List<Post> retrievePostsForUser(int id) {
		log.info("Retrieving Posts for User with ID: {}.", id);
		
		Optional<User> user = userRepository.findById(id);

		if (user.isEmpty())
			throw new UserNotFoundException("User with ID: " + id + " not found.");

		return user.get().getPosts();
	}

	public ResponseEntity<Object> createPostForUser(int id, Post post) {
		log.info("Creating Post for User with ID: {}.", id);
		
		Optional<User> user = userRepository.findById(id);

		if (user.isEmpty())
			throw new UserNotFoundException("User with ID: " + id + " not found.");

		post.setUser(user.get());

		Post savedPost = postRepository.save(post);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedPost.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

}
