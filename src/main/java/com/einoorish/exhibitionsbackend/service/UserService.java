package com.einoorish.exhibitionsbackend.service;

import com.einoorish.exhibitionsbackend.model.Organization;
import com.einoorish.exhibitionsbackend.model.user.User;
import com.einoorish.exhibitionsbackend.model.user.UserStatus;
import com.einoorish.exhibitionsbackend.repository.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    @Autowired
	private UserRepository repository;


    public User findUserById(long id) {
        return repository.findById(id).orElse(null);
    }

    public User findByUsername(String username){
        return repository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = findByUsername(username);
        if (Objects.isNull(u)) {
            throw new UsernameNotFoundException(String.format("User %s is not found", username));
        }
        return new org.springframework.security.core.userdetails.User(u.getUsername(), u.getPassword(), u.getAuthorities());
    }

    public User save(User user) {
        return repository.save(user);
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return findByUsername(auth.getName());
    }

    public User createNewUser(Organization organization) {
        User user = new User();
        user.setId(organization.getId());
        user.setUsername(organization.getEmail());
        user.setPassword(UUID.randomUUID().toString());
        user.setStatusId(UserStatus.APPROVED.getId());
        user.setRole(0);

        return repository.save(user);
    }

    public User getById(Long authorId) {
        return repository.findById(authorId).orElse(null);
    }
}
