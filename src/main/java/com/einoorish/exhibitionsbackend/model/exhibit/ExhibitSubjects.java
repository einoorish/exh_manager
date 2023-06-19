package com.einoorish.exhibitionsbackend.model.exhibit;

import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;

public enum ExhibitSubjects {
    SCIENCE_AND_TECHNOLOGY("НАУКА ТА ТЕХНОЛОГІЇ"),
    NATURE("ПРИРОДА"),
    EVERYDAY_LIFE("ПОБУТ"),
    LITERATURE("ЛІТЕРАТУРА"),
    HUMAN_RIGHTS("ПРАВА ЛЮДИНИ"),
    ENTERTAINMENT("РОЗВАГИ"),
    ASTRONOMY_AND_SPACE("АСТРОНОМІЯ ТА КОСМОС"),
    RELIGION("РЕЛІГІЯ"),
    MYTHOLOGY("МІТОЛОГІЯ"),
    WAR("ВІЙНА"),
    ART("МИСТЕЦТВО"),
    SPORT("СПОРТ"),
    MEDICINE("МЕДИЦИНА"),
    FASHION("МОДА"),
    OTHER("ІНШЕ");

    @Getter
    final String ukr;

    ExhibitSubjects(String s) {
        this.ukr = s;
    }

    public ImmutablePair<String, String> makePair(){
        System.out.println(new ImmutablePair<>(this.name(), this.getUkr()));
        return new ImmutablePair<>(this.name(), this.getUkr());
    }
}
