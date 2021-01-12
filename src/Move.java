
public class Move {
	private int player;
	private int startRow;
	private int startCol;
	private int endRow;
	private int endCol;
	private int jumpEndRow;
	private int jumpEndCol;
	private boolean isValidJump = false;
	private boolean isValidMove = false;
	private boolean isValid = false;
	private String errorMessage;
	private String moveError;
	private String jumpError;

	/**
	 * Creates a new potential move
	 * 
	 * @param player   - the player making the move
	 * @param startRow - the row of the piece starting the move
	 * @param startCol - the col of the piece starting the move
	 * @param endRow   - the row to move the piece to
	 * @param endCol   - the col to move the piece to
	 */
	public Move(int player, int startRow, int startCol, int endRow, int endCol) {
		this.player = player;
		this.startRow = startRow;
		this.startCol = startCol;
		this.endRow = endRow;
		this.endCol = endCol;
		this.errorMessage = "";
		this.moveError = "";
		this.jumpError = "";
	}

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getStartCol() {
		return startCol;
	}

	public void setStartCol(int startCol) {
		this.startCol = startCol;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public int getEndCol() {
		return endCol;
	}

	public void setEndCol(int endCol) {
		this.endCol = endCol;
	}

	public boolean getIsValidMove() {
		return isValidMove;
	}

	public void setIsValidMove(boolean isValidMove) {
		this.isValidMove = isValidMove;
	}

	public boolean getIsValidJump() {
		return isValidJump;
	}

	public void setIsValidJump(boolean isValidJump) {
		this.isValidJump = isValidJump;
	}

	public boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(boolean isValid) {
		this.isValid = isValid;
	}

	public int getJumpEndRow() {
		return jumpEndRow;
	}

	public void setJumpEndRow(int jumpEndRow) {
		this.jumpEndRow = jumpEndRow;
	}

	public int getJumpEndCol() {
		return jumpEndCol;
	}

	public void setJumpEndCol(int jumpEndCol) {
		this.jumpEndCol = jumpEndCol;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean hasErrorMessage() {
		return errorMessage != null && errorMessage.length() > 0;
	}

	public String getMoveError() {
		return moveError;
	}

	public void setMoveError(String moveError) {
		this.moveError = moveError;
	}

	public String getJumpError() {
		return jumpError;
	}

	public void setJumpError(String jumpError) {
		this.jumpError = jumpError;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (int) player;
		hash = 31 * hash + (int) startRow;
		hash = 31 * hash + (int) startCol;
		hash = 31 * hash + (int) endRow;
		hash = 31 * hash + (int) endCol;
		return hash;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || getClass() != other.getClass()) {
			return false;
		}
		Move otherMove = (Move) other;
		return getPlayer() == otherMove.getPlayer() && getStartRow() == otherMove.getStartRow() && getStartCol() == otherMove.getStartCol() && getEndCol() == otherMove.getEndCol()
				&& getEndRow() == otherMove.getEndRow();
	}

	public String toString() {
		if (this.getIsValidJump()) {
			return "P" + player + ": (" + startRow + "," + startCol + ") -> (" + jumpEndRow + "," + jumpEndCol + ")";
		}
		return "P" + player + ": (" + startRow + "," + startCol + ") -> (" + endRow + "," + endCol + ")";
	}

}
