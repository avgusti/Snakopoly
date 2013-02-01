package object;

public class FreeCell extends Cell {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public FreeCell(int index, String name, String description, int corpid) {
	super(index, name, description, corpid);
}

	@Override
	public int getType() {
		
		return Cell.FREE;
	}

	@Override
	public void process(Board board) {
	board.addMessage("Get some rest");	
	}

}
