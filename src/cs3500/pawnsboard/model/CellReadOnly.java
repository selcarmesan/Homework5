package cs3500.pawnsboard.model;

public interface CellReadOnly<C extends Card> {

  /**
   * Returns the name of the player that owns this cell currently, whether pawns or card placement.
   * Returns null if there are no pawns or cards on this tile.
   *
   * @return the owning player
   */
  Player getOwner();

  /**
   * Returns the number of pawns currently present on this tile.
   *
   * @return the pawn number
   */
  int getPawns();

  /**
   * Returns this cell's card, or null if it does not have a card.
   *
   * @return the card
   */
  C getCard();
}
