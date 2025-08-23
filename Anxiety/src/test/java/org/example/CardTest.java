package org.example;

import domain.Card;
import domain.enums.Category;
import domain.enums.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CardTest {
    @Test
    public void testRead() {
        Assertions.assertEquals(new Card(Value.ACE, Category.CLUB), Card.from("1c"));
    }

    @Test
    public void testReadInvalidVal() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {Card.from("pC");});
    }

    @Test
    public void testReadInvalidCat() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {Card.from("1l");});
    }

    @Test
    public void testReadFold() {
        Assertions.assertEquals(new Card(Value.FOLD, Category.FOLD), Card.from("FF"));
    }
}
