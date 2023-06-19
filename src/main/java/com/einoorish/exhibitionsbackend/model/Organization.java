package com.einoorish.exhibitionsbackend.model;

import com.einoorish.exhibitionsbackend.controller.auth.dto.register.RegistrationRequest;
import com.einoorish.exhibitionsbackend.model.user.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "organizations")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String fullName;
    String organizationName;
    String physicalAddress;
    String email;
    String phone;
    String directions;
    String type;

    @Transient
    String statusUkr;

    String status;

    public Organization(RegistrationRequest registrationData) {
        this.fullName = registrationData.getFullName();
        this.organizationName = registrationData.getOrganizationName();
        this.physicalAddress = registrationData.getPhysicalAddress();
        this.email = registrationData.getEmail();
        this.phone = registrationData.getPhone();
        this.directions = Arrays.stream(registrationData.getDirections())
                .map(Enum::name).collect(Collectors.joining(","));
        this.type = registrationData.getType().name();
        this.status = UserStatus.NEW.name();
    }
}
