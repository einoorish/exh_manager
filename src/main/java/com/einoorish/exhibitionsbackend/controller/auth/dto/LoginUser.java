package com.einoorish.exhibitionsbackend.controller.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LoginUser {
    private String username;
    private String password;
}
