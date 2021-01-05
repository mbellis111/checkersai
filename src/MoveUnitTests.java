import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MoveUnitTests {

	@Test
	void testBadPiece() {
		// @formatter:off
		int[][] data = new int[][] { 
			//0  1  2  3  4  5  6  7
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 0
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 1
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 2
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 3
			{ 0, 2, 0, 2, 0, 1, 0, 0 }, // 4
			{ 0, 0, 1, 0, 1, 0, 1, 0 }, // 5
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 6
			{ 0, 0, 0, 0, 0, 0, 0, 0 }  // 7
		};
		// @formatter:on
		Board board = new Board(data);
		Move move;

		// a piece that doesn't exist
		move = new Move(Constants.RED_PLAYER, 0, 0, 0, 0);
		Assertions.assertFalse(Game.validateMove(board, move), "Piece should not exist");

		// a piece that isn't ours
		move = new Move(Constants.RED_PLAYER, 4, 1, 5, 0);
		Assertions.assertFalse(Game.validateMove(board, move), "Piece should not be players");

		// a piece not on the board
		move = new Move(Constants.RED_PLAYER, -1, -1, 5, 0);
		Assertions.assertFalse(Game.validateMove(board, move), "Piece should not be players");
	}

	@Test
	void testMovesToEmpySpaces() {
		// @formatter:off
		int[][] data = new int[][] { 
			//0  1  2  3  4  5  6  7
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 0
			{ 0, 0, 0, 0, 3, 0, 0, 0 }, // 1
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 2
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 3
			{ 0, 0, 0, 1, 0, 1, 0, 0 }, // 4
			{ 0, 0, 1, 0, 1, 0, 1, 0 }, // 5
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 6
			{ 0, 0, 0, 0, 0, 0, 0, 0 }  // 7
		};
		// @formatter:on

		Board board = new Board(data);
		Move move;

		// now we want to assert a few easy checks

		// a move in the proper direction to an empty space
		move = new Move(Constants.RED_PLAYER, 5, 6, 4, 7);
		Assertions.assertTrue(Game.validateMove(board, move), "Space should be empty!");

		// a move that is only vertical
		move = new Move(Constants.RED_PLAYER, 5, 6, 4, 6);
		Assertions.assertFalse(Game.validateMove(board, move), "Move should fail due to only moving vertically!");

		// a king that is only moving vertically
		move = new Move(Constants.RED_PLAYER, 1, 4, 0, 4);
		Assertions.assertFalse(Game.validateMove(board, move), "Move should fail due to king only moving verically!");

		// a move that is only horizontal
		move = new Move(Constants.RED_PLAYER, 5, 6, 5, 7);
		Assertions.assertFalse(Game.validateMove(board, move), "Move should fail due to only moving horizontally!");

		// a king that is only moving horizontal
		move = new Move(Constants.RED_PLAYER, 1, 4, 1, 5);
		Assertions.assertFalse(Game.validateMove(board, move), "Move should fail due to king only moving horizontally!");

		// a diagonal move in the wrong direction
		move = new Move(Constants.RED_PLAYER, 5, 6, 6, 7);
		Assertions.assertFalse(Game.validateMove(board, move), "Piece is moving in the wrong direction!");

		// a king moving diagonally in the wrong direction
		move = new Move(Constants.RED_PLAYER, 1, 4, 2, 5);
		Assertions.assertTrue(Game.validateMove(board, move), "A king should be able to move in either vertical!");

		// a move that is too far away
		move = new Move(Constants.RED_PLAYER, 4, 5, 2, 7);
		Assertions.assertFalse(Game.validateMove(board, move), "The move should be too far away!");
	}

	@Test
	void testBlockedMoves() {
		// @formatter:off
		int[][] data = new int[][] { 
			//0  1  2  3  4  5  6  7
			{ 0, 0, 0, 0, 0, 0, 0, 2 }, // 0
			{ 0, 0, 0, 0, 3, 0, 2, 0 }, // 1
			{ 0, 0, 0, 1, 0, 1, 0, 0 }, // 2
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 3
			{ 0, 0, 0, 2, 0, 1, 0, 0 }, // 4
			{ 0, 0, 1, 0, 1, 0, 1, 0 }, // 5
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 6
			{ 0, 0, 0, 0, 0, 0, 0, 1 }  // 7
		};
		// @formatter:on

		Board board = new Board(data);
		Move move;

		// a move in the proper direction to a space taken by the same player
		move = new Move(Constants.RED_PLAYER, 5, 4, 4, 5);
		Assertions.assertFalse(Game.validateMove(board, move), "Space should be blocked by player!");

		// a move that is out of bounds
		move = new Move(Constants.RED_PLAYER, 7, 7, 6, 8);
		Assertions.assertFalse(Game.validateMove(board, move), "Move should be out of bounds!");

		// a move that is blocked by the same player king
		move = new Move(Constants.RED_PLAYER, 2, 3, 1, 4);
		Assertions.assertFalse(Game.validateMove(board, move), "Move should be blocked by player king!");
	}

	@Test
	void testValidJumps() {
		// @formatter:off
		int[][] data = new int[][] { 
			//0  1  2  3  4  5  6  7
			{ 2, 0, 0, 0, 0, 0, 0, 2 }, // 0
			{ 0, 1, 0, 0, 3, 0, 2, 0 }, // 1
			{ 0, 0, 3, 1, 0, 1, 0, 0 }, // 2
			{ 0, 4, 0, 0, 0, 0, 0, 0 }, // 3
			{ 0, 0, 0, 2, 0, 1, 0, 0 }, // 4
			{ 0, 0, 1, 0, 1, 0, 1, 0 }, // 5
			{ 0, 1, 0, 0, 0, 0, 0, 0 }, // 6
			{ 0, 0, 0, 0, 0, 0, 0, 1 }  // 7
		};
		// @formatter:on

		Board board = new Board(data);
		Move move;

		// a valid jump with an open space
		move = new Move(Constants.BLACK_PLAYER, 4, 3, 5, 4);
		Assertions.assertTrue(Game.validateMove(board, move), "Should be a valid jump!");
		move = new Move(Constants.RED_PLAYER, 5, 2, 4, 3);
		Assertions.assertTrue(Game.validateMove(board, move), "Should be a valid jump!");

		// a jump made by a king over a king
		move = new Move(Constants.RED_PLAYER, 2, 2, 3, 1);
		Assertions.assertTrue(Game.validateMove(board, move), "Should be a valid jump!");
	}

	@Test
	void testInvalidJumps() {
		// @formatter:off
		int[][] data = new int[][] { 
			//0  1  2  3  4  5  6  7
			{ 2, 0, 0, 0, 0, 0, 0, 0 }, // 0
			{ 0, 1, 0, 0, 0, 0, 0, 0 }, // 1
			{ 0, 0, 0, 0, 0, 0, 1, 0 }, // 2
			{ 0, 0, 0, 0, 0, 1, 0, 0 }, // 3
			{ 0, 0, 0, 2, 0, 0, 0, 0 }, // 4
			{ 0, 0, 1, 0, 0, 0, 0, 0 }, // 5
			{ 0, 1, 0, 0, 0, 0, 0, 0 }, // 6
			{ 0, 0, 0, 0, 0, 0, 0, 0 }  // 7
		};
		// @formatter:on

		Board board = new Board(data);
		Move move;

		// a jump blocked by two enemies
		move = new Move(Constants.BLACK_PLAYER, 4, 3, 5, 2);
		Assertions.assertFalse(Game.validateMove(board, move), "Jump should be blocked by two enemies!");

		// a jump blocked by a wall
		move = new Move(Constants.RED_PLAYER, 1, 1, 0, 0);
		Assertions.assertFalse(Game.validateMove(board, move), "Jump should be blocked by edge!");

		// jump over own piece
		move = new Move(Constants.RED_PLAYER, 3, 5, 2, 6);
		Assertions.assertFalse(Game.validateMove(board, move), "Jump shouldn't be possible over own piece!");
	}

//	@Test
//	void testGettingAvailableMoves() {
//		// @formatter:off
//		int[][] data = new int[][] { 
//			//0  1  2  3  4  5  6  7
//			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 0
//			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 1
//			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 2
//			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 3
//			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 4
//			{ 0, 0, 0, 2, 0, 0, 0, 0 }, // 5
//			{ 0, 0, 1, 0, 0, 1, 0, 0 }, // 6
//			{ 0, 0, 0, 0, 0, 0, 0, 0 }  // 7
//		};
//		// @formatter:on
//
//		Board board = new Board(data);
//
//		// find all moves from the board
//
//	}

	@Test
	void testMoveForcedJump() {
		// @formatter:off
		int[][] data = new int[][] { 
			//0  1  2  3  4  5  6  7
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 0
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 1
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 2
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 3
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 4
			{ 0, 2, 0, 2, 0, 0, 0, 0 }, // 5
			{ 1, 0, 1, 0, 0, 1, 0, 0 }, // 6
			{ 0, 0, 0, 0, 0, 0, 0, 0 }  // 7
		};
		// @formatter:on

		Board board = new Board(data);
		Move move;

		// test trying to make a move when there is a forced jump that must be taken
		move = new Move(Constants.RED_PLAYER, 6, 5, 5, 6);
		Assertions.assertFalse(Game.validateMove(board, move), "There is a forced jump that must be taken!");

		// test taking the forced jump when another is available
		move = new Move(Constants.RED_PLAYER, 6, 2, 5, 3);
		Assertions.assertTrue(Game.validateMove(board, move), "The forced jump should be allowed!");

	}

}
