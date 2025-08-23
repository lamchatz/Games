package effects;

import Anxiety;
import domain.Card;

public interface Effect {
    default void apply(Anxiety game, Card card) {
        game.play(card);
        game.advanceToNextPlayer();
    }
}
