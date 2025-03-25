package cs3500.pawnsboard.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;

import javax.swing.*;

import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.PawnsBoardReadOnly;
import cs3500.pawnsboard.model.Player;

/**
 * A simple graphical interface for a game of pawnsboard.
 */
public class PawnsBoardVisualView extends JFrame implements PawnsBoardView {

  PawnsBoardReadOnly model;

  private int width;
  private int height;

  /**
   * A constructor for PawnsBoardVisualView, which takes in a game of PawnsBoard in read only.
   * @param model PawnsBoardReadOnly
   */
  public PawnsBoardVisualView(PawnsBoardReadOnly model) {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("PawnsBoard");
    this.setSize(1280, 720);
    this.width = this.getWidth();
    this.height = this.getHeight();
    this.setLayout(null);
    this.setResizable(false);
    this.setVisible(true);
    this.model = model;
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
        PawnsBoardCellButton cell = new PawnsBoardCellButton(row, col, model.getCellAt(row, col));
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
              i * cellSizeX, model.getRows() * cellSizeY, cellSizeX, cellSizeY * 2, i);
      this.add(card);
    }
  }

  @Override
  public PawnsBoardReadOnly getModel() {
    return model;
  }
}
