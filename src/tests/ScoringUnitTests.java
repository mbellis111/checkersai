package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import game.AI;
import game.Board;
import game.Constants;
import game.PlayBoard;

public class ScoringUnitTests {

	@Test
	void testMaterialScore() {
		int score;

		score = AI.calculateMaterialScore(1, 0, 0, 0, Constants.RED_PLAYER);
		Assertions.assertTrue(score == 900, "Material score did not match expected!");

		score = AI.calculateMaterialScore(2, 0, 1, 0, Constants.RED_PLAYER);
		Assertions.assertTrue(score == 60, "Material score did not match expected!");

		score = AI.calculateMaterialScore(0, 0, 1, 0, Constants.RED_PLAYER);
		Assertions.assertTrue(score == -900, "Material score did not match expected!");

		score = AI.calculateMaterialScore(5, 1, 4, 1, Constants.RED_PLAYER);
		Assertions.assertTrue(score == 35, "Material score did not match expected!");

		score = AI.calculateMaterialScore(5, 1, 4, 1, Constants.BLACK_PLAYER);
		Assertions.assertTrue(score == -35, "Material score did not match expected!");
	}

	@Test
	void testPositionScore() {
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

		int score;

		// should only be based on distance
		score = AI.getPositionScore(2, 7, Constants.BLACK_PLAYER);
		Assertions.assertTrue(score == 5, "Position score did not match expected!");

		// should only be based on distance
		score = AI.getPositionScore(2, 7, Constants.RED_PLAYER);
		Assertions.assertTrue(score == 2, "Position score did not match expected!");

		// a center piece + distance score (4 + 3)
		score = AI.getPositionScore(3, 4, Constants.RED_PLAYER);
		Assertions.assertTrue(score == 7, "Position score did not match expected!");

		// a center piece + distance score (4 + 4)
		score = AI.getPositionScore(3, 4, Constants.BLACK_PLAYER);
		Assertions.assertTrue(score == 8, "Position score did not match expected!");

		// a goalie piece
		score = AI.getPositionScore(7, 0, Constants.BLACK_PLAYER);
		Assertions.assertTrue(score == 8, "Position score did not match expected!");

		// double corner goalie (8 + 4)
		score = AI.getPositionScore(7, 6, Constants.BLACK_PLAYER);
		Assertions.assertTrue(score == 12, "Position score did not match expected!");

		score = AI.getPositionScore(0, 1, Constants.RED_PLAYER);
		Assertions.assertTrue(score == 12, "Position score did not match expected!");

		// double corner piece
		score = AI.getPositionScore(6, 7, Constants.BLACK_PLAYER);
		Assertions.assertTrue(score == 5, "Position score did not match expected!");
	}

	@Test
	void testEndGameScore() {

		int score;
		Board board;
		int[][] data;

		// @formatter:off
		data = new int[][] { 
			//0  1  2  3  4  5  6  7
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 0
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 1
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 2
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 3
			{ 0, 0, 0, 2, 0, 0, 0, 0 }, // 4
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 5
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 6
			{ 3, 0, 0, 0, 0, 0, 0, 0 }  // 7
		};
		// @formatter:on

		board = new PlayBoard(data);

		// distance of 4 * factor = -20
		score = AI.calculateEndGameScore(board, 0, 1, 1, 0, Constants.RED_PLAYER);
		System.out.println(score);
		Assertions.assertTrue(score == -20, "Distance score did not match expected!");

		// @formatter:off
		data = new int[][] { 
			//0  1  2  3  4  5  6  7
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 0
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 1
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 2
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 3
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 4
			{ 2, 0, 0, 0, 0, 0, 0, 0 }, // 5
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 6
			{ 3, 0, 2, 0, 0, 0, 0, 0 }  // 7
		};
		// @formatter:on

		board = new PlayBoard(data);

		// distance of 2 * factor = -10
		score = AI.calculateEndGameScore(board, 0, 1, 2, 0, Constants.RED_PLAYER);
		System.out.println(score);
		Assertions.assertTrue(score == -20, "Distance score did not match expected!");

		// test that we only check for red if we have more kings
		score = AI.calculateEndGameScore(board, 0, 1, 0, 2, Constants.RED_PLAYER);
		System.out.println(score);
		Assertions.assertTrue(score == 0, "Distance score did not match expected!");

		// test that we only check score if the enemy has less than 5 pieces
		score = AI.calculateEndGameScore(board, 0, 4, 6, 0, Constants.RED_PLAYER);
		System.out.println(score);
		Assertions.assertTrue(score == 0, "Distance score did not match expected!");

		// @formatter:off
		data = new int[][] { 
			//0  1  2  3  4  5  6  7
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 0
			{ 0, 0, 0, 0, 0, 3, 0, 0 }, // 1
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 2
			{ 0, 0, 4, 0, 0, 0, 0, 0 }, // 3
			{ 0, 0, 0, 3, 0, 0, 0, 0 }, // 4
			{ 0, 0, 0, 0, 2, 0, 0, 0 }, // 5
			{ 0, 2, 0, 0, 0, 0, 0, 0 }, // 6
			{ 0, 0, 0, 0, 0, 0, 0, 0 }  // 7
		};
		// @formatter:on

		board = new PlayBoard(data);
		score = AI.calculateEndGameScore(board, 0, 2, 2, 1, Constants.RED_PLAYER);
		System.out.println(score);
		Assertions.assertTrue(score == -100, "Distance score did not match expected!");
	}

}
