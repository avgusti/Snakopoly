package object;

public class GoCell extends Cell {
	public GoCell(int index, String name, String description, int corpid) {
		super(index, name, description, corpid);
	
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	@Override
	public int getType() {
		return Cell.GO;
	}

	@Override
	public void process(Board board) {
		board.addMessage("GO");
		
	}

}
