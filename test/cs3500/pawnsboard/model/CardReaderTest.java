package cs3500.pawnsboard.model;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CardReaderTest {

  @Test
  public void testCardReaderReadsRed() {
    List<PawnsCard> cards = PawnsCardReader.readCards(Player.RED);
    Card card1 = cards.get(0);
    Card card2 = cards.get(1);
    Card card3 = cards.get(2);
    assertEquals("Smiler", card1.getName());
    assertEquals(3, card1.getCost());
    assertEquals(2, card1.getValue());
    assertEquals(Player.RED, card1.getColor());
    assertEquals(heartInfluence(), card1.getInfluence());
    assertEquals("I-95", card2.getName());
    assertEquals(2, card2.getCost());
    assertEquals(1, card2.getValue());
    assertEquals(Player.RED, card2.getColor());
    assertEquals(I95InfluenceRed(), card2.getInfluence());
    assertEquals("Love", card3.getName());
    assertEquals(3, card3.getCost());
    assertEquals(3, card3.getValue());
    assertEquals(Player.RED, card3.getColor());
    assertEquals(LoveInfluence(), card3.getInfluence());
  }

  @Test
  public void testCardReaderReadsBlue() {
    List<PawnsCard> cards = PawnsCardReader.readCards(Player.BLUE);
    Card card1 = cards.get(0);
    Card card2 = cards.get(1);
    Card card3 = cards.get(2);
    assertEquals(Player.BLUE, card1.getColor());
    assertEquals(heartInfluence(), card1.getInfluence());
    assertEquals(Player.BLUE, card2.getColor());
    assertEquals(I95InfluenceBlue(), card2.getInfluence());
    assertEquals(Player.BLUE, card3.getColor());
    assertEquals(LoveInfluence(), card3.getInfluence());
  }

  @Test
  public void testCardReaderThrowsIfPlayerNull() {
    assertThrows(IllegalArgumentException.class, () -> PawnsCardReader.readCards(null));
  }

  // Fix
  @Test
  public void testCardReaderThrowsIfCardSpotUnmarked() throws FileNotFoundException {
    File file = new File("docs" + File.separator + "deck.config");
  }

  // Fix
  @Test
  public void testCardReaderThrowsIfFileFormattedIncorrectly() {
    File file = new File("docs" + File.separator + "deck.config");
  }

  private boolean[][] heartInfluence() {
    boolean[][] heart = new boolean[5][5];
    heart[0][1] = true;
    heart[0][3] = true;
    heart[1][1] = true;
    heart[1][3] = true;
    heart[2][0] = true;
    heart[2][2] = true;
    heart[2][4] = true;
    heart[3][0] = true;
    heart[3][2] = true;
    heart[3][4] = true;
    heart[4][1] = true;
    heart[4][3] = true;
    return heart;
  }

  private boolean[][] I95InfluenceRed() {
    boolean[][] I95 = new boolean[5][5];
    I95[0][2] = true;
    I95[0][4] = true;
    I95[1][2] = true;
    I95[1][4] = true;
    I95[2][2] = true;
    I95[2][4] = true;
    I95[3][2] = true;
    I95[3][4] = true;
    I95[4][2] = true;
    I95[4][4] = true;
    return I95;
  }

  private boolean[][] I95InfluenceBlue() {
    boolean[][] I95 = new boolean[5][5];
    I95[0][2] = true;
    I95[0][0] = true;
    I95[1][2] = true;
    I95[1][0] = true;
    I95[2][2] = true;
    I95[2][0] = true;
    I95[3][2] = true;
    I95[3][0] = true;
    I95[4][2] = true;
    I95[4][0] = true;
    return I95;
  }

  private boolean[][] LoveInfluence() {
    boolean[][] Love = new boolean[5][5];
    Love[0][1] = true;
    Love[0][3] = true;
    Love[1][0] = true;
    Love[1][2] = true;
    Love[1][4] = true;
    Love[2][0] = true;
    Love[2][2] = true;
    Love[2][4] = true;
    Love[3][1] = true;
    Love[3][3] = true;
    Love[4][2] = true;
    return Love;
  }

}
