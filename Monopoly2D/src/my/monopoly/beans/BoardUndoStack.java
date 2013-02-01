package my.monopoly.beans;

import java.util.LinkedList;

import object.Board;

public class BoardUndoStack {
	private LinkedList<String> undoStack = new LinkedList<String>();
	private Board board = null;

	public BoardUndoStack(Board board) {
		this.board = board;
	}

	public boolean canUndo() {
		return undoStack.size() > 0;
	}

	public boolean saveBoardToUndoStack(Board board) {
		this.board = board;
		return saveBoardToUndoStack(board, 10);
	}

	public boolean saveBoardToUndoStack() {
		return saveBoardToUndoStack(this.board, 10);
	}

	public boolean saveBoardToUndoStack(int max) {
		return saveBoardToUndoStack(this.board, max);
	}

	public boolean saveBoardToUndoStack(Board board, int max) {
		final String tmp = BoardLoader.saveToString(board);
		if(max==0){return false;}
		if (tmp == null) {
			return false;
		}
		
		if (undoStack.size() == max) {
			undoStack.removeFirst();
		}
		undoStack.addLast(tmp);
		return true;
	}

	public Board loadBoardFromUndoStack() {

		Board board = this.board;
		if (undoStack.size() > 0)
			board = BoardLoader.loadFromSting(undoStack.getLast());
		undoStack.removeLast();
		if (board != null)
			this.board = board;
		return board;
	}
}
