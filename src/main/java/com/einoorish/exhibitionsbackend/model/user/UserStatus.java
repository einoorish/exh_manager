package com.einoorish.exhibitionsbackend.model.user;


import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Table;

@Table(name = "status")
@AllArgsConstructor
@Getter
public enum UserStatus {
    NEW(1, "НОВА"), APPROVED(0, "ЗАТВЕРДЖЕНА"), BANNED(2, "ЗАБЛОКОВАНА");

    private final int id;

    private final String ukr;
}
