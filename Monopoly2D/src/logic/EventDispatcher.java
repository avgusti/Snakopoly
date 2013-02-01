package logic;

import java.util.LinkedList;
import java.util.List;
import object.Board;
import object.Card;
import object.CardCell;
import object.Cell;
import object.ChanceCell;
import object.FreeCell;
import object.GoCell;
import object.GoJailCell;
import object.JailCell;
import object.Player;
import object.RailroadCell;
import object.TaxCell;
import object.TownCell;
import object.TradeableCell;
import object.WaterElCell;

public class EventDispatcher {
	public static final int STEP_NONE = 0, STEP_START = 1,
			STEP_CHECKPRISON = 2, STEP_SELL = 3, STEP_CHECKLOSE = 4,
			STEP_GAMEOVER = 5, STEP_BUY = 6, STEP_TRADE = 7, STEP_BUILD = 8,
			STEP_PROCESSPRISON = 9, STEP_PROCCESCELL = 10, STEP_FINISHED = -1,
			STEP_TRADEREQ = 11, STEP_LOSE = 12;
	private Board board;
	

	/**
	 * @return the board
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * @param board the board to set
	 */
	public void setBoard(Board board) {
		this.board = board;
		stepState=STEP_NONE;
	}

	private LinkedList<Card> cards = new LinkedList<Card>();
	private int cellsCount;

	private LinkedList<Card> chances = new LinkedList<Card>();

	int[][] corpsMap = null;

	private int curpos;

	private int nextpos;

	private Player player;
	private int estimatedFee;
	int priceDiff;
	TradeableCell playerCellToTrade;
	TradeableCell partnerCellToTrade;
	int ptradeid;
	private int stepState = STEP_NONE;

	private TradeRequest tradeReqeset;

	public void addCard(Card card) {
		this.cards.add(card);

	}

	public void addChance(Card card) {
		this.chances.add(card);

	}

	public void createGame(int players, int human, int smoney) {
//		board = new Board();
//		board.setCards(cards);
//		board.setChances(chances);
//		board.GO_AMOUNT = 150;
//		board.TAX_PERCENTAGE = 5;
//		board.add(new GoCell());// 0
//		board.add(new TownCell(100, "Press", 1));
//		board.add(new TownCell(110, "Copy-center", 1));
//		board.add(new TaxCell());
//		board.add(new TownCell(125, "Book shop", 1));
//		board.add(new RailroadCell(150, "West Railroad", 2));// 5
//		board.add(new TownCell(140, "Pizza", 3));
//		board.add(new WaterElCell(200, "Electricity", 4));
//		board.add(new TownCell(160, "Bar", 3));
//		board.add(new TownCell(175, "Restarant", 3));
//
//		board.add(new JailCell());// 10
//		board.add(new TownCell(200, "Hostel", 5));
//		board.add(new TownCell(220, "Motel", 5));
//		board.add(new ChanceCell());
//		board.add(new TownCell(240, "Hotel", 5));
//		board.add(new RailroadCell(150, "North Railroad", 2));
//		board.add(new TownCell(250, "Radio", 6));
//		board.add(new CardCell());
//		board.add(new TownCell(270, "Movies", 6));
//		board.add(new TownCell(290, "Tv", 6));
//		board.add(new FreeCell());// 20
//		board.add(new TownCell(320, "Cell phones", 7));
//		board.add(new TaxCell());
//		board.add(new TownCell(340, "Video games", 7));
//		board.add(new TownCell(360, "Computers", 7));
//		board.add(new RailroadCell(150, "East Railroad", 2));
//		board.add(new TownCell(380, "Gym", 8));
//		board.add(new WaterElCell(200, "Water", 4));
//		board.add(new TownCell(410, "Pool", 8));
//		board.add(new TownCell(440, "Golf", 8));
//		board.add(new GoJailCell());// 30
//
//		board.add(new TownCell(500, "Motocycles", 9));
//		board.add(new TownCell(530, "Cars", 9));
//		board.add(new ChanceCell());
//		board.add(new TownCell(560, "Traks", 9));
//		board.add(new RailroadCell(150, "South Railroad", 2));
//		board.add(new TownCell(600, "Bus stop", 10));
//		board.add(new CardCell());
//		board.add(new TownCell(650, "Railstation", 10));
//		board.add(new TownCell(700, "Airport", 10));
//		for (int i = 0; i < players; i++) {
//			Player p = new Player();
//			//
//			p.money = smoney;
//			//
//			if (i == human)
//				p.isHuman = true;
//			p.id = i;
//			board.getPlayers().add(p);
//			
//			board.setCurrentPlayer(board.getPlayers().get(0));
//
//			// TMP
//			// Add prison card for each player for test purpose
//			Card prison = new Card("Exit prison", 0, 0, Card.RELEASE_FROM_JAIL,
//					false);
//			p.prisoncard.add(prison);
//
//		}
	}

