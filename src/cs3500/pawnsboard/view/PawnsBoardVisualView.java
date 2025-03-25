package cs3500.pawnsboard.view;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Objects;

import javax.swing.*;

import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.PawnsBoardReadOnly;
import cs3500.pawnsboard.model.Player;

/**
 * A simple graphical interface for a game of pawnsboard.
 */
public class PawnsBoardVisualView extends JFrame implements PawnsBoardVisual, KeyListener {

  PawnsBoardReadOnly model;
  private final int width;
  private final int height;
  private PawnsBoardCellButton lastChosenCell;
  private PawnsBoardCardPanel lastChosenCard;

  /**
   * A constructor for PawnsBoardVisualView, which takes in a game of PawnsBoard in read only.
   * Sets up the initial board and card.
   * @param model PawnsBoardReadOnly
   * @throws IllegalArgumentException when the model is null
   */
  public PawnsBoardVisualView(PawnsBoardReadOnly model) {
    if (Objects.isNull(model)) {
      throw new IllegalArgumentException("Invalid Model");
    }
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("PawnsBoard");
    this.setSize(1280, 720);
    this.width = this.getWidth();
    this.height = this.getHeight();
    this.model = model;
    this.setLayout(null);
    this.setResizable(false);
    this.setVisible(true);
    this.addKeyListener(this);
    lastChosenCell = null;
    lastChosenCard = null;
    generateBoard();
    generateCards();
  }

  @Override
  public void update() {
    //Remove Previous Board
    this.getContentPane().removeAll();

    //Get New Board
    this.generateBoard();
    this.generateCards();

    //Update JFrame to Display Changes
    this.repaint();
    this.revalidate();
  }

  /**
   * Generates a board taking in the current state of
   */
  private void generateBoard() {
    int numRow = model.getRows();
    int numCol = model.getCols();

    int cellSizeX = width / (numCol + 2);
    int cellSizeY = height / (numRow + 2);

    for (int row = 0; row < numRow; row++) {
      JLabel rowScore = new JLabel();
      String value = String.format("%s", model.getRowScore(Player.RED, row));
      rowScore.setFont(new Font("Arial", Font.BOLD, 18));
      rowScore.setText(value);
      rowScore.setBackground(Color.WHITE);
      rowScore.setOpaque(true);
      rowScore.setHorizontalAlignment(JLabel.CENTER);
      rowScore.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
      rowScore.setBounds(0, row * cellSizeY, cellSizeX, cellSizeY);
      this.add(rowScore);
      for (int col = 0; col < numCol; col++) {
        PawnsBoardCellButton cell = new PawnsBoardCellButton(row, col,
                model.getCellAt(row, col), this);
        cell.setBounds((col + 1) * cellSizeX, row * cellSizeY, cellSizeX, cellSizeY);
        this.add(cell);
      }
      rowScore = new JLabel();
      value = String.format("%s", model.getRowScore(Player.BLUE, row));
      rowScore.setFont(new Font("Arial", Font.BOLD, 18));
      rowScore.setText(value);
      rowScore.setBackground(Color.WHITE);
      rowScore.setOpaque(true);
      rowScore.setHorizontalAlignment(JLabel.CENTER);
      rowScore.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
      rowScore.setBounds((numCol + 1) * cellSizeX, row * cellSizeY, cellSizeX, cellSizeY);
      this.add(rowScore);
    }
  }

  private void generateCards() {
    Player currentPlayer = model.getCurrentTurn();
    List<Card> hand = model.getHand(currentPlayer);
    int cellSizeX = width / hand.size();
    int cellSizeY = height / (model.getRows() + 2);
    for (int i = 0; i < hand.size(); i++) {
      PawnsBoardCardPanel card = new PawnsBoardCardPanel(hand.get(i), currentPlayer,
              i * cellSizeX, model.getRows() * cellSizeY,
              cellSizeX, cellSizeY * 2, i, this);
      this.add(card);
    }
  }

  @Override
  public PawnsBoardReadOnly getModel() {
    return model;
  }

  @Override
  public void setLastChosenCell(PawnsBoardCellButton cell) {
    if (Objects.isNull(lastChosenCell)) {
      lastChosenCell = cell;
    } else if (Objects.isNull(cell)) {
      changeCellColor();
      lastChosenCell = null;
    } else if (lastChosenCell.equals(cell)) {
      changeCellColor();
      lastChosenCell = null;
    } else {
      changeCellColor();
      cell.setBackground(Color.CYAN);
      lastChosenCell = cell;
    }
  }

  /**
   * Returns the color of the lastChosenCell to its original color.
   */
  private void changeCellColor() {
    Player lastPlayer = lastChosenCell.getCell().getOwner();
    if (Objects.isNull(lastPlayer) || lastChosenCell.getCell().getPawns() != 0) {
      lastChosenCell.setBackground(Color.GRAY);
    } else if (lastPlayer.equals(Player.RED)) {
      lastChosenCell.setBackground(Color.RED);
    } else {
      lastChosenCell.setBackground(Color.BLUE);
    }
  }

  @Override
  public void setLastChosenCard(PawnsBoardCardPanel card) {
    if (Objects.isNull(lastChosenCard)) {
      lastChosenCard = card;
    } else if (lastChosenCard.equals(card)) {
      if (lastChosenCard.getPlayer().equals(Player.RED)) {
        lastChosenCard.setBackground(Color.RED);
      } else {
        lastChosenCard.setBackground(Color.BLUE);
      }
      setLastChosenCell(null);
      lastChosenCard = null;
    } else {
      if (lastChosenCard.getPlayer().equals(Player.RED)) {
        lastChosenCard.setBackground(Color.RED);
      } else {
        lastChosenCard.setBackground(Color.BLUE);
      }
      setLastChosenCell(null);
      lastChosenCard = card;
    }
  }

  @Override
  public PawnsBoardCellButton getLastChosenCell() {
    return lastChosenCell;
  }

  @Override
  public PawnsBoardCardPanel getLastChosenCard() {
    return lastChosenCard;
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
      System.out.println("Confirm");
    } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
      System.out.println("Pass");
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {

  }
}
