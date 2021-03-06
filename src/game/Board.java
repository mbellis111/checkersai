package game;

public class Board {

	// java (mostly) uses word boundaries so unless on an embedded device just use an int instead
	// we will track using ROWS, then COLUMNS
	private int[][] board;
	private int turn;

	public Board() {
		this.board = new int[Constants.ROWS][Constants.COLUMNS];
	}

	public Board(int[][] values) {
		this.board = new int[Constants.ROWS][Constants.COLUMNS];
		setBoardCopy(values);
	}

	public Board(Board board) {
		this(board.getBoard());
	}

	public int getPiece(int row, int col) {
		return board[row][col];
	}

	public int[][] getBoard() {
		return board;
	}

	public void setBoardCopy(int[][] values) {
		for (int r = 0; r < Constants.ROWS; r++) {
			for (int c = 0; c < Constants.COLUMNS; c++) {
				this.board[r][c] = values[r][c];
			}
		}
	}

	public void setValue(int row, int col, int value) {
		board[row][col] = value;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

}
