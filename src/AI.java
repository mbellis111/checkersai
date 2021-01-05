import java.util.List;

public class AI {

	public static GameNode minimax(Board board, GameNode currentNode, int depth, int player, boolean maxPlayer, int alpha, int beta) {
		// base-case
		if (depth <= 0) {
			int score = AI.evaluateBoard(board, player, maxPlayer);
			return new GameNode(score);
		}

		// check game over
		List<Move> moves = Game.getAvailableMoves(board, player);
		if (moves.isEmpty()) {
			if (maxPlayer) {
				return new GameNode(Integer.MIN_VALUE);
			} else {
				return new GameNode(Integer.MAX_VALUE);
			}
		}

		GameNode selectedNode = new GameNode();
		int selectedValue;
		if (maxPlayer) {
			selectedValue = Integer.MIN_VALUE;
		} else {
			selectedValue = Integer.MAX_VALUE;
		}

		for (Move move : moves) {
			GameNode node = new GameNode(move);

			Board newBoard = new Board(board);

			// we can probably disable validation now that the jump rules has been chosen
			newBoard = Game.makeMove(newBoard, move, true);
			if (newBoard.getErrorMessage() != null && !newBoard.getErrorMessage().isEmpty()) {
				System.out.println("ERROR");
			}

			GameNode result;
			if (newBoard.getTurn() == player) {
				result = minimax(newBoard, node, depth - 1, player, maxPlayer, alpha, beta);
			} else {
				result = minimax(newBoard, node, depth - 1, Game.getOtherPlayer(player), !maxPlayer, alpha, beta);
			}

//			if (depth == Constants.PLIES) {
//				System.out.println(move + " : " + result.getValue());
//			}

			if (maxPlayer) {
				// maximize
				if (result.getValue() >= selectedValue) {
					selectedValue = result.getValue();
					selectedNode.setMove(move); // chose the first move to return
					selectedNode.setValue(selectedValue);
				}
				if (alpha > selectedValue) {
					alpha = selectedValue;
				}
				if (alpha >= beta) {
					// beta cutoff
					break;
				}

			} else {
				// minimize
				if (result.getValue() <= selectedValue) {
					selectedValue = result.getValue();
					selectedNode.setMove(move); // chose the first move to return
					selectedNode.setValue(selectedValue);
				}
				if (beta < selectedValue) {
					beta = selectedValue;
				}
				if (beta <= alpha) {
					// alpha cutoff
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

//	public static int evaluateBoard(Board board, int player) {
//		int redCheckers = 0;
//		int redKings = 0;
//		int blackCheckers = 0;
//		int blackKings = 0;
//		int score = 0;
//
//		for (int r = 0; r < Constants.ROWS; r++) {
//			for (int c = 0; c < Constants.COLUMNS; c++) {
//				int piece = board.getPiece(r, c);
//				if (piece == Constants.RED_CHECKER) {
//					redCheckers++;
//				} else if (piece == Constants.RED_KING) {
//					redKings++;
//				} else if (piece == Constants.BLACK_CHECKER) {
//					blackCheckers++;
//				} else if (piece == Constants.BLACK_KING) {
//					blackKings++;
//				}
//			}
//		}
//
//		int redScore = redCheckers + (redKings * 2);
//		int blackScore = blackCheckers + (blackKings * 2);
//
//		if (player == Constants.RED_PLAYER) {
//			score = redScore - blackScore;
//			if (redScore > 0 && blackScore == 0) {
//				return Integer.MAX_VALUE;
//			} else if (redScore == 0 && blackScore > 0) {
//				return Integer.MIN_VALUE;
//			}
//		} else if (player == Constants.BLACK_PLAYER) {
//			score = blackScore - redScore;
//			if (blackScore > 0 && redScore == 0) {
//				return Integer.MAX_VALUE;
//			} else if (blackScore == 0 && redScore > 0) {
//				return Integer.MIN_VALUE;
//			}
//		}
//
//		return score;
//	}

}
