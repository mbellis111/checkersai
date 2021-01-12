
public class Node {

	Move move;
	int value;

	public Node() {
		move = null;
	}

	public Node(int value) {
		this.move = null;
		this.value = value;
	}

	public Node(Move move) {
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

	public static Node max(Node a, Node b) {
		if (a.getValue() >= b.getValue()) {
			return a;
		}
		return b;
	}

	public static Node min(Node a, Node b) {
		if (b.getValue() <= a.getValue()) {
			return b;
		}
		return a;
	}

}
