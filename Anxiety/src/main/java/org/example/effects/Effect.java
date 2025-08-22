package org.example.effects;

import org.example.Anxiety;
import org.example.Card;

public interface Effect {
    default void apply(Anxiety game, Card card) {
        game.play(card);
        game.advanceToNextPlayer();
    }
}
