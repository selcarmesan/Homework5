package cs3500.pawnsboard;

import java.util.List;

import cs3500.pawnsboard.model.PawnsBoardGame;
import cs3500.pawnsboard.model.PawnsCard;
import cs3500.pawnsboard.model.PawnsCardReader;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.view.PawnsBoardTextualView;

public class PawnsBoard {

  public static void main(String[] args) {
    //Check read from deck.config
    List<PawnsCard> redDeck = PawnsCardReader.readCards(Player.RED);
    List<PawnsCard> blueDeck = PawnsCardReader.readCards(Player.BLUE);

    //Initializing PawnsBoardGame
    PawnsBoardGame game = new PawnsBoardGame(3, 5);
    game.startGame(redDeck, blueDeck, 5, false);

    //Initializing PawnsBoardTextualView
    PawnsBoardTextualView view = new PawnsBoardTextualView(game);

    game.placeCard(0, 0, 0);
    System.out.println(view);
    game.placeCard(0, 4, 0);
    System.out.println(view);
    game.placeCard(1, 0, 4);
    System.out.println(view);
    game.placeCard(1, 4, 4);
    System.out.println(view);
    game.placeCard(2, 0, 4);
    System.out.println(view);
  }

}
