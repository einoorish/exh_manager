package com.einoorish.exhibitionsbackend.controller.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthToken {
    private String token;
    private String roles;
}