package domain.player;

import domain.Card;
import domain.enums.Category;
import domain.enums.Value;

import java.util.Collection;
import java.util.Set;

public class GatherPlayer extends DropPlayer implements ScoringPlayer {
    private static final Set<Value> FIGURES = Value.getFigures();
    private static final Set<Card> EXTRA = Set.of(new Card(Value.TEN, Category.DIAMOND), new Card(Value.TWO, Category.SPADES));

    private int score;

    public GatherPlayer(String name) {
        super(name);
        score = 0;
    }

    @Override
    public void gather(Collection<Card> cards) {
        int i = 0;
        for (Card card : cards) {
            if (FIGURES.contains(card.getValue())) {
                score++;
                i++;
            }
            if (EXTRA.contains(card)) {
                i++;
                score++;
            }
        }

        System.out.println(i + " points gathered");
    }

    @Override
    public void jackPot(Card card) {
        System.out.println("JACKPOT");
        if (card.hasValue(Value.JACK)) {
            score += 20;
        } else {
            score += 10;
        }
    }

    @Override
    public int totalScore() {
        return score;
    }

    @Override
    public void print() {
        super.print();
        System.out.println();
    }
}
