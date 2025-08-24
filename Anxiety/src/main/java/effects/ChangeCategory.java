package effects;

import domain.Card;
import games.Anxiety;

public class ChangeCategory implements Effect{
    @Override
    public void apply(Anxiety game, Card card) {
        game.changeCategory(card);
    }
}
