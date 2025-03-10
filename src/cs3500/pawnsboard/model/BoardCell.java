package cs3500.pawnsboard.model;

/**
 A singular cell on the playing board for a game of pawns board.  Each contains either a number
 * of pawns 0-3, or a played card, with the card taking priority.
 */
public class BoardCell implements Cell<PawnsCard> {

  private Player owner;
  private int pawns;
  private PawnsCard card;
  private boolean cardPlayed;

  /**
   * Creates a new empty cell with no owner, no card, and no pawns.
   */
  public BoardCell() {
    owner = null;
    pawns = 0;
    card = null;
  }

  /**
   * Returns the name of the player that owns this cell currently, whether pawns or card placement.
   * Returns null if there are no pawns or cards on this tile.
   *
   * @return the owning player
   */
  @Override
  public Player getOwner() {
    return owner;
  }

  /**
   * Returns the number of pawns currently present on this tile.
   *
   * @return the pawn number
   */
  @Override
  public int getPawns() {
    return pawns;
  }

  /**
   * Returns this cell's card, or null if it does not have a card.
   *
   * @return the card
   */
  @Override
  public PawnsCard getCard() {
    return new PawnsCard(card.getName(), card.getCost(),
            card.getValue(), card.getColor(), card.getInfluence());
  }

  /**
   * Increments the number of pawns by one if less than 3.
   *
   * @param player the player to change ownership to, only used if no owner was previously present
   * @throws IllegalArgumentException if pawns currently not zero, or if new player is null
   * @throws IllegalStateException if cell already has a played card
   */
  @Override
  public void addPawn(Player player) {
    if (cardPlayed) {
      throw new IllegalStateException("Cannot add pawns to a cell with a played card");
    }
    if (pawns > 0) {
      throw new IllegalArgumentException("Must use other method call if pawns already present");
    }
    if (player == null) {
      throw new IllegalArgumentException("Cannot have null-owned pawns");
    }
    pawns++;
    owner = player;
  }

  /**
   * Increments the number of pawns by one if less than 3.
   *
   * @throws IllegalArgumentException if the cell currently has no owner
   * @throws IllegalStateException if cell already has a played card
   */
  @Override
  public void addPawn() {
    if (cardPlayed) {
      throw new IllegalStateException("Cannot add pawns to a cell with a played card");
    }
    if (owner == null) {
      throw new IllegalArgumentException("Must specify owner when adding pawns to empty cell");
    }
    if (pawns != 3) {
      pawns++;
    }
  }

  /**
   * Changes the ownership of the cell.  Can only do so if it has pawns on it, and no card.
   *
   * @param newOwner the new owner
   * @throws IllegalArgumentException if board has no owner currently (zero pawns)
   *                                  if the new owner is null and the current owner is not null
   * @throws IllegalStateException if cell already has a played card
   */
  @Override
  public void changeOwner(Player newOwner) {
    if (cardPlayed) {
      throw new IllegalStateException("Cannot add pawns to a cell with a played card");
    }
    if (pawns == 0) {
      throw new IllegalArgumentException("Cannot change ownership when cell is empty");
    }
    if (newOwner == null && owner != null) {
      throw new IllegalArgumentException("Cannot change ownership to null");
    }
    owner = newOwner;
  }

  /**
   * Sets the card of this cell to the given card if one has not already been played.
   *
   * @param card the card
   * @throws IllegalStateException if the cell already has a card set
   * @throws IllegalArgumentException if supplied card is null
   */
  @Override
  public void playCard(PawnsCard card) {
    if (this.card != null) {
      throw new IllegalArgumentException("Cannot play card on cell with a card");
    }
    if (card == null) {
      throw new IllegalArgumentException("Cannot play null card");
    }
    this.card = card;
    owner = card.getColor();
    pawns = 0;
    cardPlayed = true;
  }
}
