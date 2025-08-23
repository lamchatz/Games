package util;

import domain.Card;
import domain.enums.Category;
import domain.enums.Value;
import effects.Effect;

import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Reader {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String PLEASE_SELECT_A_CARD = "Please select a card: ";
    private static final Set<String> ACCEPTABLE_DRAW_TWO_ANSWERS = Set.of("play", "draw");
    private static final Set<String> YES_NO = Set.of("yes", "no", "y", "n");

    public static Card readCard() {
        System.out.println(PLEASE_SELECT_A_CARD);
        String input = scanner.nextLine();

        int length = input.length();

        while (length < 2 || length > 3) {
            System.out.println("Please type Number and First letter of Category, ie 8A");
            input = scanner.nextLine();
            length = input.length();
        }

        return Card.from(input);
    }

    public static Category readCategory() {
        System.out.printf("Change to %s. Type the first letter%n", Category.playableValues());
        String input = scanner.nextLine();

        while (input.length() != 1) {
            System.out.println("Please type the first letter only.");
            input = scanner.nextLine();
        }

        return Category.parse(input.charAt(0));
    }

    public static boolean readDrawTwo() {
        String input;
        do {
            System.out.println("Do you want to draw two or play your draw card? [Play/Draw]");
            input = scanner.nextLine();
        } while (!ACCEPTABLE_DRAW_TWO_ANSWERS.contains(input.toLowerCase()));


        return input.equalsIgnoreCase("play");
    }

    public static boolean confirm(Effect effect) {
        String input;
        do {
            System.out.printf("Do you want to add the effect: %s for a second time? [Yes/No]\n", theNameOf(effect));
            input = scanner.nextLine();
        } while (!YES_NO.contains(input.toLowerCase()));

        return input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y");
    }

    public static boolean overrideEffect(Value value, Effect effect) {
        String input;
        do {
            System.out.printf("Do you want to override the current effect of %s - %s [Yes/No]\n", value, theNameOf(effect));
            input = scanner.nextLine();
        } while (!YES_NO.contains(input.toLowerCase()));

        return input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y");
    }

    private static String theNameOf(Effect effect) {
        return effect.getClass().getSimpleName();
    }

    public static int askPlayer(int maxPlayers, int currentPlayer) {
        List<Integer> range = range(maxPlayers, currentPlayer);

        int input;
        do {
            System.out.printf("Select which player to ask %s\n", range);
            input = Integer.parseInt(scanner.nextLine());
        } while (!range.contains(input));

        return input;
    }

    private static List<Integer> range(int maxPlayers, int currentPlayer) {
        return IntStream.range(0, maxPlayers)
                .filter(i -> i != currentPlayer)
                .boxed()
                .collect(Collectors.toList());
    }
}
