import java.util.List;

public class AI {

	public static Node minimax(Board board, int depth, int player, boolean maxPlayer, int alpha, int beta) {
		// base-case
		if (depth <= 0) {
			int score = AI.evaluateBoard(board, player, maxPlayer);
			Node node = new Node(score);
			return node;
		}

		// check game over
		List<Move> moves = Game.getAvailableMoves(board, player);
		if (moves.isEmpty()) {
			// chose the depth
			Node node = new Node();
			if (maxPlayer) {
				node.setValue(Integer.MIN_VALUE + (Constants.PLIES - depth));
			} else {
				node.setValue(Integer.MAX_VALUE - (Constants.PLIES - depth));
			}
			return node;
		}

		Node selectedNode = new Node();
		selectedNode.setMove(moves.get(0));
		if (maxPlayer) {
			selectedNode.setValue(Integer.MIN_VALUE);
		} else {
			selectedNode.setValue(Integer.MAX_VALUE);
		}
		for (Move move : moves) {

			Board newBoard = new AIBoard(board);

			// we can probably disable validation now that the jump rules has been chosen
			newBoard = Game.makeMove(newBoard, move, false);

			Node result;
			if (newBoard.getTurn() == player) {
				result = minimax(newBoard, depth - 1, player, maxPlayer, alpha, beta);
			} else {
				result = minimax(newBoard, depth - 1, Game.getOtherPlayer(player), !maxPlayer, alpha, beta);
			}
			result.setMove(move);

//			if (depth == Constants.PLIES) {
//				System.out.println(move + " : " + result.getValue());
//			}

			if (maxPlayer) {
				// maximize
				selectedNode = Node.max(selectedNode, result);
				alpha = Math.max(alpha, selectedNode.getValue());
				if (alpha >= beta) {
					break;
				}
			} else {
				// minimize
				selectedNode = Node.min(selectedNode, result);
				beta = Math.min(beta, selectedNode.getValue());
				if (beta <= alpha) {
					break;
				}
			}
		}

		return selectedNode;
	}

	public static int evaluateBoard(Board board, int player, boolean maxPlayer) {
		int redCheckers = 0;
		int redKings = 0;
		int blackCheckers = 0;
		int blackKings = 0;

		for (int r = 0; r < Constants.ROWS; r++) {
			for (int c = 0; c < Constants.COLUMNS; c++) {
				int piece = board.getPiece(r, c);
				if (piece == Constants.RED_CHECKER) {
					redCheckers++;
				} else if (piece == Constants.RED_KING) {
					redKings++;
				} else if (piece == Constants.BLACK_CHECKER) {
					blackCheckers++;
				} else if (piece == Constants.BLACK_KING) {
					blackKings++;
				}
			}
		}

		int redScore = redCheckers + (redKings * 2);
		int blackScore = blackCheckers + (blackKings * 2);

		if (maxPlayer) {
			if (player == Constants.BLACK_PLAYER) {
				return blackScore - redScore;
			} else {
				return redScore - blackScore;
			}
		} else {
			if (player == Constants.RED_PLAYER) {
				return blackScore - redScore;
			} else {
				return redScore - blackScore;
			}
		}
	}

}
