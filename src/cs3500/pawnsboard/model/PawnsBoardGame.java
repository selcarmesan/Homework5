package cs3500.pawnsboard.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

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
public class PawnsBoardGame implements PawnsBoard<PawnsCard, BoardCell> {

  //<editor-fold desc="Fields">
  private final int rows;
  private final int cols;
  private final Random rand;
  private boolean randomDraw;
  private boolean gameStarted;
  private boolean gameOver;
  private BoardCell[][] board;

  private List<PawnsCard> redDeck;
  private List<PawnsCard> redHand;
  private List<PawnsCard> blueDeck;
  private List<PawnsCard> blueHand;
  private Player currentTurn;
  private boolean lastPassed = false;
  //</editor-fold>

  //<editor-fold desc="Constructors">
  /**
   * Creates a new pawns board game with a random seed.
   *
   * @param rows number of rows
   * @param cols number of columns
   * @throws IllegalArgumentException if rows or columns is not positive
   *                                  if columns is not odd and greater than two
   */
  public PawnsBoardGame(int rows, int cols) {
    if (rows < 1) {
      throw new IllegalArgumentException("rows must be greater than 0");
    }
    if (cols < 3 || cols % 2 != 1) {
      throw new IllegalArgumentException("columns must be odd and greater than two");
    }
    this.rows = rows;
    this.cols = cols;
    this.rand = new Random();
  }

  /**
   * Creates a new pawns board game using the given random as a seed.
   *
   * @param rows number of rows
   * @param cols number of columns
   * @param rand random used for drawing new cards
   * @throws IllegalArgumentException if rows or columns is not positive
   *                                  if columns is not odd and greater than two
   *                                  if the given random is null
   */
  public PawnsBoardGame(int rows, int cols, Random rand) {
    if (rows < 1) {
      throw new IllegalArgumentException("rows must be greater than 0");
    }
    if (cols < 3 || cols % 2 != 1) {
      throw new IllegalArgumentException("columns must be odd and greater than two");
    }
    if (rand == null) {
      throw new IllegalArgumentException("random must not be null");
    }
    this.rows = rows;
    this.cols = cols;
    this.rand = rand;
  }
  //</editor-fold>

  //<editor-fold desc="Game Starters">

  /**
   * Initializes a new game of pawns board.  Both players are supplied their own decks containing
   * playing cards of their color, and dealt an equal amount of cards into their starting hand.
   * A new board is initialized which is empty, except for a singular red pawn in each cell in the
   * leftmost column, and a singular blue pawn in each cell in the rightmost column.  Red has the
   * first turn.
   *
   * @param redDeck    red player's deck
   * @param blueDeck   blue player's deck
   * @param handSize   the starting hand size for both players
   * @param randomDraw whether each new card is drawn randomly or from the front
   * @throws IllegalArgumentException if either decks are null or contain null
   *                                  if either deck contains cards not of the respective color
   *                                  if the hand size is less than 1
   *                                  if either deck contains fewer cards than the number of cells
   *                                  if there are more than 2 copies of the same card in one deck
   *                                  if hand size is greater than a third of either deck size
   * @throws IllegalStateException    if game is already in progress
   */
  @Override
  public void startGame(List<PawnsCard> redDeck, List<PawnsCard> blueDeck,
                        int handSize, boolean randomDraw) {
    if (gameStarted) {
      throw new IllegalStateException("Game already started");
    }
    if (redDeck == null || blueDeck == null) {
      throw new IllegalArgumentException("Decks cannot be null");
    }
    if (redDeck.stream().anyMatch(Objects::isNull) || blueDeck.stream().anyMatch(Objects::isNull)) {
      throw new IllegalArgumentException("Decks cannot contain null");
    }
    for (PawnsCard card : redDeck) {
      if (card.getColor() != Player.RED) {
        throw new IllegalArgumentException("Red decks can only contain red cards");
      }
      if (Collections.frequency(redDeck, card) > 2) {
        throw new IllegalArgumentException("Decks cannot contain more than 2 of the same card");
      }
    }
    for (PawnsCard card : blueDeck) {
      if (card.getColor() != Player.BLUE) {
        throw new IllegalArgumentException("Blue decks can only contain blue cards");
      }
      if (Collections.frequency(blueDeck, card) > 2) {
        throw new IllegalArgumentException("Decks cannot contain more than 2 of the same card");
      }
    }
    assertDeckValidity(redDeck, blueDeck, handSize);
    List<PawnsCard> newRedDeck = new ArrayList<>();
    List<PawnsCard> newBlueDeck = new ArrayList<>();
    for (PawnsCard card : redDeck) {
      newRedDeck.add(new PawnsCard(card.getName(), card.getCost(), card.getValue(),
              card.getColor(), card.getInfluence()));
    }
    for (PawnsCard card : blueDeck) {
      newBlueDeck.add(new PawnsCard(card.getName(), card.getCost(), card.getValue(),
              card.getColor(), card.getInfluence()));
    }
    this.redDeck = newRedDeck;
    this.blueDeck = newBlueDeck;
    this.randomDraw = randomDraw;
    setupGame(handSize);
  }

