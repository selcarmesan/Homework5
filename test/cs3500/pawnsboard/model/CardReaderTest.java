package cs3500.pawnsboard.model;

import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test class for PawnsCardReader.
 */
public class CardReaderTest {

  @Test
  public void testCardReaderReadsRed() {
    List<PawnsCard> cards = PawnsCardReader.readCards(Player.RED);
    Card card1 = cards.get(0);
    Card card2 = cards.get(1);
    Card card3 = cards.get(2);
    assertEquals("Grab", card1.getName());
    assertEquals(1, card1.getCost());
    assertEquals(1, card1.getValue());
    assertEquals(Player.RED, card1.getColor());
    assertEquals(grabInfluenceRed(), card1.getInfluence());
    assertEquals("Toss", card2.getName());
    assertEquals(1, card2.getCost());
    assertEquals(1, card2.getValue());
    assertEquals(Player.RED, card2.getColor());
    assertEquals(tossInfluenceRed(), card2.getInfluence());
    assertEquals("Geronimo", card3.getName());
    assertEquals(1, card3.getCost());
    assertEquals(2, card3.getValue());
    assertEquals(Player.RED, card3.getColor());
    assertEquals(geronimoInfluence(), card3.getInfluence());
  }

  @Test
  public void testCardReaderReadsBlue() {
    List<PawnsCard> cards = PawnsCardReader.readCards(Player.BLUE);
    Card card1 = cards.get(0);
    Card card2 = cards.get(1);
    Card card3 = cards.get(2);
    assertEquals(Player.BLUE, card1.getColor());
    assertEquals(grabInfluenceBlue(), card1.getInfluence());
    assertEquals(Player.BLUE, card2.getColor());
    assertEquals(tossInfluenceBlue(), card2.getInfluence());
    assertEquals(Player.BLUE, card3.getColor());
    assertEquals(geronimoInfluence(), card3.getInfluence());
  }

  @Test
  public void testCardReaderThrowsIfPlayerNull() {
    assertThrows(IllegalArgumentException.class, () -> PawnsCardReader.readCards(null));
  }

  @Test
  public void testCardReaderThrowsIfCardSpotUnmarked() {
    File file = new File("test" + File.separator + "testDocs"
            + File.separator + "test1.config");
    assertThrows(IllegalStateException.class,
        () -> PawnsCardReader.readCardsAlternativeSource(Player.RED, file));
  }

  @Test
  public void testCardReaderAlternateSourceThrowsNullArguments() {
    File file = new File("docs" + File.separator + "deck.config");
    assertThrows(IllegalArgumentException.class,
        () -> PawnsCardReader.readCardsAlternativeSource(Player.BLUE, null));
    assertThrows(IllegalArgumentException.class,
        () -> PawnsCardReader.readCardsAlternativeSource(null, file));
  }

  @Test
  public void testCardReaderThrowsIfFileFormattedIncorrectly() {
    File file1 = new File("test" + File.separator + "testDocs"
            + File.separator + "test2.config");
    assertThrows(IllegalArgumentException.class,
        () -> PawnsCardReader.readCardsAlternativeSource(Player.RED, file1));
    File file2 = new File("test" + File.separator + "testDocs"
            + File.separator + "test3.config");
    assertThrows(IllegalArgumentException.class,
        () -> PawnsCardReader.readCardsAlternativeSource(Player.RED, file2));
    File file3 = new File("test" + File.separator + "testDocs"
            + File.separator + "test4.config");
    assertThrows(IllegalArgumentException.class,
        () -> PawnsCardReader.readCardsAlternativeSource(Player.RED, file3));
    File file4 = new File("test" + File.separator + "testDocs"
            + File.separator + "test5.config");
    assertThrows(IllegalArgumentException.class,
        () -> PawnsCardReader.readCardsAlternativeSource(Player.RED, file4));
    File file5 = new File("test" + File.separator + "testDocs"
            + File.separator + "test6.config");
    assertThrows(IllegalArgumentException.class,
        () -> PawnsCardReader.readCardsAlternativeSource(Player.RED, file5));
    File file6 = new File("test" + File.separator + "testDocs"
            + File.separator + "test7.config");
    assertThrows(IllegalArgumentException.class,
        () -> PawnsCardReader.readCardsAlternativeSource(Player.RED, file6));
  }

  private boolean[][] grabInfluenceRed() {
    boolean[][] grab = new boolean[5][5];
    grab[2][2] = true;
    grab[2][3] = true;
    grab[2][4] = true;
    return grab;
  }

  private boolean[][] grabInfluenceBlue() {
    boolean[][] grab = new boolean[5][5];
    grab[2][0] = true;
    grab[2][1] = true;
    grab[2][2] = true;
    return grab;
  }

  private boolean[][] tossInfluenceRed() {
    boolean[][] toss = new boolean[5][5];
    toss[2][2] = true;
    toss[2][4] = true;
    return toss;
  }

  private boolean[][] tossInfluenceBlue() {
    boolean[][] toss = new boolean[5][5];
    toss[2][0] = true;
    toss[2][2] = true;
    return toss;
  }

  private boolean[][] geronimoInfluence() {
    boolean[][] geronimo = new boolean[5][5];
    geronimo[2][2] = true;
    geronimo[3][2] = true;
    geronimo[4][2] = true;
    return geronimo;
  }

}
