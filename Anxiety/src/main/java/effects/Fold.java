package effects;

import Anxiety;
import domain.Card;

public class Fold implements Effect{

    @Override
    public void apply(Anxiety game, Card card) {
        game.fold();
    }
}
