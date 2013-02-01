package object;

public class ChanceCell extends Cell {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public ChanceCell(int index, String name, String description, int corpid) {
		super(index, name, description, corpid);
		
	}

	@Override
	public int getType() {

		return Cell.CHANCE;
	}

	@Override
	public void process(Board board) {
		Player player = board.getCurrentPlayer();
		Card card = board.getChance();
		if(card!=null)
		{
		if (card.getType() == Card.RELEASE_FROM_JAIL)
			player.prisoncard.add(card);
		else
			board.putCard(card);
		card.calc(board);
		}

	}
}
