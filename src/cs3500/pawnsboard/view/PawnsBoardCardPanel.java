package cs3500.pawnsboard.view;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.Player;

public class PawnsBoardCardPanel extends JPanel implements PawnsBoardPanel, MouseListener {

  private Card card;
  private int index;
  private Player player;
  private PawnsBoardVisualView view;

  public PawnsBoardCardPanel(Card card, Player player, int x, int y, int width,
                             int height, int index, PawnsBoardVisualView view) {
    this.card = card;
    this.player = player;
    this.index = index;
    this.view = view;
    if (this.player.equals(Player.RED)) {
      this.setBackground(Color.RED);
    } else {
      this.setBackground(Color.BLUE);
    }
    this.setBounds(x, y, width, height);
    this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    this.setLayout(null);
    this.addMouseListener(this);

    JLabel cardDescription = new JLabel();
    cardDescription.setForeground(Color.WHITE);
    cardDescription.setFont(new Font("Arial", Font.BOLD, 18));
    cardDescription.setText(String.format("%s, Cost: %s, Value: %s",
            card.getName(), card.getCost(), card.getValue()));
    cardDescription.setBounds(0,0, width, height);
    cardDescription.setVerticalAlignment(JLabel.TOP);
    this.add(cardDescription);

    int sizeX = width/7;
    int sizeY = height/9;
    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 5; col++) {
        JLabel influence = new JLabel();
        if (row == 2 && col == 2) {
          influence.setBackground(Color.YELLOW);
        } else if (card.getInfluence()[row][col]) {
          influence.setBackground(Color.CYAN);
        } else {
          influence.setBackground(Color.DARK_GRAY);
        }
        influence.setOpaque(true);
        influence.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        influence.setBounds((col + 1) * sizeX, (row + 2) * sizeY, sizeX, sizeY);
        this.add(influence);
      }
    }
  }

  @Override
  public Player getPlayer() {
    return player;
  }

  @Override
  public int getIndex() {
    return index;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    System.out.println("Player: " + player + ", Index: " + index);
    if (!this.getBackground().equals(Color.CYAN)) {
      this.setBackground(Color.CYAN);
    } else {
      if (player.equals(Player.RED)) {
        this.setBackground(Color.RED);
      } else {
        this.setBackground(Color.BLUE);
      }
    }
    view.setLastChosenCard(this);
  }

  @Override
  public void mousePressed(MouseEvent e) {

  }

  @Override
  public void mouseReleased(MouseEvent e) {

  }

  @Override
  public void mouseEntered(MouseEvent e) {

  }

  @Override
  public void mouseExited(MouseEvent e) {

  }
}
