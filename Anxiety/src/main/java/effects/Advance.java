package effects;

import Anxiety;
import domain.Card;

public class Advance implements Effect{
    @Override
    public void apply(Anxiety game, Card card) {
        Effect.super.apply(game, card);
    }
}