	public void doBuyStep() {
		Cell newCell = board.getCells().get(player.position);
		TradeableCell tc;
		if (newCell instanceof TradeableCell) {
			tc = (TradeableCell) newCell;
			if ((tc.getOwner() == null && player.money >= ((TradeableCell) newCell)
					.getPrice())
					|| (tc.getOwner() != null && tc.getOwner().id == player.id && player
							.checkCorp(tc))) {
				if (player.isHuman) {
					// suggest buy dialog
				} else {
					doLogicBuy();
				}
			}

		}

	}

	public void doCellProcessStep() {
		player.position = nextpos;
		// render move
		// cell info
		Cell newCell = board.getCells().get(player.position);
		newCell.process(board);

		if (nextpos != player.position) {
			newCell = board.getCells().get(player.position);
			// render move
			// cell info
		}
	}

	public void doCheckPrisonStep() {
		// Prison
		if (player.prisoncard.size() > 0 && player.prison > 0) {
			// human will see exit dialog
			if (!player.isHuman) {
				if (player.prisoncard.size() > 0 &&  board.canPlayerByAnyCell(player)) {
					board.exitPrisonComputer();
					board.addMessage("Prison card used");
				}
			}

		}

	}

	public void doGameOverStep() {
		// do nothing
	}

	// implements game logic for computer player
	public void doLogicBuy() {
		if (!player.isHuman) {
			if (player.money > estimatedFee * 1.5) //
			{
				int free = (int) (player.money - estimatedFee * 1.5);
				Cell c = board.getCells().get(player.position);
				if (c instanceof TradeableCell) {
					TradeableCell tc = (TradeableCell) c;
					if (tc.getOwner() == null && tc.getPrice() < free) {
						tc.buy(player);
						board.addMessage("Just bought");
					}
				}
			}
		}
	}

	public void doBuildStep() {
		if (!player.isHuman) {
			int free = (int) (player.money - estimatedFee * 1.5);
			if (free >= 0) {
				// some workaround due to stupid model
				int oldmoney = player.money;
				player.money = free;
				int diff = oldmoney - free;
				boolean repeat = true;

				while (repeat) {
					int added = 0;
					for (Cell cc : player.myprops) {
						if (cc instanceof TownCell) {
							TownCell tc = (TownCell) cc;
							if (tc.addInfrasturcture()) {
								board.addMessage("Buld infrasturcure", tc);
								added++;
							}
						}
					}
					if (added == 0)
						repeat = false;

				}
				for (TradeableCell tc : player.myprops) {
					if (tc.isMortgage())
						if (tc.payMortgage())
							board.addMessage("Payed mortgage", tc);
				}

				player.money += diff;
			}
		}
	}

	public void doLogicSell() {
		if (!player.isHuman) {

			while (player.money < 0 && player.myprops.size() > 0) {
				int minW = Integer.MAX_VALUE;
				TradeableCell minTc = null;
				for (TradeableCell tc : player.myprops) {
					int tmp;
					if (tc.isMortgage())
						tmp = player.countCorp(tc) * tc.getMaxRent() / 5;
					else
						tmp = player.countCorp(tc) * tc.getRent();

					if (minW > tmp) {
						minTc = tc;
						minW = tmp;
					}
				}
				if (minTc != null) {
					if (minTc.getInfrasturcture() > 0) {
						((TownCell) minTc).selllInfrasturcture();
					} else if (!minTc.isMortgage()) {
						minTc.getMortgage();
						board.addMessage("Got Mortgage for cell", minTc);
					} else {
						minTc.sell();
						board.addMessage("Sold cell", minTc);
					}
				}
			}
			if (player.money < 0) {
				board.addMessage("Lost game");
				player.lose = true;
			}
		}
	}

	public void doLoseStep() {
		if (player.money < 0 && player.myprops.size() == 0) {
			player.lose = true;
			board.addMessage("Lost game");
		}

		// can move?
		if (player.lose) {
			if (player.isHuman) {
				board.addMessage("Game over");
				board.setGameOver(true);
			}
		}
	}

	public void doPrisonStep() {
		if (player.prison > 0) {
			player.prison--;
			board.addMessage("Wait in prison");
			stepState = STEP_NONE;
			return;
		}

	}

