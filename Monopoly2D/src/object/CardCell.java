package object;

public class CardCell extends Cell {
	public CardCell(int index, String name, String description, int corpid) {
		super(index, name, description, corpid);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public int getType() {
		return Cell.CARD;
	}

	@Override
	public void process(Board board) {
		Player player = board.getCurrentPlayer();
		Card card = board.getCard();
		if (card != null) {
			if (card.getType() == Card.RELEASE_FROM_JAIL)
				player.prisoncard.add(card);
			else
				board.putCard(card);
			card.calc(board);
		}

	}

}
