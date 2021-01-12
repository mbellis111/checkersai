public interface Board {

	public int getPiece(int row, int col);

	public int[][] getBoard();

	public void setValue(int row, int col, int value);

	public int getTurn();

	public void setTurn(int turn);

}
