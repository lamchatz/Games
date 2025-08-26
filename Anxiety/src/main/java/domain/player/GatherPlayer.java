package domain.player;

import domain.Card;
import domain.enums.Category;
import domain.enums.Value;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

public class GatherPlayer extends DropPlayer implements ScoringPlayer {
    private static final Comparator<Card> VALUE_COMPARATOR =
            Comparator.comparing(Card::getValue)
                    .thenComparing(Card::getCategory);
    private static final Set<Value> FIGURES = Value.getFigures();
    private static final Set<Card> EXTRA = Set.of(new Card(Value.TEN, Category.DIAMOND), new Card(Value.TWO, Category.SPADES));

    private int score;

    private int cardsGathered;
    public GatherPlayer(String name) {
        super(name, VALUE_COMPARATOR);
        score = 0;
        cardsGathered = 0;
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

        cardsGathered += cards.size();
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

        cardsGathered += 2;
    }

    @Override
    public int totalScore() {
        return score;
    }

    @Override
    public int totalCardsGathered() {
        return cardsGathered;
    }

    @Override
    public void addMostCardsGatheredPoints() {
        score += 3;
    }

    @Override
    public void print() {
        super.print();
        System.out.println();
    }
}
