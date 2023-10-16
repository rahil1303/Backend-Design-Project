package com.upgrad.bookmyconsultation.repository;

import com.upgrad.bookmyconsultation.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

	@Override
	List<User> findAll();

	// Method to find a User by their email ID
	User findByEmailId(String emailId);
}

