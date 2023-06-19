package com.einoorish.exhibitionsbackend.controller;

import com.einoorish.exhibitionsbackend.model.Organization;
import com.einoorish.exhibitionsbackend.service.EmailService;
import com.einoorish.exhibitionsbackend.service.OrganizationsService;
import com.einoorish.exhibitionsbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class OrganizationController {

    private final OrganizationsService organizationsService;

    private final EmailService emailService;

    private final UserService userService;

    @Autowired
    public OrganizationController(OrganizationsService organizationsService, EmailService emailService, UserService userService) {
        this.organizationsService = organizationsService;
        this.emailService = emailService;
        this.userService = userService;
    }


    @GetMapping("/organizations")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String findAll(Model model){
        List<Organization> organizations = organizationsService.findAll();
        model.addAttribute("organizations", organizations);

        return "admin/admin_panel";
    }

    @PostMapping("/approve/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createToursForm(Long id){
        organizationsService.approveOrganization(id);
        return "redirect:" + "/organizations";
    }

    @PostMapping("/decline/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createToursForm(@PathVariable("id") Long id, String reason){
        organizationsService.rejectOrganization(id);
        return "redirect:" + "/organizations";
    }

    @PostMapping("/ban/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String banOrganization(@PathVariable("id") Long id, String reason){
        organizationsService.ban(id);
        return "redirect:" + "/organizations";
    }

}
