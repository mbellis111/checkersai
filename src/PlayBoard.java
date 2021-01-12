import java.util.ArrayList;
import java.util.List;

public class PlayBoard implements Board {

	// java (mostly) uses word boundaries so unless on an embedded device just use an int instead
	// we will track using ROWS, then COLUMNS
	private int[][] board = new int[Constants.ROWS][Constants.COLUMNS];

	private int turn = Constants.BLACK_PLAYER;

	private boolean gameOver = false;

	private int winner;

	private List<Move> moves;

	public PlayBoard() {
		emptyBoard();
		moves = new ArrayList<Move>();
		gameOver = false;
		winner = 0;
		turn = Constants.BLACK_PLAYER;
	}

	public PlayBoard(int[][] board) {
		this();
		for (int r = 0; r < Constants.ROWS; r++) {
			for (int c = 0; c < Constants.COLUMNS; c++) {
				this.board[r][c] = board[r][c];
			}
		}
	}

	public PlayBoard(Board board) {
		this();
		// use a deep copy here?
		int[][] values = board.getBoard();
		for (int r = 0; r < Constants.ROWS; r++) {
			for (int c = 0; c < Constants.COLUMNS; c++) {
				this.board[r][c] = values[r][c];
			}
		}
	}

	public void setBoard(int[][] board) {
		this.board = board;
	}

	public int[][] getBoard() {
		return board;
	}

	public void setValue(int r, int c, int value) {
		board[r][c] = value;
	}

	private void emptyBoard() {
		for (int r = 0; r < Constants.ROWS; r++) {
			for (int c = 0; c < Constants.COLUMNS; c++) {
				board[r][c] = 0;
			}
		}
	}

	// @formatter:off
	//   0 1 2 3 4 5 6 7
	// ==================
	// | 0 2 0 2 0 2 0 2 | 0
	// | 2 0 2 0 2 0 2 0 | 1
	// | 0 2 0 2 0 2 0 2 | 2
	// | 0 0 0 0 0 0 0 0 | 3
	// | 0 0 0 0 0 0 0 0 | 4
	// | 1 0 1 0 1 0 1 0 | 5
	// | 0 1 0 1 0 1 0 1 | 6
	// | 1 0 1 0 1 0 1 0 | 7
	// ===================
	// @formatter:on

	public void initBoard() {
		// set to initial state
		boolean rowEven;
		boolean colEven;
		int whichChecker;
		int val;

		for (int r = 0; r < Constants.ROWS; r++) {
			for (int c = 0; c < Constants.COLUMNS; c++) {
				rowEven = r == 0 || (r % 2) == 0;
				colEven = c == 0 || (c % 2) == 0;
				val = Constants.EMPTY;
				whichChecker = Constants.EMPTY;

				if (r < 3) {
					whichChecker = Constants.BLACK_CHECKER;
				} else if (r >= 5) {
					whichChecker = Constants.RED_CHECKER;
				}

				if (r < 3 || r >= 5) {
					if (rowEven) {
						val = colEven ? Constants.EMPTY : whichChecker;
					} else {
						val = !colEven ? Constants.EMPTY : whichChecker;
					}
				}
				board[r][c] = val;
			}
		}
	}

	/**
	 * Returns the piece at the given row and column.
	 * 
	 * If the piece doesn't exist returns -1;
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public int getPiece(int row, int col) {
		if (row < 0 || row >= Constants.ROWS || col < 0 || col >= Constants.COLUMNS) {
			return -1;
		}
		return board[row][col];
	}

	public String printBoard() {
		StringBuilder sb = new StringBuilder();
		for (int r = 0; r < Constants.ROWS; r++) {
			for (int c = 0; c < Constants.COLUMNS; c++) {
				sb.append(board[r][c] + " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public List<Move> getMoves() {
		return moves;
	}

	public void setMoves(List<Move> moves) {
		this.moves = moves;
	}

	public void addMove(Move move) {
		this.moves.add(move);
	}

	public int getWinner() {
		return winner;
	}

	public void setWinner(int winner) {
		this.winner = winner;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

}