	public void doSellStep() {
		stepState = STEP_SELL;
		// logic
		while (player.money < 0 && player.prison == 0 && !player.lose) //
		{
			if (player.isHuman) {
				break;
			} else {
				// logic
				doLogicSell();
			}

		}
	}

	public void doStartStep() {
		stepState = STEP_START;
		board.clearMessages();
		board.nextPlayer();
		player = board.getCurrentPlayer();
		// Skip looser
		if (player.lose) {
			doStartStep();
			return;
		}
		//
		board.dropDice();
		if (board.isDuplet() && player.prison>0) {
			board.addMessage("Droped duplet! Exit prison");
			player.prison = 0;
		}
		cellsCount = board.getCells().size();
		curpos = player.position;
		nextpos = curpos + board.calcStep();
		if (nextpos >= cellsCount) {
			nextpos -= cellsCount;
			player.money += board.GO_AMOUNT;
		}
		// Estimate fee
		estimatedFee = 0;
		for (Cell c : board.getCells()) {
			if (c instanceof TradeableCell && c.index > player.position) {
				TradeableCell tc = (TradeableCell) c;
				if (tc.getOwner() != player) {
					estimatedFee += tc.getRent();
				}
			}
			if (c.getType() == Cell.TAX)
				estimatedFee += board.TAX_PERCENTAGE;
		}
		// realistic fee until reaching go cell
		// fee=fee / //total fee
		// (board.cellsCount-p.position+1) * //cells left to get more money
		// ((board.cellsCount-p.position+1)/6); // estimated number of steps

		// so
		estimatedFee = estimatedFee / 6;
		// some assumption

	}

	public int doSteps() {
		int oldstate = stepState;
		switch (stepState) {
		case STEP_NONE: {
			doStartStep();
			stepState = STEP_CHECKPRISON;
			break;
		}
		case STEP_CHECKPRISON: {
			doCheckPrisonStep();
			stepState = STEP_PROCESSPRISON;
			doPrisonStep();
			break;
		}
		case STEP_PROCESSPRISON: {
			doPrisonStep();
			stepState = STEP_PROCCESCELL;
			break;
		}
		case STEP_SELL: {
			doSellStep();
			stepState = STEP_CHECKLOSE;
			break;
		}
		case STEP_CHECKLOSE: {
			doLoseStep();
			if (board.isGameOver()) {
				stepState = STEP_GAMEOVER;
				break;
			}

			if (!player.lose) {
				stepState = STEP_BUY;
			} else {
				stepState = STEP_LOSE;
			}
			break;
		}
		case STEP_LOSE: {
			stepState = STEP_NONE;
			break;
		}
		case STEP_PROCCESCELL: {
			doCellProcessStep();
			stepState = STEP_SELL;
			break;
		}
		case STEP_GAMEOVER: {
			doGameOverStep();
			// stepState = STEP_BUY;
			break;
		}
		case STEP_BUY: {
			doBuyStep();
			stepState = STEP_TRADE;
			break;
		}
		case STEP_TRADE: {
			doTradeStep();
			if (STEP_TRADEREQ == stepState) {
				break;
			}
			stepState = STEP_BUILD;
			break;
		}
		case STEP_TRADEREQ: {
			// /doTradeStep();
			stepState = STEP_BUILD;
			break;
		}
		case STEP_BUILD: {
			doBuildStep();
			stepState = STEP_FINISHED;
			break;
		}
		case STEP_FINISHED: {
			stepState = STEP_NONE;
			break;
		}
		default:
			break;
		}
		return oldstate;
	}

