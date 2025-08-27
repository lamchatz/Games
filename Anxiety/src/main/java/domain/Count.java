package domain;

public class Count {

    private int counter = 0;
    private final int players;

    public Count(final int players) {
        this.players = players;
    }

    public void next() {
        counter = (counter + 1) % players;
    }

    public int current() {
        return counter;
    }

    public int previous() {
        return (counter - 1 + players) % players;
    }
}
