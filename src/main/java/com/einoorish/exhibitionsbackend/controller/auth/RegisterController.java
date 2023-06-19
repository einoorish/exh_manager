package com.einoorish.exhibitionsbackend.controller.auth;

import com.einoorish.exhibitionsbackend.controller.auth.dto.register.RegistrationRequest;
import com.einoorish.exhibitionsbackend.model.Organization;
import com.einoorish.exhibitionsbackend.service.OrganizationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class RegisterController {

	private final OrganizationsService organizationsService;

	@Autowired
	public RegisterController(OrganizationsService organizationsService) {
		this.organizationsService = organizationsService;
	}

	@GetMapping("/register")
	public String registrationPage(Model model) {
		model.addAttribute("registrationData", new RegistrationRequest());
		return "auth/register";
	}


	@GetMapping("/registration_success")
	public String registrationSuccessPage(Model model) {
		return "auth/registration_success";
	}

	@PostMapping("/register_new_organization")
	public String register(@ModelAttribute RegistrationRequest registrationData) {
		Organization organization = new Organization(registrationData);
		organizationsService.save(organization);
		return "redirect:" + "/registration_success";
	}
}