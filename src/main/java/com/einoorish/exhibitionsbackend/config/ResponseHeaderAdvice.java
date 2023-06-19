package com.einoorish.exhibitionsbackend.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ResponseHeaderAdvice {
 
    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
    }
}