  /**
   * Initializes a new game of pawns board.  Both players are supplied their own decks from the
   * config file, and dealt an equal amount of cards into their starting hand.
   * A new board is initialized which is empty, except for a singular red pawn in each cell in the
   * leftmost column, and a singular blue pawn in each cell in the rightmost column.  Red has the
   * first turn.
   *
   * @param handSize   the starting hand size for both players
   * @param randomDraw whether each new card is drawn randomly or from the front
   * @throws IllegalArgumentException if there is some error with the config file
   *                                  if the hand size is less than 1
   *                                  if either deck contains fewer cards than the number of cells
   *                                  if there are more than 2 copies of the same card in one deck
   *                                  if hand size is greater than a third of either deck size
   * @throws IllegalStateException    if game is already in progress
   */
  @Override
  public void startGame(int handSize, boolean randomDraw) {
    if (gameStarted) {
      throw new IllegalStateException("Game already started");
    }
    redDeck = PawnsCardReader.readCards(Player.RED);
    blueDeck = PawnsCardReader.readCards(Player.BLUE);
    startGame(redDeck, blueDeck, handSize, randomDraw);
  }

  private void setupGame(int handSize) {
    this.redHand = new ArrayList<>();
    this.blueHand = new ArrayList<>();
    for (int i = 0; i < handSize; i++) {
      drawCard(Player.RED);
    }
    for (int i = 0; i < handSize; i++) {
      drawCard(Player.BLUE);
    }
    board = new BoardCell[rows][cols];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        board[i][j] = new BoardCell();
        if (j == 0) {
          BoardCell cell = new BoardCell();
          cell.addPawn(Player.RED);
          board[i][j] = cell;
        }
        if (j == cols - 1) {
          BoardCell cell = new BoardCell();
          cell.addPawn(Player.BLUE);
          board[i][j] = cell;
        }
      }
    }
    currentTurn = Player.RED;
    gameStarted = true;
    gameOver = false;
  }

  private void assertDeckValidity(List<PawnsCard> redDeck, List<PawnsCard> blueDeck, int handSize) {
    if (handSize < 1 || handSize > Math.max(redDeck.size(), blueDeck.size()) / 3) {
      throw new IllegalArgumentException("Hand must be positive and less than a third of the deck");
    }
    if (redDeck.size() < getNumCells() || blueDeck.size() < getNumCells()) {
      throw new IllegalArgumentException("Decks must contain enough cards to fill the board");
    }
  }
  //</editor-fold>

  //<editor-fold desc="Getters">

  /**
   * Returns the number of rows on the board.
   *
   * @return the rows
   */
  public int getRows() {
    return rows;
  }

  /**
   * Returns the number of columns on the board.
   *
   * @return the columns
   */
  public int getCols() {
    return cols;
  }

  /**
   * Returns the current score of the specified player.
   *
   * @param player the player whose score is returned
   * @return the player's score
   * @throws IllegalStateException if game is not in progress
   */
  @Override
  public int getScore(Player player) {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    int score = 0;
    for (int i = 0; i < rows; i++) {
      int tempPlayer = 0;
      int tempOpponent = 0;
      for (int j = 0; j < cols; j++) {
        Cell<PawnsCard> cell = getCellAt(i, j);
        if (cell.getOwner() == player && cell.getCard() != null) {
          tempPlayer += cell.getCard().getValue();
        } else if (cell.getOwner() != player && cell.getCard() != null) {
          tempOpponent += cell.getCard().getValue();
        }
      }
      if (tempPlayer > tempOpponent) {
        score += tempPlayer;
      }
    }
    return score;
  }

  /**
   * Returns the current hand of cards belonging to the specified player.
   *
   * @param player the player whose hand is returned
   * @return the player's hand
   * @throws IllegalStateException    if game is not in progress
   * @throws IllegalArgumentException if player is null
   */
  @Override
  public List<PawnsCard> getHand(Player player) {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    List<PawnsCard> hand = new ArrayList<>();
    if (player == Player.RED) {
      for (PawnsCard card : redHand) {
        hand.add(new PawnsCard(card.getName(), card.getCost(),
                card.getValue(), card.getColor(), card.getInfluence()));
      }
    } else {
      for (PawnsCard card : blueHand) {
        hand.add(new PawnsCard(card.getName(), card.getCost(),
                card.getValue(), card.getColor(), card.getInfluence()));
      }
    }
    return hand;
  }

  /**
   * Returns the player whose turn it is.
   *
   * @return the turn
   * @throws IllegalStateException if game is not in progress
   */
  @Override
  public Player getCurrentTurn() {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    return currentTurn;
  }

  /**
   * Returns the winner, the player with the greater score, or null if tied.
   *
   * @return the winning player
   * @throws IllegalStateException if game is not in progress
   */
  @Override
  public Player getWinner() {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (!isGameOver()) {
      throw new IllegalStateException("Can only determine winner after game");
    }
    int redScore = getScore(Player.RED);
    int blueScore = getScore(Player.BLUE);
    if (redScore > blueScore) {
      return Player.RED;
    } else if (blueScore > redScore) {
      return Player.BLUE;
    } else {
      return null;
    }
  }

  /**
   * Returns a copy of the current playing board.
   *
   * @return the board
   * @throws IllegalStateException if game is not in progress
   */
  @Override
  public BoardCell[][] getBoard() {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    BoardCell[][] copyBoard = new BoardCell[rows][cols];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        copyBoard[i][j] = getCellAt(i, j);
      }
    }
    return copyBoard;
  }

  /**
   * Returns a copy of the specified cell.
   *
   * @param row the row
   * @param col the column
   * @return the cell in question
   * @throws IllegalArgumentException if row and column pair marks an invalid space
   * @throws IllegalStateException    if game is not in progress
   */
  @Override
  public BoardCell getCellAt(int row, int col) {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (!locationValid(row, col)) {
      throw new IllegalArgumentException("Invalid location");
    }
    BoardCell cell = board[row][col];
    BoardCell copyCell = new BoardCell();
    if (cell.getCard() != null) {
      copyCell.playCard(cell.getCard());
    } else if (cell.getPawns() > 0) {
      copyCell.addPawn(cell.getOwner());
      for (int i = 0; i < cell.getPawns() - 1; i++) {
        copyCell.addPawn();
      }
    }
    return copyCell;
  }

  /**
   * Tells whether game is still in progress
   *
   * @return if the game state is over
   * @throws IllegalStateException if game is not in progress
   */
  @Override
  public boolean isGameOver() {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    return gameOver;
  }
  //</editor-fold>

  //<editor-fold desc="Turn Options">

  /**
   * Influences the given board starting at the specified row and column.  Each card reaches at
   * most 2 in any direction from the starting space, while the specifics vary by card.
   * A card can be placed if the pawns cost for it are within the desired space, and the pawns
   * belong to the player attempting to place the card.  A new card is drawn to replace it after,
   * if any remain in the player's deck.
   *
   * @param row    the starting cell's row
   * @param col    the starting cell's column
   * @param handId the index of the card in the player's hand
   * @throws IllegalArgumentException hand ID is invalid
   *                                  if desired cell already has a card
   *                                  if desired cell does not have enough owned pawns for the cost
   *                                  if desired cell has pawns that do not belong to the player
   * @throws IllegalStateException    if game is not in progress
   */
  @Override
  public void placeCard(int row, int col, int handId) {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (!locationValid(row, col)) {
      throw new IllegalArgumentException("Invalid location");
    }
    if (handId < 0 || handId >= getCurrentPlayerHand().size()) {
      throw new IllegalArgumentException("Invalid hand id");
    }
    PawnsCard card = getHand(getCurrentTurn()).get(handId);
    BoardCell cell = getCellAt(row, col);
    if (card.getCost() > cell.getPawns() ||cell.getOwner() != getCurrentTurn()) {
      throw new IllegalArgumentException("Not enough owned pawns to play card");
    }
    if (cell.getCard() != null) {
      throw new IllegalArgumentException("Can't place card in occupied cell");
    }
    board[row][col].playCard(getCurrentPlayerHand().get(handId));
    lastPassed = false;
    influenceBoard(row, col, card);
    swapTurn();
  }

  /**
   * Skips the turn of the current player, rather than placing a new card.
   *
   * @throws IllegalStateException if game is not in progress
   */
  @Override
  public void skipTurn() {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (lastPassed) {
      gameOver = true;
    }
    lastPassed = true;
    swapTurn();
  }
  //</editor-fold>

  //<editor-fold desc="Helpers">
  private boolean locationValid(int row, int col) {
    return row >= 0 && row < rows && col >= 0 && col < cols;
  }

  private int getNumCells() {
    return rows * cols;
  }

  private void drawCard(Player deck) {
    if (deck == Player.RED) {
      if (redDeck.isEmpty()) {
        return;
      }
      if (randomDraw) {
        redHand.add(redDeck.get(rand.nextInt(redDeck.size())));
      } else {
        redHand.add(redDeck.get(0));
      }
    } else {
      if (blueDeck.isEmpty()) {
        return;
      }
      if (randomDraw) {
        blueHand.add(blueDeck.get(rand.nextInt(blueDeck.size())));
      } else {
        blueHand.add(blueDeck.get(0));
      }
    }
  }

  // For getting the proper hand, not a copy unlike the public method
  private List<PawnsCard> getCurrentPlayerHand() {
    if (getCurrentTurn() == Player.RED) {
      return redHand;
    } else {
      return blueHand;
    }
  }

  private void swapTurn() {
    if (currentTurn == Player.RED) {
      currentTurn = Player.BLUE;
    } else {
      currentTurn = Player.RED;
    }
    drawCard(getCurrentTurn());
  }

  private void influenceBoard(int row, int col, PawnsCard card) {
    boolean[][] influence = card.getInfluence();
    for (int i = -2; i <= 2; i++) {
      for (int j = -2; j <= 2; j++) {
        if (locationValid(row + i, col + j)) {
          if (influence[i + 2][j + 2]) {
            influenceCell(board[row + i][col + j], card.getColor());
          }
        }
      }
    }
  }

  private void influenceCell(BoardCell cell, Player player) {
    if (cell.getCard() == null) {
      if (cell.getOwner() != player) {
        if (cell.getOwner() == null) {
          cell.addPawn(player);
        } else {
          cell.changeOwner(player);
        }
      } else {
        cell.addPawn();
      }
    }
  }
  //</editor-fold>
}
