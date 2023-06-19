package com.einoorish.exhibitionsbackend.controller.auth.dto.register;

import com.einoorish.exhibitionsbackend.model.exhibit.ExhibitType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RegistrationRequest {
    String fullName;
    String organizationName;
    String physicalAddress;
    String email;
    String phone;
    ExhibitType[] directions;
    Type type;
}
