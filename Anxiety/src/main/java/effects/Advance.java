package effects;

import domain.Card;
import main.Anxiety;

public class Advance implements Effect{
    @Override
    public void apply(Anxiety game, Card card) {
        Effect.super.apply(game, card);
    }
}
