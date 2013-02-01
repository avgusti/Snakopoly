package object;

public class RailroadCell extends TradeableCell {

	public RailroadCell(int index, String name, String description, int corpid,
			int price, int rent) {
		super(index, name, description, corpid, price, rent);
	
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

	@Override
	public int getType() {
		return Cell.RAIL;
	}

	@Override
	public void process(Board board) {
		Player player = board.getCurrentPlayer();
		if (this.owner != null && this.owner.id != player.id) {
			player.money -= this.getRent();
			owner.money += this.getRent();
			board.addMessage("Rent payed " + this.getRent());
		}
	}

	public int getRent() {
		if (owner == null)
			return 0;
		if (mortgage)
			return 0;

		int count = 0;
		for (TradeableCell c : owner.myprops)
			if (c instanceof RailroadCell)
				count++;
		return rent * count;
	}

	@Override
	public int getMaxRent() {
		return rent * 4;
	}
}
