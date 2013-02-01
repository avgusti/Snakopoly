package object;

import java.io.Serializable;
import java.util.LinkedList;

public class Card implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String text;
	private int pos;
	private int ammount;
	private int type;
	private boolean isChance;

	public Card(String text, int pos, int ammount, int type, boolean chance) {
		super();
		this.text = text;
		this.pos = pos;
		this.ammount = ammount;
		this.type = type;
		this.isChance = chance;
	}

	public static final int GO_START = 0;
	public static final int GO_JAIL = 1;
	public static final int RELEASE_FROM_JAIL = 2;
	public static final int PRIZE = 3;
	public static final int CALC_SHOPS = 4;
	public static final int CALC_HOTELS = 5;
	public static final int CALC_PLAYERS = 6;

	public void calc(Board board) {
		LinkedList<Player> players = board.getPlayers();
		Player player = board.getCurrentPlayer();
		int res = ammount;
		switch (type) {
		case PRIZE:
			player.money += ammount;
			break;
		case CALC_PLAYERS:
			res = ammount * board.getActivePlayersCount();
			player.money += res;
			for (Player p : players) {
				p.money -= ammount;
			}
			break;
		case CALC_SHOPS: {
			Player p = player;
			int infr = 0;
			for (TradeableCell c : p.myprops) {
				if (c.getInfrasturcture() < 5)
					infr += c.getInfrasturcture();
			}
			res = ammount * infr;
			p.money += res;
			break;
		}
		case CALC_HOTELS: {
			Player p = player;
			int infr = 0;
			for (TradeableCell c : p.myprops) {
				if (c.getInfrasturcture() == 5)
					infr++;
			}
			res = ammount * infr;
			p.money += res;
			break;
		}
		case GO_JAIL: {
			player.position = board.getJailIndex();
			board.getCells().get(player.position).process(board);
			break;
		}
		case RELEASE_FROM_JAIL: {
			if (player.prisoncard.size() > 0) {
				Card c = player.prisoncard.remove(player.prisoncard.size() - 1);
				player.prison = 0;
				if (!isChance)
					board.putCard(c);
				else
					board.putChance(c);
			}
			break;
		}
		case GO_START: {
			player.money += ammount;
			player.position = pos;
			board.getCells().get(player.position).process(board);
			break;
		}
		default:
		}
		board.addMessage((res != 0 ? res + " " : "") + this.getText());
	}

	public int getType() {
		return type;
	}

	public String getText() {
		return text;
	}

	/**
	 * @return the pos
	 */
	public int getPos() {
		return pos;
	}

	/**
	 * @param pos
	 *            the pos to set
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}

	/**
	 * @return the ammount
	 */
	public int getAmmount() {
		return ammount;
	}

	/**
	 * @param ammount
	 *            the ammount to set
	 */
	public void setAmmount(int ammount) {
		this.ammount = ammount;
	}

	/**
	 * @return the isChance
	 */
	public boolean isChance() {
		return isChance;
	}

	/**
	 * @param isChance
	 *            the isChance to set
	 */
	public void setChance(boolean isChance) {
		this.isChance = isChance;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

}
