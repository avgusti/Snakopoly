package object;

public class WaterElCell extends TradeableCell {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public WaterElCell(int index, String name, String description, int corpid,
			int price, int rent) {
		super(index, name, description, corpid, price, rent);
	}

	@Override
	public int getType() {
		return Cell.WATELL;
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
		if (infrasturcture == 0)
			if (!corp)
				return rent;
			else
				return rent * 2;
		return 0;
	}

	@Override
	public int getMaxRent() {
		return rent * 2;
	}
}
