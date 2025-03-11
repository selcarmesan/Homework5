package cs3500.pawnsboard.model;

import java.util.List;

/**
 * A game of pawns board.  The game begins with an empty board of specified dimensions, with the
 * restrictions being that the board is rectangular, with a positive number of rows, and an odd
 * number of columns greater than 1.
 * Gameplay begins by dealing each of the two players a hand and allowing them to take turns
 * placing one card at a time or skipping their turn until the board is full or no player can go.
 * Score is row individual.  The one with more value based on their placed cards per row is the
 * winner of that row, and receives their total value, while the opponent receives nothing.  The
 * player with the most points from the rows they claimed is the victor at the end, or it ends in a
 * draw.
 */
public interface PawnsBoard<C extends Card, B extends Cell> {

  /**
   * Initializes a new game of pawns board.  Both players are supplied their own decks containing
   * playing cards of their color, and dealt an equal amount of cards into their starting hand.
   * A new board is initialized which is empty, except for a singular red pawn in each cell in the
   * leftmost column, and a singular blue pawn in each cell in the rightmost column.  Red has the
   * first turn.
   *
   * @param redDeck red player's deck
   * @param blueDeck blue player's deck
   * @param handSize the starting hand size for both players
   * @param randomDraw whether each new card is drawn randomly or from the front
   * @throws IllegalArgumentException if either decks are null or contain null
   *                                  if either deck contains cards not of the respective color
   *                                  if the hand size is less than 1
   *                                  if either deck contains fewer cards than the number of cells
   *                                  if there are more than 2 copies of the same card in one deck
   *                                  if hand size is greater than a third of either deck size
   * @throws IllegalStateException if game is already in progress
   */
  void startGame(List<C> redDeck, List<C> blueDeck, int handSize, boolean randomDraw);

  /**
   * Initializes a new game of pawns board.  Both players are supplied their own decks from the
   * config file, and dealt an equal amount of cards into their starting hand.
   * A new board is initialized which is empty, except for a singular red pawn in each cell in the
   * leftmost column, and a singular blue pawn in each cell in the rightmost column.  Red has the
   * first turn.
   *
   * @param handSize the starting hand size for both players
   * @param randomDraw whether each new card is drawn randomly or from the front
   * @throws IllegalArgumentException if there is some error with the config file
   *                                  if the hand size is less than 1
   *                                  if either deck contains fewer cards than the number of cells
   *                                  if there are more than 2 copies of the same card in one deck
   *                                  if hand size is greater than a third of either deck size
   * @throws IllegalStateException if game is already in progress
   */
  void startGame(int handSize, boolean randomDraw);

  /**
   * Returns the number of rows on the board.
   * @return the rows
   */
  int getRows();

  /**
   * Returns the number of columns on the board.
   * @return the columns
   */
  int getCols();

  /**
   * Returns the current score of the specified player.
   *
   * @param player the player whose score is returned
   * @return the player's score
   * @throws IllegalStateException if game is not in progress
   */
  int getScore(Player player);

  /**
   * Returns the current hand of cards belonging to the specified player.
   *
   * @param player the player whose hand is returned
   * @return the player's hand
   * @throws IllegalStateException if game is not in progress
   * @throws IllegalArgumentException if player is null
   */
  List<C> getHand(Player player);

  /**
   * Influences the given board starting at the specified row and column.  Each card reaches at
   * most 2 in any direction from the starting space, while the specifics vary by card.
   * A card can be placed if the pawns cost for it are within the desired space, and the pawns
   * belong to the player attempting to place the card.  A new card is drawn to replace it after,
   * if any remain in the player's deck.
   *
   * @param row the starting cell's row
   * @param col the starting cell's column
   * @param handId the index of the card in the player's hand
   * @throws IllegalArgumentException if hand ID is invalid
   *                                  if desired cell already has a card
   *                                  if desired cell does not have enough owned pawns for the cost
   *                                  if desired cell has pawns that do not belong to the player
   * @throws IllegalStateException if game is not in progress
   */
  void placeCard(int row, int col, int handId);

  /**
   * Skips the turn of the current player, rather than placing a new card.
   *
   * @throws IllegalStateException if game is not in progress
   */
  void skipTurn();

  /**
   * Returns the player whose turn it is.
   *
   * @return the turn
   * @throws IllegalStateException if game is not in progress
   */
  Player getCurrentTurn();

  /**
   * Tells whether game is still in progress
   *
   * @return if the game state is over
   * @throws IllegalStateException if game is not in progress
   */
  boolean isGameOver();

  /**
   * Returns the winner, the player with the greater score, or null if tied.
   *
   * @return the winning player
   * @throws IllegalStateException if game is not in progress
   *                               if game is not over
   */
  Player getWinner();

  /**
   * Returns a copy of the current playing board.
   *
   * @return the board
   * @throws IllegalStateException if game is not in progress
   */
  B[][] getBoard();

  /**
   * Returns a copy of the specified cell.
   *
   * @param row the row
   * @param col the column
   * @return the cell in question
   * @throws IllegalArgumentException if row and column pair marks an invalid space
   * @throws IllegalStateException if game is not in progress
   */
  B getCellAt(int row, int col);
}
