package object;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Board implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

	private List<BoardMessage> boardMessages = new ArrayList<BoardMessage>();
	private LinkedList<Card> cards = new LinkedList<Card>();
	private LinkedList<Cell> cells = new LinkedList<Cell>();
	public int cellsCount;
	private LinkedList<Card> chances = new LinkedList<Card>();
	private Player currentPlayer, nextPlayer;
	private int dice1, dice2;
	private int freeIndex;
	private boolean gameOver = false;
	
	//Reserved to support full game rules	
//	public int HOTELS_TOTAL = 12;
//	public int hotelsInUse = 0;
//	public int HOUSES_TOTAL = 32;
//	public int housesInUse = 0;
	private Player human = null;
//flag to notify boar about restore
	public transient boolean isRestrore=false;
	// updated on add()
	private int jailIndex;
	private boolean justCreated = true;
	private LinkedList<Player> players = new LinkedList<Player>();
	public transient Random rnd = new Random();
	private long seed = System.nanoTime();
	public int GO_AMOUNT=150;
	public int TAX_PERCENTAGE= 5;
	public int START_AMOUNT=2000;
	private int turnNum=0;



	

	public Board() {

	}

	public void add(Cell c) {
		cells.add(c);
		cellsCount = cells.size();
		c.index = cellsCount - 1;

		if (c.getType() == Cell.JAIL)
			jailIndex = c.index;
		if (c.getType() == Cell.FREE)
			freeIndex = c.index;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void addMessage(String message) {
		boardMessages.add(new BoardMessage(message, currentPlayer,
				getCurrCell()));
	}

	public void addMessage(String message, Cell cell) {
		boardMessages.add(new BoardMessage(message, currentPlayer, cell));
	}

	public void addMessage(String message, Player player, Cell cell) {
		boardMessages.add(new BoardMessage(message, player, cell));
	}

	private void calcNextPlayer() {
		if (currentPlayer.id < players.size() - 1) {
			nextPlayer = players.get(currentPlayer.id + 1);
		} else {
			nextPlayer = players.get(0);
		}
	}

	public int calcStep() {
		return dice1 + dice2;
	}

	public boolean canBuy() {

		Cell c = cells.get(currentPlayer.position);
		if (c instanceof TradeableCell) {
			TradeableCell tc = (TradeableCell) c;
			if (tc.getOwner() == null && currentPlayer.money >= tc.getPrice()) {
				return true;
			}
		}
		return false;

	}

	// / for human only
	public boolean canLeavePrison() {

		if (nextPlayer.isHuman
				&& cells.get(nextPlayer.position) instanceof JailCell
				&& nextPlayer.prisoncard.size() > 0)
			return true;
		return false;
	}

	public boolean canMove() {
		if (currentPlayer != null) {
			return currentPlayer.money >= 0;
		}
		return true;
	}

	public boolean canPlayerByAnyCell(Player p) {
		for (Cell c : cells) {
			if (c instanceof TradeableCell) {
				final TradeableCell tc = (TradeableCell) c;
				if (tc.getOwner() == null && p.money > tc.getPrice())
					return true;
			}
		}
		return false;
	}

	public void clearMessages() {
		boardMessages.clear();
	}

	public void doBuy() {
		((TradeableCell) getCurrCell()).buy(currentPlayer);
	}

	public int dropDice() {
		if (!isRestrore) {
			seed = System.nanoTime();
		} else {
			isRestrore = false;
		}
		rnd.setSeed(seed);
		dice1 = rnd.nextInt(6) + 1;
		dice2 = rnd.nextInt(6) + 1;
		turnNum++;
		return dice1 + dice2;
	}

	public void exitPrisonComputer() {
		currentPlayer.prisoncard.remove(0);
		currentPlayer.prison = 0;
	}

	// / for human only
	public void exitPrisonHuman() {
		if (canLeavePrison()) {
			nextPlayer.prisoncard.remove(0);
			nextPlayer.prison = 0;
		}
	}

//	public void freeHotel() {
//		if (hotelsInUse > 0)
//			hotelsInUse--;
//	}
//
//	public void freeHouse() {
//		if (housesInUse > 0)
//			housesInUse--;
//	}

	public List<BoardMessage> getBoardMessages() {
		return boardMessages;
	}

	public Card getCard() {
		if (cards.size() != 0) {
			cards.addFirst(cards.getLast());
			return cards.removeLast();
		} else
			return null;
	}

	/**
	 * @return the cards
	 */
	public LinkedList<Card> getCards() {
		return cards;
	}

	public int getCellID() {
		return currentPlayer.position;

	}

	public LinkedList<Cell> getCells() {
		return cells;
	}

	/**
	 * @return the cellsCount
	 */
	public int getCellsCount() {
		return cellsCount;
	}

	public Card getChance() {
		if (chances.size() != 0) {
			chances.addFirst(chances.getLast());
			return chances.removeLast();
		} else
			return null;
	}

	/**
	 * @return the chances
	 */
	public LinkedList<Card> getChances() {
		return chances;
	}

	
	public int getCorpID() {
		Cell c = getCurrCell();

		if (c instanceof TradeableCell) {
			return ((TradeableCell) c).getCorpid();
		}
		return 0;
	}

	public Cell getCurrCell() {
		return cells.get(currentPlayer.position);
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public int getDice1() {
		return dice1;
	}

	public int getDice2() {
		return dice2;
	}

	public int getFreeIndex() {
		return freeIndex;
	}

	/**
	 * @return the hotelsInUse
	 */
//	public int getHotelsInUse() {
//		return hotelsInUse;
//	}
//
//	/**
//	 * @return the housesInUse
//	 */
//	public int getHousesInUse() {
//		return housesInUse;
//	}

	public Player getHuman() {
		if (human == null) {
			for (Player p : players) {
				if (p.isHuman)
					human = p;
			}
		}
		return human;
	}

	public int getJailIndex() {
		return jailIndex;
	}

	/**
	 * @return the nextPlayer
	 */
	public Player getNextPlayer() {
		return nextPlayer;
	}

	public LinkedList<Player> getPlayers() {
		return players;
	}

	public int getPrice() {
		if (getCurrCell() instanceof TradeableCell) {
			return ((TradeableCell) getCurrCell()).price;
		}
		return 0;
	}


	/**
	 * @return the seed
	 */
	public long getSeed() {
		return seed;
	}

	/**
	 * @return the turnNum
	 */
	public int getTurnNum() {
		return turnNum;
	}

	public boolean isCard() {
		if (getCurrCell() instanceof CardCell)
			return true;
		return false;
	}

	public boolean isChance() {
		if (getCurrCell() instanceof ChanceCell)
			return true;
		return false;
	}

	public boolean isDuplet() {
		return dice1 == dice2;
	}

	/**
	 * @return the gameOver
	 */
	public boolean isGameOver() {
		return gameOver;
	}
//
//	public boolean isHouseAvailable() {
//		if (housesInUse < HOUSES_TOTAL)
//			return true;
//		return false;
//	}

	/**
	 * @return the justCreated
	 */
	public boolean isJustCreated() {
		return justCreated;
	}

	public boolean isPrison() {
		return (currentPlayer.position == getJailIndex());
	}

	public void moveToPosition(int i) {
		clearMessages();
		currentPlayer.position = i;
		getCells().get(currentPlayer.position).process(this);
	}

	public void moveToPrison() {
		currentPlayer.position = getJailIndex();
		getCells().get(currentPlayer.position).process(this);
		addMessage("Go to prison");
	}

	public void nextPlayer() {

		if (justCreated || currentPlayer == null) {
			currentPlayer = players.getFirst();
			justCreated = false;
			return;
		}
		if (currentPlayer.id == players.size() - 1) {
			currentPlayer = players.getFirst();
		} else if (players.size() > 1) {
			currentPlayer = players.get(currentPlayer.id + 1);
		} else {
			currentPlayer = players.getFirst();
		}
		{
			if (currentPlayer.lose) {
				nextPlayer();
			}
		}
		calcNextPlayer();

	}

	public void putCard(Card c) {
		cards.addFirst(c);
	}

	public void putChance(Card c) {
		chances.addFirst(c);
	}

	private void readObject(final ObjectInputStream s)
			throws ClassNotFoundException, IOException {
		s.defaultReadObject();
		// derivedValue is still 0
		this.rnd = new Random();
	}

	/**
	 * @param boardMessages
	 *            the boardMessages to set
	 */
	public void setBoardMessages(List<BoardMessage> boardMessages) {
		this.boardMessages = boardMessages;
	}

//	public boolean reserveHotel() {
//		if (hotelsInUse < HOTELS_TOTAL) {
//			hotelsInUse++;
//			return true;
//		}
//		return false;
//	}
//
//	public boolean reserveHouse() {
//		if (housesInUse < HOUSES_TOTAL) {
//			housesInUse++;
//			return true;
//		}
//		return false;
//	}

	/**
	 * @param cards
	 *            the cards to set
	 */
	public void setCards(LinkedList<Card> cards) {
		this.cards = cards;
	}

	/**
	 * @param cells
	 *            the cells to set
	 */
	public void setCells(LinkedList<Cell> cells) {
		this.cells = cells;
		this.cellsCount=cells.size();
		for(int i=0;i<cellsCount;i++)
		{
			this.cells.get(i).index=i;
		}
	}

	

	/**
	 * @param chances
	 *            the chances to set
	 */
	public void setChances(LinkedList<Card> chances) {
		this.chances = chances;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
		calcNextPlayer();
	}

	/**
	 * @param dice1
	 *            the dice1 to set
	 */
	public void setDice1(int dice1) {
		this.dice1 = dice1;
	}

	/**
	 * @param dice2
	 *            the dice2 to set
	 */
	public void setDice2(int dice2) {
		this.dice2 = dice2;
	}

	public void setFreeIndex(int freeIndex) {
		this.freeIndex = freeIndex;
	}

	/**
	 * @param gameOver
	 *            the gameOver to set
	 */
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	/**
	 * @param hotelsInUse
	 *            the hotelsInUse to set
	 */
//	public void setHotelsInUse(int hotelsInUse) {
//		this.hotelsInUse = hotelsInUse;
//	}
//
//	/**
//	 * @param housesInUse
//	 *            the housesInUse to set
//	 */
//	public void setHousesInUse(int housesInUse) {
//		this.housesInUse = housesInUse;
//	}

	public void setJailIndex(int jailIndex) {
		this.jailIndex = jailIndex;
	}

	/**
	 * @param justCreated
	 *            the justCreated to set
	 */
	public void setJustCreated(boolean justCreated) {
		this.justCreated = justCreated;
	}

	/**
	 * @param nextPlayer
	 *            the nextPlayer to set
	 */
	public void setNextPlayer(Player nextPlayer) {
		this.nextPlayer = nextPlayer;
	}

	/**
	 * @param players
	 *            the players to set
	 */
	public void setPlayers(LinkedList<Player> players) {
		this.players = players;
	}

	
	/**
	 * @param seed
	 *            the seed to set
	 */
	public void setSeed(long seed) {
		this.seed = seed;
	}

	/**
	 * @param turnNum the turnNum to set
	 */
	public void setTurnNum(int turnNum) {
		this.turnNum = turnNum;
	}

	public int getActivePlayersCount() {
		int c=0;
		for(Player p:players)
			if(!p.lose) c++;
		return c;
	}
}
