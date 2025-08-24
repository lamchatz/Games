package effects;

import domain.Card;
import games.Anxiety;

public class Fold implements Effect{

    @Override
    public void apply(Anxiety game, Card card) {
        game.fold();
    }
}
