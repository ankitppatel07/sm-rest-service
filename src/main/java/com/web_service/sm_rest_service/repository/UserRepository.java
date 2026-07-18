package com.web_service.sm_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web_service.sm_rest_service.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}