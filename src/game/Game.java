package game;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {

	public static boolean detectDraw(PlayBoard board) {
		// if there haven't been enough moves, exit early
		List<Move> moves = board.getMoves();
		if (moves.size() < Constants.DRAW_MOVES_WITHOUT_CAPTURE && moves.size() < Constants.DRAW_REPEATED_MOVES) {
			return false;
		}

		// if we have made too many moves with a jump or if the same state has been seen three times
		int movesBeforeJump = 0;
		for (Move move : moves) {
			if (!move.getIsValidJump()) {
				movesBeforeJump++;
			} else {
				movesBeforeJump = 0;
			}
			if (movesBeforeJump >= Constants.DRAW_MOVES_WITHOUT_CAPTURE) {
				System.out.println("Draw due to no captures.");
				return true;
			}
		}

		// check to see if the same move was made a certain number of times
		// the Move class implements hashCode
		HashMap<Move, Integer> moveCount = new HashMap<Move, Integer>();
		for (Move move : moves) {
			if (moveCount.containsKey(move)) {
				moveCount.put(move, moveCount.get(move) + 1);
			} else {
				moveCount.put(move, 1);
			}
		}
		for (Integer dupes : moveCount.values()) {
			if (dupes >= Constants.DRAW_REPEATED_MOVES) {
				System.out.println("Draw due to repeated moves.");
				return true;
			}
		}

		return false;
	}

	public static Board makeMove(Board board, Move move) {
		return makeMove(board, move, true);
	}

	public static Board makeMove(Board board, Move move, boolean validate) {

		if (validate) {
			boolean isValid = validateMove(board, move);
			if (!isValid) {
				return board;
			}
		}

		// TODO for easier debugging, change later to just access from move
		int player = move.getPlayer();
		int startRow = move.getStartRow();
		int startCol = move.getStartCol();
		int endRow = move.getEndRow();
		int endCol = move.getEndCol();

		int piece = board.getPiece(startRow, startCol);

		boolean isJump = move.getIsValidJump();
		if (isJump) {

			// move the original piece
			board.setValue(startRow, startCol, Constants.EMPTY);

			int jumpRow = move.getJumpEndRow();
			int jumpCol = move.getJumpEndCol();

			// remove the middle piece
			board.setValue(endRow, endCol, Constants.EMPTY);

			// move the current piece
			board.setValue(jumpRow, jumpCol, piece);

			// if this SAME piece is capable of more jumps it must continue
			List<Move> additionalMoves = Game.getValidMovesForPiece(board, player, piece, jumpRow, jumpCol);
			boolean hasMoreJumps = false;
			for (Move additionalMove : additionalMoves) {
				if (additionalMove.getIsValidJump()) {
					hasMoreJumps = true;
					break;
				}
			}

			// if the piece can continue jumping it must
			if (hasMoreJumps) {
				board.setTurn(player);
			} else {
				board.setTurn(getOtherPlayer(player));
			}

			// kings are checked at the end so we don't run into the
			// bug where a king is made then continues jumping
			// check if the piece has become a king
			boolean kingPiece = checkMoveMakesKing(player, jumpRow);
			if (kingPiece) {
				if (player == Constants.RED_PLAYER) {
					piece = Constants.RED_KING;
				} else {
					piece = Constants.BLACK_KING;
				}
				board.setValue(jumpRow, jumpCol, piece);
			}

		} else {

			// move the original piece
			board.setValue(startRow, startCol, Constants.EMPTY);

			// check if the piece has become a king
			boolean kingPiece = checkMoveMakesKing(player, endRow);
			if (kingPiece) {
				if (player == Constants.RED_PLAYER) {
					piece = Constants.RED_KING;
				} else {
					piece = Constants.BLACK_KING;
				}
			}
			board.setValue(endRow, endCol, piece);

			// swap turns since no jump was made
			board.setTurn(getOtherPlayer(player));
		}

		// board.addMove(move);
		// board.setErrorMessage("");
		return board;
	}

	public static boolean checkGameOver(PlayBoard board, int player) {
		// the game ends if the player cannot make any moves
		List<Move> moves = getAvailableMoves(board, player);
		if (moves.isEmpty()) {
			board.setGameOver(true);
			board.setWinner(getOtherPlayer(player));
		}

		// check for a draw
		if (detectDraw(board)) {
			board.setGameOver(true);
			board.setWinner(Constants.DRAW);
		}

		return board.isGameOver();
	}

	private static boolean checkMoveMakesKing(int player, int endRow) {
		if (player == Constants.RED_PLAYER && endRow == 0) {
			return true;
		} else if (player == Constants.BLACK_PLAYER && endRow == Constants.ROWS - 1) {
			return true;
		}
		return false;
	}

	private static List<Move> getPotentialMoves(Board board, int player) {
		List<Move> allMoves = new ArrayList<Move>();
		for (int r = 0; r < Constants.ROWS; r++) {
			for (int c = 0; c < Constants.COLUMNS; c++) {
				int piece = board.getPiece(r, c);

				// save a bit of double checking by making sure we are the owner first
				if (!isOwner(piece, player)) {
					continue;
				}
				List<Move> pieceMoves = getValidMovesForPiece(board, player, piece, r, c);
				allMoves.addAll(pieceMoves);
			}
		}
		return allMoves;
	}

	public static List<Move> getAvailableMoves(Board board, int player) {
		List<Move> allMoves = getPotentialMoves(board, player);
		// filter this so that if there is a jump that must be taken!
		List<Move> jumps = new ArrayList<Move>();
		for (Move move : allMoves) {
			if (move.getIsValidJump()) {
				jumps.add(move);
			}
		}

		// if there are no jumps regular moves are valid
		if (jumps.isEmpty()) {
			return allMoves;
		}

		// if there are
		return jumps;
	}

	public static List<Move> getAvailableJumps(Board board, int player) {
		List<Move> possibleJumps = new ArrayList<Move>();
		List<Move> allMoves = getPotentialMoves(board, player);
		for (Move move : allMoves) {
			if (move.getIsValidJump()) {
				possibleJumps.add(move);
			}
		}

		return possibleJumps;
	}

	public static boolean validateMove(Board board, Move move) {
		boolean isValidMove = isValidMove(board, move);
		boolean isValidJump = isValidJump(board, move);

		List<Move> availableJumps = getAvailableJumps(board, move.getPlayer());
		boolean hasAvailableJumps = !availableJumps.isEmpty();

		boolean isValid = isValidMove || isValidJump;
		if (isValidMove && !isValidJump && hasAvailableJumps) {
			isValid = false;
			move.setIsValid(false);
			move.setErrorMessage("Must take a jump if available!");
			return false;
		}

		if (!isValid) {
			// for error messages, find the reason
			if (!isValidMove && isValidJump) {
				move.setErrorMessage(move.getJumpError());
			} else if (isValidMove && !isValidJump) {
				move.setErrorMessage(move.getMoveError());
			} else if (!isValidMove && !isValidJump) {
				move.setErrorMessage(move.getJumpError());
			}
			move.setIsValid(false);
			return false;
		}
		move.setIsValid(true);
		return true;

	}

	private static boolean isValidMove(Board board, Move move) {

		// taken out here for easier debugging
		int startRow = move.getStartRow();
		int startCol = move.getStartCol();
		int player = move.getPlayer();
		int endRow = move.getEndRow();
		int endCol = move.getEndCol();

		// check that a piece exists and it is the players
		int piece = board.getPiece(startRow, startCol);
		if (piece == -1 || !isOwner(piece, player)) {
			move.setIsValidMove(false);
			move.setMoveError("Must select a piece you own!");
			return false;
		}

		// check if the piece will remain in bounds
		boolean inBounds = isInBounds(endRow, endCol);
		if (!inBounds) {
			move.setIsValidMove(false);
			move.setMoveError("Cannot move out of bounds!");
			return false;
		}

		// check the the player is moving diagonally and in the correct column direction
		// kings get to ignore the column direction
		boolean correctDirection = isCorrectDirection(player, piece, startRow, startCol, endRow, endCol);
		if (!correctDirection) {
			move.setIsValidMove(false);
			move.setMoveError("Must move in the correct direction!");
			return false;
		}

		// check that the move space is free
		int endPiece = board.getPiece(endRow, endCol);
		boolean spaceEmpty = endPiece == Constants.EMPTY;
		if (!spaceEmpty) {
			move.setIsValidMove(false);
			move.setMoveError("Must select an empty space to move!");
			return false;
		}

		move.setIsValidMove(true);
		return true;
	}

	private static boolean isValidJump(Board board, Move move) {

		// TODO for easier debugging, change later to just access from move
		int player = move.getPlayer();
		int startRow = move.getStartRow();
		int startCol = move.getStartCol();
		int endRow = move.getEndRow();
		int endCol = move.getEndCol();

		// check that a piece exists and it is the players
		int piece = board.getPiece(startRow, startCol);
		if (piece == -1 || !isOwner(piece, player)) {
			move.setIsValidJump(false);
			move.setJumpError("Must select a piece you own!");
			return false;
		}

		// check the the player is moving diagonally and in the correct column direction
		// kings get to ignore the column direction
		boolean correctDirection = isCorrectDirection(player, piece, startRow, startCol, endRow, endCol);
		if (!correctDirection) {
			move.setIsValidJump(false);
			move.setJumpError("Must move in the correct direction!");
			return false;
		}

		// its a jump if the selected piece is the enemies
		// and the jump space is empty
		// and the jump space is in bounds
		int rDir = endRow - startRow;
		int cDir = endCol - startCol;
		int jumpRow = rDir >= 1 ? endRow + 1 : endRow - 1;
		int jumpCol = cDir >= 1 ? endCol + 1 : endCol - 1;
		move.setJumpEndRow(jumpRow);
		move.setJumpEndCol(jumpCol);

		boolean jumpInBounds = isInBounds(jumpRow, jumpCol);
		// check to see if this is a jump
		if (!jumpInBounds) {
			move.setIsValidJump(false);
			move.setJumpError("Jumping space must be available!");
			return false;
		}

		boolean jumpAvailable = board.getPiece(jumpRow, jumpCol) == Constants.EMPTY;
		// check to see if this is a jump
		if (!jumpAvailable) {
			move.setIsValidJump(false);
			move.setJumpError("Jumping space must be available!");
			return false;
		}

		// space must be occupied by an enemy
		// check that the move space is free (or enemy to allow jump)
		int endPiece = board.getPiece(endRow, endCol);
		boolean spaceEmpty = endPiece == Constants.EMPTY;
		boolean enemyOwned = getOwner(endPiece) == getOtherPlayer(player);
		if (spaceEmpty || !enemyOwned) {
			move.setIsValidJump(false);
			move.setJumpError("You can only jump over an enemy piece!");
			return false;
		}

		move.setIsValidJump(true);
		return true;
	}

	private static List<Move> getValidMovesForPiece(Board board, int player, int piece, int startRow, int startCol) {
		List<Move> moves = new ArrayList<Move>();

		// black moves south
		// red moves north
		// kings do both

		boolean isKing = isKing(piece);

		// up left
		if (player == Constants.RED_PLAYER || isKing) {
			Move upLeft = new Move(player, startRow, startCol, startRow - 1, startCol - 1);
			if (isValidJump(board, upLeft) || isValidMove(board, upLeft)) {
				moves.add(upLeft);
			}
		}

		// up right
		if (player == Constants.RED_PLAYER || isKing) {
			Move upRight = new Move(player, startRow, startCol, startRow - 1, startCol + 1);
			if (isValidJump(board, upRight) || isValidMove(board, upRight)) {
				moves.add(upRight);
			}
		}

		// down left
		if (player == Constants.BLACK_PLAYER || isKing) {
			Move downLeft = new Move(player, startRow, startCol, startRow + 1, startCol - 1);
			if (isValidJump(board, downLeft) || isValidMove(board, downLeft)) {
				moves.add(downLeft);
			}
		}

		// down right
		if (player == Constants.BLACK_PLAYER || isKing) {
			Move downRight = new Move(player, startRow, startCol, startRow + 1, startCol + 1);
			if (isValidJump(board, downRight) || isValidMove(board, downRight)) {
				moves.add(downRight);
			}
		}

		return moves;
	}

	private static boolean isInBounds(int endRow, int endCol) {
		return endRow >= 0 && endRow < Constants.ROWS && endCol >= 0 && endCol < Constants.COLUMNS;
	}

	private static boolean isCorrectDirection(int player, int piece, int startRow, int startCol, int endRow, int endCol) {
		int rDir = endRow - startRow;
		int cDir = endCol - startCol;

		boolean correctRowDir;
		boolean correctColDir;
		if (isKing(piece)) {
			correctRowDir = rDir == 1 || rDir == -1;
			correctColDir = cDir == 1 || cDir == -1;
		} else {
			correctRowDir = player == Constants.BLACK_PLAYER ? rDir == 1 : rDir == -1;
			correctColDir = cDir == 1 || cDir == -1;
		}
		return correctRowDir && correctColDir;
	}

	public static int getOtherPlayer(int player) {
		if (player == Constants.RED_PLAYER) {
			return Constants.BLACK_PLAYER;
		} else if (player == Constants.BLACK_PLAYER) {
			return Constants.RED_PLAYER;
		}
		return -1;
	}

	public static boolean isOwner(int piece, int player) {
		return player == getOwner(piece);
	}

	public static int getOwner(int piece) {
		if (piece == Constants.EMPTY) {
			return Constants.EMPTY;
		} else if (piece == Constants.RED_CHECKER || piece == Constants.RED_KING) {
			return Constants.RED_PLAYER;
		} else if (piece == Constants.BLACK_CHECKER || piece == Constants.BLACK_KING) {
			return Constants.BLACK_PLAYER;
		}
		// should never occur
		return -1;
	}

	public static boolean isKing(int piece) {
		return piece == Constants.RED_KING || piece == Constants.BLACK_KING;
	}
}
