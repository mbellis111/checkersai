package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.Instant;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GUI {

	private static JLabel status;
	private static JLabel turnTracker;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private static void createAndShowGUI() {
		// init the game state and board
		BoardUI gameBoard = new BoardUI();
		PlayBoard board = new PlayBoard();

		board.initBoard();

		gameBoard.updateBoard(board);
		board.setTurn(Constants.BLACK_PLAYER);

		// set up the GUI
		JFrame f = new JFrame("Checkers");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(Constants.GRID_SIZE * (Constants.ROWS), Constants.GRID_SIZE * (Constants.COLUMNS));
		f.getContentPane().add(gameBoard, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		turnTracker = new JLabel("Turn: " + Constants.playerToString(board.getTurn()));
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 0.5;
		constraints.anchor = GridBagConstraints.CENTER;
		panel.add(turnTracker, constraints);

		status = new JLabel("Starting Game...");
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.weightx = 0.5;
		constraints.anchor = GridBagConstraints.CENTER;
		panel.add(status, constraints);

		f.getContentPane().add(panel, BorderLayout.SOUTH);

		f.pack();
		f.setVisible(true);
	}

	public static void setStatus(String text) {
		status.setText(text);
	}

	public static void setTurn(String text) {
		turnTracker.setText(text);
	}

}

class Click {
	private int row, col;

	public Click(int x, int y) {
		setRow(y);
		setCol(x);
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	private void setRow(int y) {
		if (y == 0) {
			row = 0;
		} else {
			row = (int) Math.floor(y / Constants.GRID_SIZE);
		}
	}

	private void setCol(int x) {
		if (x == 0) {
			col = 0;
		} else {
			col = (int) Math.floor(x / Constants.GRID_SIZE);
		}
	}

	public String toString() {
		return "(" + row + "," + col + ")";
	}
}

class Player {
	int controller;
	int player;

	public Player(int player, int controller) {
		this.player = player;
		this.controller = controller;
	}

	public int getController() {
		return controller;
	}

	public int getPlayer() {
		return player;
	}

}

class BoardUI extends JPanel {

	private static final long serialVersionUID = 1L;

	private PlayBoard board;
	private Player playerOne = new Player(Constants.BLACK_PLAYER, Constants.COMPUTER);
	private Player playerTwo = new Player(Constants.RED_PLAYER, Constants.COMPUTER);

	private Click firstClick;
	private Click secondClick;

	public BoardUI() {

		board = null;

		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {

				// if the game is over do nothing
				if (board.isGameOver()) {
					return;
				}

				Click click = new Click(e.getX(), e.getY());

				if (firstClick == null) {
					// check if is a computer turn
					if (isComputerTurn(board.getTurn())) {
						makeComputerMove(board);
						// check for a game over
						if (Game.checkGameOver(board, board.getTurn())) {
							gameOver();
							return;
						}
						resetClicks();
					} else {
						firstClick = click;
						secondClick = null;
						int piece = board.getPiece(click.getRow(), click.getCol());
						if (!Game.isOwner(piece, board.getTurn())) {
							resetClicks();
							return;
						}
						GUI.setStatus("Selected " + click.toString());
					}
				} else if (firstClick != null && secondClick == null) {
					secondClick = click;

					if (!isComputerTurn(board.getTurn())) {
						// human move
						Move move = new Move(board.getTurn(), firstClick.getRow(), firstClick.getCol(), secondClick.getRow(), secondClick.getCol());
						Board temp = Game.makeMove(board, move);
						if (move.hasErrorMessage()) {
							GUI.setStatus(move.getErrorMessage());
						} else {
							updateState(temp, move);
						}
						// check for a game over
						if (Game.checkGameOver(board, board.getTurn())) {
							gameOver();
							return;
						}
						resetClicks();
					}
				} else {
					resetClicks();
				}
				repaint();
			}
		});

	}

	private boolean isComputerTurn(int player) {
		return getPlayer(player).getController() == Constants.COMPUTER;
	}

	private void gameOver() {
		int winner = board.getWinner();
		if (winner == Constants.DRAW) {
			GUI.setStatus("The game has ended in a draw!");
			GUI.setTurn("Game Over! ");
		} else {
			GUI.setStatus("Winner: " + Constants.playerToString(board.getWinner()));
			GUI.setTurn("Game Over!");
		}
		resetClicks();
		repaint();
	}

	private Player getPlayer(int player) {
		if (playerOne.getPlayer() == player) {
			return playerOne;
		} else if (playerTwo.getPlayer() == player) {
			return playerTwo;
		}
		return null;
	}

	private void updateState(Board board, Move move) {
		this.board.setBoardCopy(board.getBoard());
		this.board.setTurn(board.getTurn());
		this.board.addMove(move);
		GUI.setStatus(move.toString());
		System.out.println(move);
		GUI.setTurn("Turn: " + Constants.playerToString(this.board.getTurn()));
	}

	private Board makeComputerMove(Board board) {
		Instant start = Instant.now();
		Node node = AI.minimax(board, Constants.PLIES, board.getTurn(), true, Integer.MIN_VALUE, Integer.MAX_VALUE);
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		System.out.println("Computer thought for " + timeElapsed);

		Move move = node.getMove();
		Board temp = Game.makeMove(board, move);
		updateState(temp, move);
		return temp;
	}

	private void resetClicks() {
		firstClick = null;
		secondClick = null;
	}

	public void updateBoard(PlayBoard board) {
		this.board = board;
	}

	public Dimension getPreferredSize() {
		return new Dimension(Constants.GRID_SIZE * (Constants.ROWS), Constants.GRID_SIZE * (Constants.COLUMNS));
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// draw the board
		if (board != null) {
			drawBoard(g);
		}
	}

	private void drawBoard(Graphics g) {
		for (int r = 0; r < Constants.ROWS; r++) {
			for (int c = 0; c < Constants.COLUMNS; c++) {
				g.setColor(Constants.GRID_COLOR);
				g.drawRect(c * Constants.GRID_SIZE, r * Constants.GRID_SIZE, Constants.GRID_SIZE, Constants.GRID_SIZE);

				boolean rowEven = r == 0 || (r % 2) == 0;
				boolean colEven = c == 0 || (c % 2) == 0;

				if (!rowEven && colEven || rowEven && !colEven) {
					g.setColor(Constants.SQUARE_COLOR);
					g.fillRect(c * Constants.GRID_SIZE, r * Constants.GRID_SIZE, Constants.GRID_SIZE, Constants.GRID_SIZE);
				}

				g.setColor(Color.BLUE);
				g.drawString(r + ", " + c, (c * Constants.GRID_SIZE) + 15, (r * Constants.GRID_SIZE) + 15);

				int piece = board.getPiece(r, c);
				if (piece == -1 || piece == 0) {
					continue;
				}

				// otherwise draw checker or king
				Color pieceColor = getColorForPiece(piece);
				g.setColor(pieceColor);
				g.fillOval(c * Constants.GRID_SIZE, r * Constants.GRID_SIZE, Constants.GRID_SIZE, Constants.GRID_SIZE);

				g.setColor(Color.BLUE);
				g.drawString(r + ", " + c, (c * Constants.GRID_SIZE) + 15, (r * Constants.GRID_SIZE) + 15);
			}
		}

		// draw highlights for clicks
		if (firstClick != null) {
			g.setColor(Constants.HIGHLIGHT_COLOR);
			g.drawOval(firstClick.getCol() * Constants.GRID_SIZE, firstClick.getRow() * Constants.GRID_SIZE, Constants.GRID_SIZE, Constants.GRID_SIZE);
		}

		// draw the last move if it happened
		// an arrow from the previous to the current
		// remember that jumps need more
		if (!board.getMoves().isEmpty()) {
			Move move = board.getMoves().get(board.getMoves().size() - 1);
			int sc = (move.getStartCol() * Constants.GRID_SIZE) + Constants.GRID_SIZE / 2;
			int sr = (move.getStartRow() * Constants.GRID_SIZE) + Constants.GRID_SIZE / 2;
			int ec, er;
			if (move.getIsValidJump()) {
				ec = (move.getJumpEndCol() * Constants.GRID_SIZE) + Constants.GRID_SIZE / 2;
				er = (move.getJumpEndRow() * Constants.GRID_SIZE) + Constants.GRID_SIZE / 2;
			} else {
				ec = (move.getEndCol() * Constants.GRID_SIZE) + Constants.GRID_SIZE / 2;
				er = (move.getEndRow() * Constants.GRID_SIZE) + Constants.GRID_SIZE / 2;
			}
			g.setColor(Constants.LINE_COLOR);
			g.drawLine(sc, sr, ec, er);
			g.fillRect(ec - 5, er - 5, 10, 10);
		}
	}

	private Color getColorForPiece(int piece) {
		if (piece == Constants.RED_CHECKER) {
			return Constants.RED_COLOR;
		} else if (piece == Constants.RED_KING) {
			return Constants.DARK_RED_COLOR;
		} else if (piece == Constants.BLACK_CHECKER) {
			return Constants.BLACK_COLOR;
		} else if (piece == Constants.BLACK_KING) {
			return Constants.DARK_BLACK_COLOR;
		}
		return null;
	}
}