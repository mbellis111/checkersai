
public class GameNode {

	Move move;
	int value;

	public GameNode() {
		move = null;
	}

	public GameNode(int value) {
		this.move = null;
		this.value = value;
	}

	public GameNode(Move move) {
		this.move = move;
	}

	public String toString() {
		String ret = "V: " + value;
		if (move != null) {
			ret += ", " + move.toString();
		}
		return ret;
	}

	public Move getMove() {
		return move;
	}

	public void setMove(Move move) {
		this.move = move;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
