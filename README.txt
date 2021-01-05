
```
CheckersAI
```

A checkers game that supports both humans and computers.

The computer is running a basic minimax algorithm with alpha-beta pruning.

Has a quick & dirty Java Swing GUI.


```
Features and Known Bugs
```
[] add an undo feature
	could either undo moves from the list of moves
	or make a daisy chain of boards each containing previous state
	either way...
[] make a game interface and new board class to simplify the board class for less information copying to speed up the minimax algorithm
	currently copying over wayyyyy too much
[] add in forfeit / new game button
[] make the tracker line thicker
[X] add in a move history printout
	printing out to console for now .... make this prettier later
	consider a side scrolling log that lists the moves
[X] add better printing for jumps that show the jump location
[X] potential bug found where a previous jump chain allows another piece to go
	this was caused because a king was incorrectly allowing new jumps to be made
	a king should only be made at the end of a turn, after checks for jumps are done
[X] only make computer move when you click
	did this to make it easier to see the moves the computer makes
	consider doing this on a timer or just visually show it a litter nicer. a pain with Swing repainting
[] Consider a stronger huerstic for board evaluation, like considering advancing pieces and keeping back row stable, and/or middle board control