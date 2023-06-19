package com.einoorish.exhibitionsbackend.repository.auth;

import com.einoorish.exhibitionsbackend.model.user.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>{
	User findByUsername(String username);

}
