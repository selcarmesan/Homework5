package cs3500.pawnsboard.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Reads from a config file, and processes its contents into a list of cards based on a
 * particular format.  A card is formatted as 6 separate lines in a text file.  The first
 * specifies name, cost, and value.  The next five each consist of five symbols to denote affected
 * squares with an "I", unaffected with an "X", and the card itself as "C", which must always be
 * in the center of the influence radius.
 */
public class PawnsCardReader {

  /**
   * Returns a deck of cards for the corresponding player, read from the config file (deck.config)
   * The deck for the blue player has its area of influence mirrored horizontally, but contains the
   * same deck otherwise.
   *
   * @param playerDeck the player for which to make the deck
   * @return the constructed deck
   * @throws IllegalStateException if some error reading file or format is incorrect.
   * @throws IllegalArgumentException if player deck is null
   */
  public static List<PawnsCard> readCards(Player playerDeck) {
    if (playerDeck == null) {
      throw new IllegalArgumentException("Cannot construct deck for null player");
    }
    try {
      File file = new File("docs" + File.separator + "deck.config");
      Scanner scanner = new Scanner(file);
      ArrayList<PawnsCard> cards = new ArrayList<>();
      while (scanner.hasNextLine()) {
        String name = scanner.next();
        int cost = scanner.nextInt();
        int value = scanner.nextInt();
        scanner.nextLine();
        boolean[][] influence = new boolean[5][5];
        for (int i = 0; i < 5; i++) {
          String line = scanner.nextLine();
          for (int j = 0; j < 5; j++) {
            int j2 = j;
            if (playerDeck == Player.BLUE) {
              j2 = 4 - j;
            }
            influence[i][j] = line.charAt(j2) == 'I' || line.charAt(j2) == 'C';
            if (i == 2 && line.charAt(j2) != 'C' && j2 == 2) {
              throw new IllegalStateException("Config file formatted incorrectly");
            }
          }
        }
        cards.add(new PawnsCard(name, cost, value, playerDeck, influence));
      }
      return cards;
    } catch (IOException | NoSuchElementException e) {
      throw new IllegalStateException("Error reading config file, fix format and try again");
    }
  }
}
