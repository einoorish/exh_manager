package com.einoorish.exhibitionsbackend.model.exhibit;

import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;

public enum Epoch {
    PREHISTORICAL("ДОІСТОРИЧНА"),
    ANCIENT("ДРЕВНЯ"),
    MIDDLE_AGES("СЕРЕДНЬОВІЧНА"),
    EARLY_MODERN("РАННІЙ НОВИЙ ЧАС"),
    MODERN("НОВИЙ ЧАС"),
    VERY_MODERN("СУЧАСНА");

    @Getter
    final String ukr;

    Epoch(String s) {
        this.ukr = s;
    }

    public ImmutablePair<String, String> makePair(){
        return new ImmutablePair<>(this.name(), this.getUkr());
    }
}
