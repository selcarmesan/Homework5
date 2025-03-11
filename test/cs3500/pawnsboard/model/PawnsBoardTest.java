package cs3500.pawnsboard.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class PawnsBoardTest {

  PawnsBoard<PawnsCard, BoardCell> board;
  List<PawnsCard> redCards;
  List<PawnsCard> blueCards;

  @Before
  public void setUp() {
    board = new PawnsBoardGame(2, 3);
    boolean[][] testGrid = new boolean[5][5];
    testGrid[2][2] = true;
    PawnsCard test1Red = new PawnsCard("test1", 1, 1, Player.RED, testGrid);
    PawnsCard test1Blue = new PawnsCard("test1", 1, 1, Player.BLUE, testGrid);
    PawnsCard test2Red = new PawnsCard("test2", 1, 1, Player.RED, testGrid);
    PawnsCard test2Blue = new PawnsCard("test2", 1, 1, Player.BLUE, testGrid);
    PawnsCard test3Red = new PawnsCard("test3", 1, 1, Player.RED, testGrid);
    PawnsCard test3Blue = new PawnsCard("test3", 1, 1, Player.BLUE, testGrid);
    redCards = new ArrayList<>();
    redCards.add(test1Red);
    redCards.add(test1Red);
    redCards.add(test2Red);
    redCards.add(test2Red);
    redCards.add(test3Red);
    redCards.add(test3Red);
    blueCards = new ArrayList<>();
    blueCards.add(test1Blue);
    blueCards.add(test1Blue);
    blueCards.add(test2Blue);
    blueCards.add(test2Blue);
    blueCards.add(test3Blue);
    blueCards.add(test3Blue);
  }

  @Test
  public void testConstructorThrowsInvalidRowsOrCols() {
    assertThrows(IllegalArgumentException.class,
        () -> new PawnsBoardGame(0, 5));
    assertThrows(IllegalArgumentException.class,
        () -> new PawnsBoardGame(3, 0));
    assertThrows(IllegalArgumentException.class,
        () -> new PawnsBoardGame(3, 2));
    assertThrows(IllegalArgumentException.class,
        () -> new PawnsBoardGame(3, 4));
    assertThrows(IllegalArgumentException.class,
        () -> new PawnsBoardGame(0, 5, new Random()));
    assertThrows(IllegalArgumentException.class,
        () -> new PawnsBoardGame(3, 0, new Random()));
    assertThrows(IllegalArgumentException.class,
        () -> new PawnsBoardGame(3, 2, new Random()));
    assertThrows(IllegalArgumentException.class,
        () -> new PawnsBoardGame(3, 4, new Random()));
  }

  @Test
  public void testConstructThrowsNullRandom() {
    assertThrows(IllegalArgumentException.class,
        () -> new PawnsBoardGame(3, 5, null));
  }

  @Test
  public void testStartGameSetsUpBoard() {
    board.startGame(redCards, blueCards, 1, false);
    assertEquals(Player.RED, board.getCurrentTurn());
    for (int i = 0; i < board.getRows(); i++) {
      assertEquals(1, board.getCellAt(i, 0).getPawns());
      assertEquals(Player.RED, board.getCellAt(i, 0).getOwner());
      assertEquals(1, board.getCellAt(i, board.getCols() - 1).getPawns());
      assertEquals(Player.BLUE, board.getCellAt(i, board.getCols() - 1).getOwner());
    }
  }

  @Test
  public void testStartGameCardReaderWorks() {
    board = new PawnsBoardGame(1, 3);
    board.startGame(1, false);
    assertEquals(Player.RED, board.getCurrentTurn());
    for (int i = 0; i < board.getRows(); i++) {
      assertEquals(1, board.getCellAt(i, 0).getPawns());
      assertEquals(Player.RED, board.getCellAt(i, 0).getOwner());
      assertEquals(1, board.getCellAt(i, board.getCols() - 1).getPawns());
      assertEquals(Player.BLUE, board.getCellAt(i, board.getCols() - 1).getOwner());
    }
  }

  @Test
  public void testStartGameThrowsWithNullDeck() {
    assertThrows(IllegalArgumentException.class,
        () -> board.startGame(null, blueCards, 1,  false));
    assertThrows(IllegalArgumentException.class,
            () -> board.startGame(redCards, null, 1,  false));
  }

  @Test
  public void testStartGameThrowsWithDeckContainingNull() {
    blueCards.add(0, null);
    assertThrows(IllegalArgumentException.class,
        () -> board.startGame(redCards, blueCards, 1, false));
    blueCards.remove(0);
    redCards.add(0, null);
    assertThrows(IllegalArgumentException.class,
            () -> board.startGame(redCards, blueCards, 1, false));
  }

  @Test
  public void testStartGameThrowsWithDeckWithInvalidCardColors() {
    assertThrows(IllegalArgumentException.class,
        () -> board.startGame(blueCards, blueCards, 1, false));
    assertThrows(IllegalArgumentException.class,
        () -> board.startGame(redCards, redCards, 1, false));
  }

  @Test
  public void testStartGameThrowsInvalidHandSize() {
    assertThrows(IllegalArgumentException.class,
        () -> board.startGame(redCards, blueCards, 0, false));
    assertThrows(IllegalArgumentException.class,
        () -> board.startGame(redCards, blueCards, 4, false));
  }

  @Test
  public void testStartGameThrowsIfNotEnoughCardsForBoard() {
    List<PawnsCard> smallRedCards = new ArrayList<>();
    smallRedCards.add(PawnsCardReader.readCards(Player.RED).get(0));
    List<PawnsCard> smallBlueCards = new ArrayList<>();
    smallBlueCards.add(PawnsCardReader.readCards(Player.BLUE).get(0));
    assertThrows(IllegalArgumentException.class,
        () -> board.startGame(smallRedCards, blueCards, 1, false));
    assertThrows(IllegalArgumentException.class,
        () -> board.startGame(redCards, smallBlueCards, 1, false));
  }

  @Test
  public void testStartGameThrowsIfMoreThanTwoOfSameCard() {
    List<PawnsCard> ogRed = new ArrayList<>(redCards);
    redCards.addAll(redCards);
    assertThrows(IllegalArgumentException.class,
        () -> board.startGame(redCards, blueCards, 1, false));
    blueCards.addAll(blueCards);
    assertThrows(IllegalArgumentException.class,
        () -> board.startGame(ogRed, blueCards, 1, false));
  }

  @Test
  public void testStartGameThrowsWhenGameInProgress() {
    board.startGame(redCards, blueCards, 1, false);
    assertThrows(IllegalStateException.class,
        () -> board.startGame(1, false));
    assertThrows(IllegalStateException.class,
        () -> board.startGame(redCards, blueCards, 1, false));
  }

  @Test
  public void testGetRowsAndCols() {
    assertEquals(2, board.getRows());
    assertEquals(3, board.getCols());
  }

  @Test
  public void testGetScore() {
    board.startGame(redCards, blueCards, 1, false);
    assertEquals(0, board.getScore(Player.RED));
    assertEquals(0, board.getScore(Player.BLUE));
    int redScore = board.getHand(Player.RED).get(0).getValue();
    int blueScore = board.getHand(Player.BLUE).get(0).getValue();
    board.placeCard(0, 0, 0);
    board.placeCard(1, 2, 0);
    assertEquals(redScore, board.getScore(Player.RED));
    assertEquals(blueScore, board.getScore(Player.BLUE));
  }

  @Test
  public void testGetScoreCancelsOtherPlayerWithinRow() {
    redCards.add(0, new PawnsCard("test", 1, 3,
            Player.RED, new boolean[5][5]));
    board.startGame(redCards, blueCards, 2, false);
    assertEquals(0, board.getScore(Player.RED));
    assertEquals(0, board.getScore(Player.BLUE));
    int redScore = board.getHand(Player.RED).get(0).getValue();
    board.placeCard(0, 0, 0);
    board.placeCard(0, 2, 0);
    assertEquals(redScore, board.getScore(Player.RED));
    assertEquals(0, board.getScore(Player.BLUE));
  }

  @Test
  public void testGetScoreThrowsGameNotStarted() {
    assertThrows(IllegalStateException.class,
        () -> board.getScore(Player.RED));
  }

  @Test
  public void testGetHand() {
    board.startGame(redCards, blueCards, 1, false);
    List<PawnsCard> redHand = redCards.subList(0, 1);
    List<PawnsCard> blueHand = blueCards.subList(0, 1);
    assertEquals(redHand, board.getHand(Player.RED));
    assertEquals(blueHand, board.getHand(Player.BLUE));
  }

  @Test
  public void testGetHandThrowsGameNotStarted() {
    assertThrows(IllegalStateException.class,
        () -> board.getHand(Player.RED));
  }

  @Test
  public void testGetHandThrowsIfNullPlayer() {
    board.startGame(redCards, blueCards, 1, false);
    assertThrows(IllegalArgumentException.class, () -> board.getHand(null));
  }

  @Test
  public void testGetHandReturnsCopyOfHand() {
    board.startGame(redCards, blueCards, 1, false);
    List<PawnsCard> redHand = board.getHand(Player.RED);
    redHand.remove(0);
    assertEquals(redCards.get(0), board.getHand(Player.RED).get(0));
    List<PawnsCard> blueHand = board.getHand(Player.BLUE);
    blueHand.remove(0);
    assertEquals(blueCards.get(0), board.getHand(Player.BLUE).get(0));
  }

  @Test
  public void testPlaceCard() {
    board.startGame(redCards, blueCards, 1, false);
    PawnsCard firstCard = redCards.get(0);
    PawnsCard secondCard = blueCards.get(0);
    board.placeCard(0, 0, 0);
    assertEquals(firstCard, board.getCellAt(0, 0).getCard());
    board.placeCard(0, 2, 0);
    assertEquals(secondCard, board.getCellAt(0, 2).getCard());
  }

  @Test
  public void testPlaceCardInfluenceClaimPawnFromEmpty() {
    boolean[][] influence = new boolean[5][5];
    influence[2][2] = true;
    influence[2][3] = true;
    redCards.add(0, new PawnsCard("test", 1, 1, Player.RED, influence));
    board.startGame(redCards, blueCards, 1, false);
    assertEquals(0, board.getCellAt(0, 1).getPawns());
    assertEquals(null, board.getCellAt(0, 1).getOwner());
    board.placeCard(0, 0, 0);
    assertEquals(1, board.getCellAt(0, 1).getPawns());
    assertEquals(Player.RED, board.getCellAt(0, 1).getOwner());
  }

  @Test
  public void testPlaceCardInfluenceAddPawnToOwned() {
    boolean[][] influence = new boolean[5][5];
    influence[2][2] = true;
    influence[3][2] = true;
    redCards.add(0, new PawnsCard("test", 1, 1, Player.RED, influence));
    board.startGame(redCards, blueCards, 1, false);
    assertEquals(1, board.getCellAt(1, 0).getPawns());
    assertEquals(Player.RED, board.getCellAt(1, 0).getOwner());
    board.placeCard(0, 0, 0);
    assertEquals(2, board.getCellAt(1, 0).getPawns());
    assertEquals(Player.RED, board.getCellAt(1, 0).getOwner());
  }

  @Test
  public void testPlaceCardInfluenceStealsPawnsFromOpponent() {
    boolean[][] influence = new boolean[5][5];
    influence[2][2] = true;
    influence[2][4] = true;
    redCards.add(0, new PawnsCard("test", 1, 1, Player.RED, influence));
    board.startGame(redCards, blueCards, 1, false);
    assertEquals(1, board.getCellAt(0, 2).getPawns());
    assertEquals(Player.BLUE, board.getCellAt(0, 2).getOwner());
    board.placeCard(0, 0, 0);
    assertEquals(1, board.getCellAt(0, 2).getPawns());
    assertEquals(Player.RED, board.getCellAt(0, 2).getOwner());
  }

  @Test
  public void testPlaceCardInfluenceDoesNotAffectCellWithCard() {
    boolean[][] influence = new boolean[5][5];
    influence[2][2] = true;
    influence[2][4] = true;
    redCards.add(0, new PawnsCard("test", 1, 1, Player.RED, influence));
    board.startGame(redCards, blueCards, 1, false);
    board.skipTurn();
    board.placeCard(0, 2, 0);
    PawnsCard card = blueCards.get(0);
    assertEquals(Player.BLUE, board.getCellAt(0, 2).getOwner());
    assertEquals(card, board.getCellAt(0, 2).getCard());
    board.placeCard(0, 0, 0);
    assertEquals(Player.BLUE, board.getCellAt(0, 2).getOwner());
    assertEquals(card, board.getCellAt(0, 2).getCard());
  }

  @Test
  public void testCardIsRemovedWhenPlayed() {
    board.startGame(redCards, blueCards, 1, false);
    PawnsCard secondCard = redCards.get(1);
    board.placeCard(0, 0, 0);
    assertEquals(secondCard, board.getHand(Player.RED).get(0));
  }

  @Test
  public void testRandomDrawWorks() {
    board = new PawnsBoardGame(2, 3, new Random(1));
    board.startGame(redCards, blueCards, 1, true);
    PawnsCard firstCard = redCards.get((new Random(1).nextInt(redCards.size())));
    board.placeCard(0, 0, 0);
    assertEquals(firstCard, board.getCellAt(0, 0).getCard());
  }

  @Test
  public void testPlaceCardOnlyWorksWithEnoughPawns() {
    board.startGame(redCards, blueCards, 1, false);
    assertThrows(IllegalArgumentException.class, () -> board.placeCard(0, 1, 0));
  }

  @Test
  public void testPlaceCardOnlyWorksIfPawnsAreOwnedByPlayer() {
    board.startGame(redCards, blueCards, 1, false);
    assertThrows(IllegalArgumentException.class, () -> board.placeCard(0, 2, 0));
  }

  @Test
  public void testPlaceCardThrowsGameNotStarted() {
    assertThrows(IllegalStateException.class, () -> board.placeCard(0, 0, 0));
  }

  @Test
  public void testPlaceCardThrowsInvalidHandId() {
    board.startGame(redCards, blueCards, 1, false);
    assertThrows(IllegalArgumentException.class, () -> board.placeCard(0, 0, -1));
    assertThrows(IllegalArgumentException.class, () -> board.placeCard(0, 0, 2));
  }

  @Test
  public void testPlaceCardThrowsInvalidLocation() {
    board.startGame(redCards, blueCards, 1, false);
    assertThrows(IllegalArgumentException.class, () -> board.placeCard(0, -1, 0));
    assertThrows(IllegalArgumentException.class, () -> board.placeCard(-1, 0, 0));
    assertThrows(IllegalArgumentException.class, () -> board.placeCard(2, 0, 0));
    assertThrows(IllegalArgumentException.class, () -> board.placeCard(0, 3, 0));
  }

  @Test
  public void testPlaceCardThrowsCellAlreadyOccupied() {
    board.startGame(redCards, blueCards, 1, false);
    board.placeCard(0, 0, 0);
    board.placeCard(0, 2, 0);
    assertThrows(IllegalArgumentException.class, () -> board.placeCard(0, 0, 0));
  }

  @Test
  public void testSkipTurn() {
    board.startGame(redCards, blueCards, 1, false);
    assertEquals(Player.RED, board.getCurrentTurn());
    board.skipTurn();
    assertEquals(Player.BLUE, board.getCurrentTurn());
  }

  @Test
  public void testSkipTurnThrowsGameNotStarted() {
    assertThrows(IllegalStateException.class, () -> board.skipTurn());
  }

  @Test
  public void testGetTurn() {
    board.startGame(redCards, blueCards, 1, false);
    assertEquals(Player.RED, board.getCurrentTurn());
    board.placeCard(0, 0, 0);
    assertEquals(Player.BLUE, board.getCurrentTurn());
    board.placeCard(0, 2, 0);
    assertEquals(Player.RED, board.getCurrentTurn());
    board.skipTurn();
    assertEquals(Player.BLUE, board.getCurrentTurn());
  }

  @Test
  public void testGetTurnThrowsGameNotStarted() {
    assertThrows(IllegalStateException.class, () -> board.getCurrentTurn());
  }

  @Test
  public void testGameOver() {
    board.startGame(redCards, blueCards, 1, false);
    board.skipTurn();
    board.skipTurn();
    assertTrue(board.isGameOver());
  }

  @Test
  public void testGameNotOverWhenTwoSkipsNonSequential() {
    board.startGame(redCards, blueCards, 1, false);
    board.skipTurn();
    board.placeCard(0, 2, 0);
    board.skipTurn();
    assertFalse(board.isGameOver());
  }

  @Test
  public void testGameOverThrowsGameNotStarted() {
    assertThrows(IllegalStateException.class, () -> board.isGameOver());
  }

  @Test
  public void testGetWinnerDraw() {
    board.startGame(redCards, blueCards, 1, false);
    board.skipTurn();
    board.skipTurn();
    assertEquals(null, board.getWinner());
  }

  @Test
  public void testGetWinnerRed() {
    board.startGame(redCards, blueCards, 1, false);
    board.placeCard(0, 0, 0);
    board.skipTurn();
    board.skipTurn();
    assertEquals(Player.RED, board.getWinner());
  }

  @Test
  public void testGetWinnerBlue() {
    board.startGame(redCards, blueCards, 1, false);
    board.skipTurn();
    board.placeCard(0, 2, 0);
    board.skipTurn();
    board.skipTurn();
    assertEquals(Player.BLUE, board.getWinner());
  }

  @Test
  public void testGetWinnerThrowsGameNotStarted() {
    assertThrows(IllegalStateException.class, () -> board.getWinner());
  }

  @Test
  public void testGetWinnerThrowsGameNotOver() {
    board.startGame(redCards, blueCards, 1, false);
    assertThrows(IllegalStateException.class, () -> board.getWinner());
  }

  @Test
  public void testGetBoard() {
    board.startGame(redCards, blueCards, 1, false);
    BoardCell[][] cells = board.getBoard();
    for (int row = 0; row < cells.length; row++) {
      for (int col = 0; col < cells[row].length; col++) {
        if (col == 0 || col == cells[row].length - 1) {
          assertEquals(1, cells[row][col].getPawns());
          if (col == 0) {
            assertEquals(Player.RED, cells[row][col].getOwner());
          }
          if (col == cells.length - 1) {
            assertEquals(Player.BLUE, cells[row][col].getOwner());
          }
        } else {
          assertEquals(null, cells[row][col].getOwner());
          assertEquals(0, cells[row][col].getPawns());
        }
        assertEquals(null, cells[row][col].getCard());
      }
    }
  }

  @Test
  public void testGetBoardWorksAfterPlayingCard() {
    board.startGame(redCards, blueCards, 1, false);
    board.placeCard(0, 0, 0);
    board.placeCard(0, 2, 0);
    BoardCell[][] cells = board.getBoard();
    assertEquals(redCards.get(0), cells[0][0].getCard());
    assertEquals(blueCards.get(0), cells[0][2].getCard());
  }

  @Test
  public void testGetBoardThrowsGameNotStarted() {
    assertThrows(IllegalStateException.class, () -> board.getBoard());
  }

  @Test
  public void testGetCell() {
    board.startGame(redCards, blueCards, 1, false);
    assertEquals(1, board.getCellAt(0, 0).getPawns());
    assertEquals(Player.RED, board.getCellAt(0, 0).getOwner());
    assertEquals(1, board.getCellAt(0, 2).getPawns());
    assertEquals(Player.BLUE, board.getCellAt(0, 2).getOwner());
    assertEquals(0, board.getCellAt(0, 1).getPawns());
    assertEquals(null, board.getCellAt(0, 1).getOwner());
    assertEquals(null, board.getCellAt(0, 0).getCard());
    board.placeCard(0, 0, 0);
    assertEquals(redCards.get(0), board.getCellAt(0, 0).getCard());
    board.placeCard(0, 2, 0);
    assertEquals(blueCards.get(0), board.getCellAt(0, 2).getCard());
  }

  @Test
  public void testGetCellThrowsGameNotStarted() {
    assertThrows(IllegalStateException.class, () -> board.getCellAt(0, 0));
  }

  @Test
  public void testGetCellThrowsInvalidCellCoordinates() {
    board.startGame(redCards, blueCards, 1, false);
    assertThrows(IllegalArgumentException.class, () -> board.getCellAt(-1, 0));
    assertThrows(IllegalArgumentException.class, () -> board.getCellAt(0, -1));
    assertThrows(IllegalArgumentException.class, () -> board.getCellAt(0, 3));
    assertThrows(IllegalArgumentException.class, () -> board.getCellAt(2, 0));
  }
}
