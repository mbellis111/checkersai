package tests;

import game.AI;
import game.Board;
import game.Constants;
import game.PlayBoard;

public class Tester {
	public static void main(String[] args) {
		int score;
		Board board;
		int[][] data;
		// @formatter:off
		data = new int[][] { 
			//0  1  2  3  4  5  6  7
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 0
			{ 3, 0, 0, 0, 0, 0, 0, 0 }, // 1
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 2
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 3
			{ 0, 0, 0, 3, 0, 0, 0, 0 }, // 4
			{ 0, 0, 0, 0, 2, 0, 0, 0 }, // 5
			{ 0, 0, 0, 0, 0, 2, 0, 0 }, // 6
			{ 0, 0, 0, 0, 0, 0, 0, 0 }  // 7
		};
		// @formatter:on

		board = new PlayBoard(data);
		score = AI.evaluateBoard(board, Constants.RED_PLAYER, true);
		// score = AI.calculateEndGameScore(board, 0, 2, 2, 0, Constants.RED_PLAYER);
		System.out.println(score);
	}
}
