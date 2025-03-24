package cs3500.pawnsboard.model;

import org.junit.Before;
import org.junit.Test;

import cs3500.pawnsboard.model.strategies.PawnsBoardStrategy;
import cs3500.pawnsboard.model.strategies.StrategyFillFirst;
import cs3500.pawnsboard.model.strategies.StrategyMaximizeRowScore;

/**
 * Test class for strategies.
 */
public class StrategyTest {

  PawnsBoardStrategy fillFirst;
  PawnsBoardStrategy maxRowScore;

  @Before
  public void setUp() {
    fillFirst = new StrategyFillFirst();
    maxRowScore = new StrategyMaximizeRowScore();
  }

  @Test
  public void testFillFirstWorks() {

  }
}
