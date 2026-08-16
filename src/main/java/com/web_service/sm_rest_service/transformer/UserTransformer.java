package com.web_service.sm_rest_service.transformer;

import java.util.List;
import java.util.stream.Collectors;

import com.web_service.sm_rest_service.dto.UserResponse;
import com.web_service.sm_rest_service.entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserTransformer {
	
	public List<UserResponse> toUserResponses(List<User> users) {		
		return users.stream()
				.map((User user) -> {
					return UserResponse.builder()
					.id(user.getId())
					.name(user.getName())
					.build();
				})
				.collect(Collectors.toList());
	}
}
