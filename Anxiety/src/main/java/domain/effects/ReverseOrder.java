package domain.effects;

import Anxiety;
import domain.Card;

public class ReverseOrder implements Effect{
    @Override
    public void apply(Anxiety game, Card card) {
        game.play(card);
        game.reverseOrder();
    }
}
