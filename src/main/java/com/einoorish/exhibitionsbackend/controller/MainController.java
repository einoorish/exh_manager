package com.einoorish.exhibitionsbackend.controller;

import com.einoorish.exhibitionsbackend.controller.auth.dto.login.AuthRequest;
import com.einoorish.exhibitionsbackend.controller.auth.dto.register.RegistrationRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage(){
        return "main/main";
    }



    @GetMapping("/login")
    public String loginPage(AuthRequest authRequest){
        return "auth/login";
    }

}
