package effects;

import domain.Card;
import main.Anxiety;

public class ChangeCategory implements Effect{
    @Override
    public void apply(Anxiety game, Card card) {
        game.changeCategory(card);
    }
}
