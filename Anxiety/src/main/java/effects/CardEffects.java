package effects;

import domain.Card;
import domain.enums.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CardEffects {
    private final Map<Value, Effect> effects;
    private static final Effect ADVANCE = new Advance();

    public CardEffects() {
        this.effects = new HashMap<>();
    }

    public void add(Value value, Effect effect) {
        this.effects.put(value, effect);
    }

    private Effect get(Value value) {
        return effects.getOrDefault(value, ADVANCE);
    }

    public Effect get(Card card) {
        return get(card.getValue());
    }

    public Optional<Value> getValueWith(Effect effect) {
        for (Map.Entry<Value, Effect> entry : effects.entrySet()) {
            if (effect.equals(entry.getValue())) {
                return Optional.ofNullable(entry.getKey());
            }
        }

        return Optional.empty();
    }
}
