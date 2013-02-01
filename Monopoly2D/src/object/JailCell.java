package object;

public class JailCell extends Cell {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public JailCell(int index, String name, String description, int corpid) {
	super(index, name, description, corpid);

}

	@Override
	public int getType() {
		return Cell.JAIL;
	}

	@Override
	public void process(Board board) {
		Player player = board.getCurrentPlayer();
		if (player.prison == 0)
			player.prison = 3;
		board.addMessage("Ups...");
	}

}
