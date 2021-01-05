import java.awt.Color;

public class Constants {

	// TODO
	// [] add in undo feature
	// [] add in forfeit
	// [] add in multiple jump tracker
	// [] make the tracker line thicker
	// [X] add in a move history printout
	// printing out to console for now .... make this prettier later
	// [X] potential bug found where a previous jump chain allows another piece to go
	// this was caused because
	// [X] only make computer move when you click
	// did this to make it easier to see the moves the computer makes

	/**
	 * Number of moves to look ahead
	 */
	public static final int PLIES = 8;

	public static final int HUMAN = 0;
	public static final int COMPUTER = 1;

	public static final int BLACK_PLAYER = 1;
	public static final int RED_PLAYER = 2;
	public static final int DRAW = 0;

	public static final int EMPTY = 0;
	public static final int RED_CHECKER = 1;
	public static final int BLACK_CHECKER = 2;
	public static final int RED_KING = 3;
	public static final int BLACK_KING = 4;

	public static final int ROWS = 8;
	public static final int COLUMNS = 8;

	/**
	 * The number of turns to take before declaring a draw
	 */
	public static final int DRAW_MOVES_WITHOUT_CAPTURE = 50;

	/**
	 * Number of times the same move can be made before declaring a draw
	 */
	public static final int DRAW_REPEATED_MOVES = 20;

	/**
	 * Grid size in pixels per box
	 */
	public static final int GRID_SIZE = 50;

	public static final Color RED_COLOR = new Color(204, 0, 0);
	public static final Color DARK_RED_COLOR = new Color(102, 0, 0);
	public static final Color DARK_BLACK_COLOR = new Color(96, 96, 96);
	public static final Color BLACK_COLOR = new Color(13, 13, 13);
	public static final Color GRID_COLOR = new Color(153, 153, 153);
	public static final Color SQUARE_COLOR = new Color(185, 185, 185);
	public static final Color HIGHLIGHT_COLOR = new Color(81, 81, 255);
	public static final Color LINE_COLOR = new Color(185, 102, 238);

	public static final String playerToString(int player) {
		if (player == Constants.RED_PLAYER) {
			return "Red";
		} else if (player == Constants.BLACK_PLAYER) {
			return "Black";
		}
		return "Player-Not-Found";
	}

	public static final String pieceToString(int piece) {
		switch (piece) {
		case Constants.EMPTY:
			return "Empty";
		case Constants.RED_CHECKER:
			return "Red Checker";
		case Constants.BLACK_CHECKER:
			return "Black Checker";
		case Constants.RED_KING:
			return "Red King";
		case Constants.BLACK_KING:
			return "Black King";
		default:
			return "Not Found";
		}
	}

}
