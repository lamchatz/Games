package effects;

import domain.Card;
import main.Anxiety;

public class Advance implements Effect {
    private final String NAME = "Advance";
    @Override
    public void apply(Anxiety game, Card card) {
        Effect.super.apply(game, card);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Advance advance = (Advance) o;

        return NAME.equals(advance.NAME);
    }

    @Override
    public int hashCode() {
        return NAME.hashCode();
    }
}
