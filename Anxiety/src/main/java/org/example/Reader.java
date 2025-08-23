package org.example;

import java.util.Scanner;
import java.util.Set;

public class Reader {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String PLEASE_SELECT_A_CARD = "Please select a card: ";
    private static final Set<String> ACCEPTABLE_DRAW_TWO_ANSWERS = Set.of("play", "draw");

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
        System.out.println(String.format("Change to %s. Type the first letter", Category.playableValues()));
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
            input =  scanner.nextLine();
        } while (!ACCEPTABLE_DRAW_TWO_ANSWERS.contains(input.toLowerCase()));


        return input.equalsIgnoreCase("play");
    }
}
