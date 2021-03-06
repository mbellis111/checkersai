package game;

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

		// check draw
		if (board.getMovesSinceJump() >= Constants.DRAW_MOVES_WITHOUT_CAPTURE) {
			Node node = new Node();
			node.setValue(0);
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

			Board newBoard = new Board(board);

			// we can probably disable validation now that the jump rules has been chosen
			newBoard = Game.makeMove(newBoard, move, false);

			Node result;
			if (newBoard.getTurn() == player) {
				result = minimax(newBoard, depth - 1, player, maxPlayer, alpha, beta);
			} else {
				result = minimax(newBoard, depth - 1, Game.getOtherPlayer(player), !maxPlayer, alpha, beta);
			}
			result.setMove(move);

			if (depth == Constants.PLIES) {
				System.out.println(move + " : " + result.getValue());
			}

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

	public static int getPositionScore(int row, int col, int player) {

		// all of these only apply to non-kings

		int positionScore = 0;

		// @formatter:off
		//   0 1 2 3 4 5 6 7
		// ==================
		// | 0 2 0 2 0 2 0 2 | 0
		// | 2 0 2 0 2 0 2 0 | 1
		// | 0 2 0 2 0 2 0 2 | 2
		// | 0 0 0 0 0 0 0 0 | 3
		// | 0 0 0 0 0 0 0 0 | 4
		// | 1 0 1 0 1 0 1 0 | 5
		// | 0 1 0 1 0 1 0 1 | 6
		// | 1 0 1 0 1 0 1 0 | 7
		// ===================
		
		// @formatter:off
		//   00 01 02 03 04 05 06 07
		// ===========================
		// | -- 31 -- 30 -- 29 -- 28 | 0
		// | 27 -- 26 -- 25 -- 24 -- | 1
		// | -- 23 -- 22 -- 21 -- 20 | 2
		// | 19 -- 18 -- 17 -- 16 -- | 3
		// | -- 15 -- 14 -- 13 -- 12 | 4
		// | 11 -- 10 -- 09 -- 08 -- | 5
		// | -- 07 -- 06 -- 05 -- 04 | 6
		// | 03 -- 02 -- 01 -- 00 -- | 7
		// ===========================
		// @formatter:on

		// add value for controlling the middle squares
		if (row == 3 && (col == 2 || col == 4)) {
			positionScore += Constants.PIECE_MIDDLE_CENTER_SQUARE_VALUE;
		} else if (row == 4 && (col == 3 || col == 5)) {
			positionScore += Constants.PIECE_MIDDLE_CENTER_SQUARE_VALUE;
		} else if (row == 3 && (col == 0 || col == 6)) {
			positionScore += Constants.PIECE_MIDDLE_SIDE_SQUARE_VALUE;
		} else if (row == 4 && (col == 1 || col == 7)) {
			positionScore += Constants.PIECE_MIDDLE_SIDE_SQUARE_VALUE;
		}

		// add values for goalies
		if (player == Constants.RED_PLAYER) {
			if (row == 0 && (col == 7 || col == 1)) {
				positionScore += Constants.PIECE_SIDE_GOALIES_VALUE;
			}
			if (row == 0 && (col == 3 || col == 5)) {
				positionScore += Constants.PIECE_CENTER_GOALIE_VALUE;
			}
			if ((row == 0 && col == 1) || (row == 1 && col == 0)) {
				positionScore += Constants.PIECE_DOUBLE_CORNER_VALUE;
			}
		} else if (player == Constants.BLACK_PLAYER) {
			if (row == 7 && (col == 6 || col == 0)) {
				positionScore += Constants.PIECE_SIDE_GOALIES_VALUE;
			}
			if (row == 7 && (col == 2 || col == 4)) {
				positionScore += Constants.PIECE_CENTER_GOALIE_VALUE;
			}
			if ((row == 7 && col == 6) || (row == 6 && col == 7)) {
				positionScore += Constants.PIECE_DOUBLE_CORNER_VALUE;
			}
		}

		// add value for how close to opponent home row
		if (player == Constants.RED_PLAYER) {
			positionScore += row * Constants.PIECE_ROW_ADVANCE_VALUE;
		} else if (player == Constants.BLACK_PLAYER) {
			positionScore += (7 - row) * Constants.PIECE_ROW_ADVANCE_VALUE;
		}

		return positionScore;
	}

	public static int evaluateBoard(Board board, int player, boolean maxPlayer) {

		int redCheckers = 0;
		int redKings = 0;
		int blackCheckers = 0;
		int blackKings = 0;

		int totalScore = 0;
		int positionScore = 0;

		// check number of pieces
		for (int r = 0; r < Constants.ROWS; r++) {
			for (int c = 0; c < Constants.COLUMNS; c++) {
				int piece = board.getPiece(r, c);

				// add up the materials
				if (piece == Constants.RED_CHECKER) {
					redCheckers++;
				} else if (piece == Constants.RED_KING) {
					redKings++;
				} else if (piece == Constants.BLACK_CHECKER) {
					blackCheckers++;
				} else if (piece == Constants.BLACK_KING) {
					blackKings++;
				}

				// if its not a king, calculate the position score
				if (piece != Constants.EMPTY && !Game.isKing(piece)) {
					if (Game.isOwner(piece, player)) {
						positionScore += getPositionScore(r, c, player);
					} else {
						positionScore -= getPositionScore(r, c, player);
					}
				}

			}
		}

		// calculate the score from the pieces on the board
		int materialScore = calculateMaterialScore(redCheckers, redKings, blackCheckers, blackKings, player);
		totalScore += materialScore;

		// add the position score to the total
		totalScore += positionScore;

		// add in 'end-game' which attempts to minimize distance between pieces to go for a kill
		// this is assuming you have more pieces than the opponent
		int endGameScore = calculateEndGameScore(board, redCheckers, redKings, blackCheckers, blackKings, player);
		totalScore += endGameScore;

		if (maxPlayer) {
			return totalScore;
		} else {
			return -totalScore;
		}
	}

	public static int calculateEndGameScore(Board board, int redCheckers, int redKings, int blackCheckers, int blackKings, int player) {

		int moreKings = player == Constants.RED_PLAYER ? redKings - blackKings : blackKings - redKings;
		int enemyPieces = player == Constants.RED_PLAYER ? blackCheckers + blackKings : redCheckers + redKings;

		boolean endGame = moreKings > 0 && enemyPieces < 5;
		if (!endGame) {
			return 0;
		}

		double distance = 0;
		// find the distance between your kings and any enemy pieces
		for (int r = 0; r < Constants.ROWS; r++) {
			for (int c = 0; c < Constants.COLUMNS; c++) {
				int piece = board.getPiece(r, c);
				int owner = Game.getOwner(piece);

				if (!Game.isKing(piece) || owner != player) {
					continue;
				}

				// find distance between this and all enemy pieces
				for (int rr = 0; rr < Constants.ROWS; rr++) {
					for (int cc = 0; cc < Constants.COLUMNS; cc++) {
						int otherPiece = board.getPiece(rr, cc);
						int otherOwner = Game.getOwner(otherPiece);
						if (otherPiece == Constants.EMPTY || otherOwner == player) {
							continue;
						}
						distance += distanceFormula(r, c, rr, cc);
					}
				}
			}
		}

		int score = (int) Math.round(distance);

		return -score * Constants.DIST_FACTOR_VALUE;
	}

	private static double distanceFormula(int x1, int y1, int x2, int y2) {
		int xDist = (x2 - x1) * (x2 - x1);
		int yDist = (y2 - y1) * (y2 - y1);
		if (xDist + yDist > 0) {
			return Math.sqrt(xDist + yDist);
		}
		return 0;
	}

	public static int calculateMaterialScore(int redCheckers, int redKings, int blackCheckers, int blackKings, int player) {
		int score = 0;

		int redMaterial = (redCheckers * Constants.CHECKER_VALUE) + (redKings * Constants.KING_VALUE);
		int blackMaterial = (blackCheckers * Constants.CHECKER_VALUE) + (blackKings * Constants.KING_VALUE);

		if (redMaterial > blackMaterial) {
			score = Math.round((redMaterial - blackMaterial) * ((float) redMaterial / (blackMaterial == 0 ? 1 : blackMaterial)));
		} else if (blackMaterial >= redMaterial) {
			score = Math.round((redMaterial - blackMaterial) * ((float) blackMaterial / (redMaterial == 0 ? 1 : redMaterial)));
		}

		if (player == Constants.RED_PLAYER) {
			if (blackMaterial == 0) {
				return Integer.MAX_VALUE;
			}
			return score;
		} else {
			if (redMaterial == 0) {
				return Integer.MIN_VALUE;
			}
			return -score;
		}
	}

}
