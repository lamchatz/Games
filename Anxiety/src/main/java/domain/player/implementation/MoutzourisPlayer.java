package domain.player.implementation;

import domain.Card;
import domain.enums.Value;
import domain.player.declaration.Player;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * When UI is available, this class probably won't be necessary.
 * But it is simpler to remove a random card each time, rather than having the user select one.
 */
public class MoutzourisPlayer extends PickPlayer implements Player {

    private final Random random = new Random();

    public MoutzourisPlayer(String name) {
        super(name);
    }

    @Override
    public void draw(Card card) {
        Map<Value, Set<Card>> hand = getHand();
        Set<Card> cards = hand.get(card.getValue());
        int cardsSize = cards.size();

        if (cardsSize == 1) {
            cards.clear();
            hand.get(card.getValue()).clear();
            decreaseSize();
            return;
        }

        cards.add(card);
        increaseSize();
    }

    @Override
    public Card play(Card card) {
        List<Card> allCards = getHand().values().stream()
                .flatMap(Set::stream)
                .toList();

        if (allCards.isEmpty()) {
            throw new IllegalStateException("Hand is empty!");
        }

        decreaseSize();
        return allCards.get(random.nextInt(allCards.size()));
    }
}
