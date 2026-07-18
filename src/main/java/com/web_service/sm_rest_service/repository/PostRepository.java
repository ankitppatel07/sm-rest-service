package com.web_service.sm_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web_service.sm_rest_service.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {

}