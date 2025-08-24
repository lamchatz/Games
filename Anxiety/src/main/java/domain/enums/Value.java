package domain.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum Value {

    ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13), FOLD(-1);
    private final int val;
    private static final Map<String, Value> VALUES = Map.ofEntries(
            Map.entry("a", Value.ACE),
            Map.entry("1", Value.ACE),
            Map.entry("2", Value.TWO),
            Map.entry("3", Value.THREE),
            Map.entry("4", Value.FOUR),
            Map.entry("5", Value.FIVE),
            Map.entry("6", Value.SIX),
            Map.entry("7", Value.SEVEN),
            Map.entry("8", Value.EIGHT),
            Map.entry("9", Value.NINE),
            Map.entry("10", Value.TEN),
            Map.entry("j", Value.JACK),
            Map.entry("q", Value.QUEEN),
            Map.entry("k", Value.KING),
            Map.entry("f", Value.FOLD)
    );


    Value(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public static Value parse(String s) {
        Value value = VALUES.get(s);

        if (value == null) {
            throw new IllegalArgumentException(String.format("Could not match %s to a Value", s));
        }

        return value;
    }

    public static List<Value> playableValues() {
        return Arrays.asList(ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING);
    }

    public static Set<Value> getFigures() {
        return Set.of(JACK, QUEEN, KING, ACE);
    }

    @Override
    public String toString() {
        switch (val) {
            case 1: return "A";
            case 11: return "J";
            case 12: return "Q";
            case 13: return "K";
            default: return String.valueOf(val);
        }
    }
}
