package com.einoorish.exhibitionsbackend.repository.auth;

import com.einoorish.exhibitionsbackend.model.Organization;
import com.einoorish.exhibitionsbackend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
