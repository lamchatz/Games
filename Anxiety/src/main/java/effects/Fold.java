package effects;

import domain.Card;
import main.Anxiety;

public class Fold implements Effect{

    @Override
    public void apply(Anxiety game, Card card) {
        game.fold();
    }
}
