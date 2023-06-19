package com.einoorish.exhibitionsbackend.controller.auth.dto.register;

import lombok.Getter;

public enum Type {
    MUSEUM("МУЗЕЙ"),
    LIBRARY("БІБЛІОТЕКА"),
    RESERVATION("РЕЗЕРВАЦІЯ"),
    ARCHITECTURAL_COMPLEX("АРХІТЕКТУРНИЙ КОМПЛЕКС"),;

    @Getter
    final String ukr;

    Type(String s) {
        this.ukr = s;
    }
}
