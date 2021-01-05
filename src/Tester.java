import java.util.Scanner;

public class Tester {
	public static void main(String[] args) {

		// @formatter:off
		int[][] data = new int[][] { 
			//0  1  2  3  4  5  6  7
			{ 2, 0, 0, 0, 0, 0, 0, 0 }, // 0
			{ 0, 1, 0, 0, 0, 2, 0, 0 }, // 1
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 2
			{ 0, 0, 0, 2, 0, 0, 0, 0 }, // 3
			{ 0, 0, 1, 0, 0, 0, 0, 0 }, // 4
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 5
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 6
			{ 0, 0, 0, 0, 0, 0, 0, 0 }  // 7
		};
		// @formatter:on

//		// @formatter:off
//		int[][] data = new int[][] { 
//			//0  1  2  3  4  5  6  7
//			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 0
//			{ 0, 3, 0, 0, 0, 0, 0, 0 }, // 1
//			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 2
//			{ 0, 0, 0, 4, 0, 0, 0, 0 }, // 3
//			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 4
//			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 5
//			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 6
//			{ 0, 0, 0, 0, 0, 0, 0, 0 }  // 7
//		};
//		// @formatter:on

		int testDepth = 10;
		Board board = new Board(data);
		board.setTurn(Constants.RED_PLAYER);
		GameNode node;

		System.out.println(board.printBoard());

		Scanner scanner = new Scanner(System.in);
		boolean keepGoing = true;
		while (keepGoing) {
			System.out.println("\nPress Any Key (Empty to Exit)");
			String text = scanner.nextLine();
			if (text == null || text.isEmpty()) {
				keepGoing = false;
				break;
			}

			node = AI.minimax(board, new GameNode(), testDepth, board.getTurn(), true, Integer.MIN_VALUE, Integer.MAX_VALUE);
			System.out.println(node.getValue());
			System.out.println(node.getMove());
			board = Game.makeMove(board, node.getMove());
			System.out.println(board.printBoard());

			if (node == null || node.getMove() == null) {
				keepGoing = false;
				break;
			}
		}
		scanner.close();
		System.out.println("Done!");
	}
}
