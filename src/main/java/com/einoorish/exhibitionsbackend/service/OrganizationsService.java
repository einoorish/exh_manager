package com.einoorish.exhibitionsbackend.service;

import com.einoorish.exhibitionsbackend.model.Organization;
import com.einoorish.exhibitionsbackend.model.user.User;
import com.einoorish.exhibitionsbackend.model.user.UserStatus;
import com.einoorish.exhibitionsbackend.repository.auth.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrganizationsService {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    EmailService emailService;


    @Autowired
    UserService userService;

    OrganizationsService(OrganizationRepository organizationRepository, EmailService emailService){
        this.organizationRepository = organizationRepository;
        this.emailService = emailService;
    }

    public List<Organization> findAll(){
        List<Organization> orgs = organizationRepository.findAll();
        orgs.forEach(
                org -> org.setStatusUkr(UserStatus.valueOf(org.getStatus()).getUkr())
        );
        return orgs;
    }

    public Organization findById(Long id){
        Organization org =  organizationRepository.findById(id).orElse(null);
        if(org != null) {
            org.setStatusUkr(UserStatus.valueOf(org.getStatus()).getUkr());
        }
        return org;
    }

    public void approveOrganization(Long id){
        Organization organization = findById(id);
        organization.setStatus(UserStatus.APPROVED.name());
        organizationRepository.save(organization);

        User user = userService.createNewUser(organization);
        emailService.sendApprovalMail(organization.getEmail(), user.getPassword());
    }

    public void rejectOrganization(Long id){
        Organization organization = findById(id);
        organization.setStatus(UserStatus.BANNED.name());
        organizationRepository.save(organization);
        emailService.sendRejectionMail(organization.getEmail());
    }

    public void save(Organization organization) {
        organizationRepository.save(organization);
    }

    public void ban(Long id) {
        Organization organization = findById(id);
        organization.setStatus(UserStatus.BANNED.name());
        organizationRepository.save(organization);
    }
}
