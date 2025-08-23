package domain;

import domain.enums.Category;
import domain.enums.Value;

public class Card implements Comparable<Card> {
    private final Value value;
    private final Category category;

    public Card(Value value, Category category) {
        this.value = value;
        this.category = category;
    }

    public static Card from(String input) {
        String lowerCase = input.toLowerCase();

        int last = lowerCase.length() - 1;
        return new Card(Value.parse(lowerCase.substring(0, last)), Category.parse(lowerCase.charAt(last)));
    }

    public Value getValue() {
        return value;
    }

    public Category getCategory() {
        return category;
    }

    public boolean sameTo(Card card) {
        if (this == card) return true;

        if (card == null) {
            return false;
        }

        return this.getValue().equals(card.getValue());
    }

    public boolean hasValue(Value value) {
        return value.equals(getValue());
    }

    public String print() {
        return getValue().toString() + getCategory();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        if (value != card.value) return false;
        return category == card.category;
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + category.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "{" + "value=" + value +
                ", category=" + category +
                '}';
    }

    @Override
    public int compareTo(Card other) {
        int catComp = this.getCategory().compareTo(other.getCategory());

        if (catComp != 0) {
            return catComp;
        }

        int thisVal = this.getValue().getVal();
        int otherVal = other.getValue().getVal();

        return Integer.compare(thisVal, otherVal);
    }
}