	public void doTrade() {

		if (player.isHuman) {
			return;
		}
		if (corpsMap == null)
			fillCorpsMap();

		int[][] weights = new int[board.getPlayers().size()][board.cellsCount];
		List<Cell> cells = board.getCells();
		// Calc corp values (can be refactored)
		int[][] corpsCount = new int[corpsMap.length][board.getPlayers().size()];
		for (int i = 0; i < cells.size(); i++) {
			Cell c = cells.get(i);
			if (c instanceof TradeableCell) {
				TradeableCell tc = (TradeableCell) c;
				if (tc.getOwner() != null) {
					int corpid = tc.getCorpid();
					int ownerid = tc.getOwner().id;
					corpsCount[corpid][ownerid]++;
				}
			}
		}

		// fill weights table
		for (int k = 0; k < corpsCount[0].length; k++)
			for (int i = 0; i < corpsCount.length; i++) {
				int corpCount = corpsCount[i][k];
				for (int j = 0; j < 4; j++) {
					if (corpsMap[i][j] != 0) {
						TradeableCell cell = (TradeableCell) cells
								.get(corpsMap[i][j]);
						if (cell.getOwner() == null || cell.isMortgage() || cell.getInfrasturcture()>0) {
							weights[k][cell.index] = 0;
							continue;
						}
						int weight = corpCount * cell.getMaxRent();
						if (cell.getType() == Cell.TOWN) {
							if (corpCount == 2) {
								weight *= 10;
							}// don't trade
							if (corpCount == 3) {
								weight = 0;
							}// don't trade
						}
						if (cell.getType() == Cell.RAIL) {

						}
						if (cell.getType() == Cell.WATELL) {
						}

						if (cell.getOwner().id != k)
							weight = -weight;

						weights[k][cell.index] = weight;
					}
				}
			}
		// Search for property player interested in

		int weight = 0, id = -1;
		for (int i = 0; i < cells.size(); i++) {
			if (weights[player.id][i] < weight) {
				weight = weights[player.id][i];
				id = i;
			}
		}
		if (weight < 0) {
			// Look for counter offer
			TradeableCell partnerCell = (TradeableCell) cells.get(id);
			int tradeid = partnerCell.getOwner().id;
			int sellweigth = 0;
			int sellid = -1;
			for (int i = 0; i < player.myprops.size(); i++) {
				if (weights[tradeid][player.myprops.get(i).index] < sellweigth) {
					sellweigth = weights[tradeid][player.myprops.get(i).index];
					sellid = player.myprops.get(i).index;
				}
			}
			if (sellweigth < 0) {
				// Do trade
				// @TODO add weight check
				// tc = (TradeableCell) cells.get(id);
				TradeableCell playerCell = (TradeableCell) cells.get(sellid);
				priceDiff = partnerCell.getPrice() - playerCell.getPrice();
				if ((player.money > priceDiff)
						&& (board.getPlayers().get(tradeid).money > partnerCell
								.getPrice() - playerCell.getPrice())) {
					if (board.getPlayers().get(tradeid).isHuman) {
						partnerCellToTrade = partnerCell;
						playerCellToTrade = playerCell;
						stepState = STEP_TRADEREQ;
						tradeReqeset.requestTrade(this, playerCell,
								partnerCell, playerCell.getPrice()
										- partnerCell.getPrice());

					} else {
						partnerCellToTrade = partnerCell;
						playerCellToTrade = playerCell;
						performTrade();
					}
				}
			}
		}

	}

	public void doTradeStep() {
		doTrade();
	}

	public void fillCorpsMap() {
		LinkedList<Cell> cells = board.getCells();
		int tcc = 0;
		for (int i = 0; i < cells.size(); i++) {
			if (cells.get(i) instanceof TradeableCell) {
				TradeableCell tc = (TradeableCell) cells.get(i);
				if (tc.getCorpid() > tcc)
					tcc = tc.getCorpid();

			}
		}
		corpsMap = new int[tcc + 1][4];
		for (int i = 0; i < cells.size(); i++) {
			if (cells.get(i) instanceof TradeableCell) {
				TradeableCell tc = (TradeableCell) cells.get(i);
				int j = 0;
				while (corpsMap[tc.getCorpid()][j] > 0)
					j++;
				corpsMap[tc.getCorpid()][j] = tc.index;
			}
		}
	}

	/**
	 * @return the stepState
	 */
	public int getStepState() {
		return stepState;
	}

	public void performTrade() {
		final Player player = playerCellToTrade.getOwner();
		final Player partner = partnerCellToTrade.getOwner();

		//board.addMessage("Has " + player.money, player, null);
		//board.addMessage("Has " + partner.money, partner, null);
		//board.addMessage("Price diff " + priceDiff, null, null);
		//board.addMessage("Price " + playerCellToTrade.getPrice(), null,
		//		playerCellToTrade);
		//board.addMessage("Price " + partnerCellToTrade.getPrice(), null,
		//		partnerCellToTrade);

		player.money -= priceDiff;
		partner.money += priceDiff;
		playerCellToTrade.setOwner(partner);
		partnerCellToTrade.setOwner(player);

		board.addMessage("Got from trade", player, partnerCellToTrade);
		board.addMessage("Got from trade", partner, playerCellToTrade);
		
		player.refreshCorps();
		partner.refreshCorps();
		//board.addMessage("Has " + player.money, player, null);
		//board.addMessage("Has " + partner.money, partner, null);

	}

	public void setOnRequestTrade(TradeRequest request) {
		this.tradeReqeset = request;
	}

}
