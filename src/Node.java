import java.util.ArrayList;
import java.util.List;

public class Node {

	Board board;
	private Move move;
	double value;

	private Node parent;
	private List<Node> children;

	public Node() {
		value = 0;
		children = new ArrayList<Node>();
		parent = null;
	}

//	public Node(Node node) {
//		this();
//		this.setParent(parent);
//	}

	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

	public void addChild(Node child) {
		this.children.add(child);
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	public Move getMove() {
		return move;
	}

	public void setMove(Move move) {
		this.move = move;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

}
