
public class AIBoard implements Board {

	private int[][] board;
	private int turn;

	public AIBoard(int[][] values) {
		this.board = new int[Constants.ROWS][Constants.COLUMNS];
		for (int r = 0; r < Constants.ROWS; r++) {
			for (int c = 0; c < Constants.COLUMNS; c++) {
				this.board[r][c] = values[r][c];
			}
		}
	}

	public AIBoard(Board board) {
		this(board.getBoard());
	}

	@Override
	public int getPiece(int row, int col) {
		return board[row][col];
	}

	@Override
	public int[][] getBoard() {
		return board;
	}

	@Override
	public void setValue(int row, int col, int value) {
		board[row][col] = value;
	}

	@Override
	public int getTurn() {
		return turn;
	}

	@Override
	public void setTurn(int turn) {
		this.turn = turn;
	}

}
