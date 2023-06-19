package com.einoorish.exhibitionsbackend.model.exhibit;

import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;

public enum ExhibitType {
    TEXTILE("ОДЯГ"),
    ARTIFACT("АРТЕФАКТ"),
    DOCUMENTS_AND_LITERATURE("ДОКУМЕНТИ ТА ЛІТЕРАТУРА"),
    PAINTING_AND_MOSAIC("КАРТИНИ ТА МОЗАЇКА"),
    MONUMENTAL_ART("МОНУМЕНТАЛЬНЕ МИСТЕЦТВО"),
    ARCHITECTURE("АРХІТЕКТУРА"),
    LANDSCAPE("ЛАНДШАФТ"),
    INVENTION("ВИНАХІД");


    @Getter
    final String ukr;

    ExhibitType(String s) {
        this.ukr = s;
    }



    public ImmutablePair<String, String> makePair(){
        return new ImmutablePair<>(this.name(), this.getUkr());
    }
}