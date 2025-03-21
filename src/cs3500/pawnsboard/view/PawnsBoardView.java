package cs3500.pawnsboard.view;

import java.io.IOException;

import cs3500.pawnsboard.model.PawnsBoard;

/**
 * Interface for a PawnsBoard game view.
 */
public interface PawnsBoardView {

  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.)
   * to the given appendable.
   * @throws IllegalArgumentException if append is null
   * @throws IOException if the rendering fails for some reason
   */
  void render(Appendable append) throws IOException;

  /**
   * Gets the model of the textual view.
   * @return model
   */
  PawnsBoard getModel();
}
