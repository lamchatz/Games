package org.example;

import java.util.Arrays;
import java.util.List;

public enum Category {
    HEART(Colour.RED), DIAMOND(Colour.RED), CLUB(Colour.BLACK), SPADES(Colour.BLACK), FOLD(Colour.BLACK);

    private final Colour colour;

    Category(Colour colour) {
        this.colour = colour;
    }

    public Colour getColour() {
        return colour;
    }

    public static Category parse(char c) {
        switch (c) {
            case 'h':
                return HEART;
            case 'd':
                return DIAMOND;
            case 'c':
                return CLUB;
            case 's':
                return SPADES;
            case 'f':
                return FOLD;
            default:
                throw new IllegalArgumentException(String.format("Could not match: %s to a Category", c));
        }
    }

    public static List<Category> playableValues() {
        return Arrays.asList(HEART, DIAMOND, SPADES, CLUB);
    }


    @Override
    public String toString() {
        return String.valueOf(name().charAt(0));
    }
}
