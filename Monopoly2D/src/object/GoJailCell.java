package object;

public class GoJailCell extends Cell {
	public GoJailCell(int index, String name, String description, int corpid) {
		super(index, name, description, corpid);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Override
	public int getType() {
		return Cell.GO_JAIL;
	}

	@Override
	public void process(Board board) {
		board.moveToPrison();
		//board.addMessage("Go to jail");
	}

}
