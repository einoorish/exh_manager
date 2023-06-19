package com.einoorish.exhibitionsbackend.model.exhibit;

import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;

public enum MediaType {
    PHOTO("ФОТО"),
    VIDEO("ВІДЕО"),
    AUDIO("АУДІО"),
    _3D("3Д МОДЕЛЬ");


    @Getter
    final String ukr;

    MediaType(String s) {
        this.ukr = s;
    }

    public ImmutablePair<String, String> makePair(){
        return new ImmutablePair<>(this.name(), this.getUkr());
    }
}
