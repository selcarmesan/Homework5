This codebase functions as a playable game and view for a card game called Pawns Board.

To use this code, run the main file, or alternatively create a new PawnsBoardGame(rows, cols),
and startGame() using either the method that takes in lists for the decks, or the one that
automatically reads them from the config file

This game consists of two major portions, the model and view.  The model acts as the design for
the game itself, as well as the components of it, being the players available, red & blue, the
cards used for making moves as a player, and the cells that the board consists of.
The game itself is also capable of reading from the card reader class within the model, which
takes from the deck.config file, or with a specified list of cards for each player.

The view takes in a game of pawns board, and while active, is able to represent the board and hands
in a simple, textual form to view the current progress of the game.

The card class has a name, cost, value, owner, and influence grid.  It acts as a move to make
within the game, requiring the space it is played in to have the proper number of pawns belonging
to the player, after which its value (score) comes into play for the sake of tallying.

Each cell has a current number of pawns, an owner, and a card.  An empty cell starts with no pawns,
no owner, and no card, and the owner changes to the last player to influence it.  Once a card is
played, a card can no longer be played, and the cell essentially acts as a storage of the score.

The game class itself constructs a board of cells of the specified size, with the leftmost column
starting with 1 red pawn in each, and the rightmost column starting with one blue pawn in each.
The game allows each player to take a move in rotating order, either placing one of their cards
if possible, or skipping their turn.  The game continues until both players skip their turns
sequentially, in which case the game ends, and the winner can be determined.  Scoring is done
per row, and the player with the higher score cancels out all the opposing player's points
in that row.

The main class tests the functionality of the game. It runs a predetermined game of pawns board,
playing each move with the game class and displaying using the view.

All objects related to the model of the game can be found within the model package, along with
the object that reads out the cards from the config file and converts them to a list.
The view package contains the available view for the model, a textual representation.
The main class is placed in pawns board package outside of model and view.
The docs folder contains the deck config file.