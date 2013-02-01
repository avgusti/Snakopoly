package object;

public class TaxCell extends Cell {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

	public TaxCell(int index, String name, String description, int corpid) {
		super(index, name, description, corpid);

	}

	@Override
	public int getType() {
		return Cell.TAX;
	}

	@Override
	public void process(Board board) {
		Player player = board.getCurrentPlayer();
		int tax=player.money*board.TAX_PERCENTAGE/100;
		player.money -= tax;
		board.addMessage("Tax "+board.TAX_PERCENTAGE+"% paid " + tax);
	}

}
