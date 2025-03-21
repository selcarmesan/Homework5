package cs3500.pawnsboard.view;

import java.io.IOException;
import java.util.Objects;

import cs3500.pawnsboard.model.PawnsBoard;
import cs3500.pawnsboard.model.Player;

/**
 * PawnsBoardTextualView displays a game of PawnsBoard.
 */
public class PawnsBoardTextualView implements PawnsBoardView {

  private final PawnsBoard model;

  /**
   * Constructs a PawnsBoardTextualView from a given PawnsBoard model.
   *
   * @param model PawnsBoard model
   */
  public PawnsBoardTextualView(PawnsBoard model) {
    if (Objects.isNull(model)) {
      throw new IllegalArgumentException("Invalid Model");
    }
    this.model = model;
  }

  /**
   * Gets the model of the textual view.
   * @return model
   */
  public PawnsBoard getModel() {
    return model;
  }

  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.)
   * to the given appendable.
   * @throws IllegalArgumentException if append is null
   * @throws IOException if the rendering fails for some reason
   */
  @Override
  public void render(Appendable append) throws IOException {
    if (Objects.isNull(append)) {
      throw new IllegalArgumentException("Appendable is Null");
    }
    try {
      append.append(this.toString());
    } catch (IOException e) {
      throw new IOException(e.getMessage());
    }
  }

  @Override
  public String toString() {
    String output = "";
    for (int row = 0; row < model.getRows(); row++) {
      output = String.format(output + "%s ", model.getRowScore(Player.RED, row));
      for (int col = 0; col < model.getCols(); col++) {
        if (Objects.isNull(model.getCellAt(row, col).getOwner())) {
          output = String.format(output + "%s", "_");
        } else if (!Objects.isNull(model.getCellAt(row, col).getCard())) {
          if (model.getCellAt(row, col).getOwner() == Player.RED) {
            output = String.format(output + "%s", "R");
          }
          if (model.getCellAt(row, col).getOwner() == Player.BLUE) {
            output = String.format(output + "%s", "B");
          }
        } else {
          output = String.format(output + "%s", model.getCellAt(row, col).getPawns());
        }
      }
      output = String.format(output + " %s\n", model.getRowScore(Player.BLUE, row));
    }
    return output;
  }
}
