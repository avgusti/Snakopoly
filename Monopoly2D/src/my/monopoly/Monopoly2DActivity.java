package my.monopoly;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import logic.EventDispatcher;
import logic.TradeRequest;
import my.monopoly.beans.BoardInfo;
import my.monopoly.beans.BoardLoader;
import my.monopoly.beans.BoardUndoStack;
import my.monopoly.beans.CellInfo;
import my.monopoly.beans.GameOptions;
import object.Board;
import object.BoardMessage;
import object.Card;
import object.Cell;
import object.Player;
import object.RailroadCell;
import object.TownCell;
import object.TradeableCell;
import object.WaterElCell;
import resource.ResourceBundle;
import resource.ResourceLoader;
import wigets.CellView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

// prepare game
public class Monopoly2DActivity extends Activity implements TradeRequest {
	private class buildListener implements View.OnClickListener {

		final TradeableCell c;
		final int code;
		final LinearLayout l;
		final ViewGroup vp;

		public buildListener(LinearLayout l, TradeableCell c, int code,
				ViewGroup vp) {
			this.l = l;
			this.c = c;
			this.vp = vp;
			this.code = code;
		}

		@Override
		public void onClick(View v) {
			switch (code) {
			case BUILD_PLUS:
				// showToast("Plus");
				if (c instanceof TownCell) {
					TownCell tc = (TownCell) c;
					tc.addInfrasturcture();
					break;
				}
			case BUILD_MINUS: {
				// showToast("Minus");
				if (c instanceof TownCell) {
					TownCell tc = (TownCell) c;
					if (tc.getInfrasturcture() > 0) {
						tc.selllInfrasturcture();
					} else {
						List<TradeableCell> cells = board.getCurrentPlayer().myprops;
						boolean hasStras = false;
						for (TradeableCell cc : cells) {
							if (cc.getCorpid() == c.getCorpid()
									&& cc.getInfrasturcture() > 0)
								hasStras = true;
						}
						if (!hasStras)
							tc.sell();
						else
							showToast("Can't sell. Sell infasrtucture first");
					}
				} else {
					c.sell();
				}
				break;
			}
			case BUILD_MORTGAGE: {
				if (c.isMortgage()) {
					c.payMortgage();
				} else {
					c.getMortgage();
				}
			}
			default:
				break;
			}
			buildupdate(l, c, vp);
			updateCells();
		}
	}

	 private class MyAnimationListener implements Animator.AnimatorListener //
	 
	 // Animation.AnimationListener
	 {
	 private CellView cv;
	 private int p;
	 private ImageView v;
	
	 public MyAnimationListener(ImageView v, int player, CellView cv) {
	 this.v = v;
	 this.p = player;
	 this.cv = cv;
	 }
	
	 @Override
	 public void onAnimationCancel(Animator animation) {
	 movePlayer(p, v, cv);
	 animationActive = false;
	 dropDiceStep();
	
	 }
	
	 @Override
	 public void onAnimationEnd(Animator animation) {
	 movePlayer(p, v, cv);
	 animationActive = false;
	 dropDiceStep();
	
	 }
	
	 @Override
	 public void onAnimationRepeat(Animator animation) {
	 }
	
	 @Override
	 public void onAnimationStart(Animator animation) {
	 animationActive = true;
	
	 }
	 }

	class TurnHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// switch to identify the message by its code
			switch (msg.what) {
			default:
			case 0:
				super.handleMessage(msg);
				if (board.canMove()) {
					dropDiceStep();
					int d1 = getBoard().getDice1();
					int d2 = getBoard().getDice2();
					ad1.stop();
					ad2.stop();
					dice1View.setImageDrawable(bundle.dices[d1]);
					dice2View.setImageDrawable(bundle.dices[d2]);
					currPlayerView
							.setImageDrawable(bundle.players[currPlayerID + 1]);
				}
			}
		}
	}

	public static final int BUILD_PLUS = 0, BUILD_MINUS = 1,
			BUILD_MORTGAGE = 2;
	public static final int BUY = 0, TRADE = 1, SALE_BUILD = 2, INFO = 4,
			EXITPRISON = 7, TAX = 9, CANT_MOVE = 10, TURNINFO = 11,
			TRADEREQUEST = 12, CELLINFO = 14;
	public static final int GO = 100;
	AnimationDrawable ad1, ad2;
	private boolean animationActive;

	Board board;

	ResourceBundle bundle;

	private Dialog buyD, tradeD, tradeOnReqD, sale_buildD, infoD, turnInfoD,
			exitD, cellInfoD;
	private int cellInfoID;
	private int[] cellsMap;
	CellView[] cells = new CellView[40];

	int currPlayerID = 0;

	ImageView dice1View, dice2View, currPlayerView;

	EventDispatcher dispatcher;
	boolean exitDialogResult = false;
	int fwidth, fheight, rotation, width, height, cw, ch;

	FrameLayout gameBoard;

	private GameOptions gameOptions;

	View go;

	private Dialog goD;

	TradeableCell hc = null, pc = null;

	LayoutInflater inflater;
	int[] oldcell = { -1, -1, -1, -1 };

	Player[] PlayersForTrade = new Player[] { null, null };
	ImageView[] PlayersViews = new ImageView[4];

	Random rnd = new Random();

	View sale_build, trade, info, undo;

	private BoardUndoStack undoStack;

	private void BuildDialogs() {
		infoD = new Dialog(this, R.style.FullHeightDialog);
		tradeD = new Dialog(this, R.style.FullHeightDialog);
		tradeOnReqD = new Dialog(this, R.style.FullHeightDialog);
		sale_buildD = new Dialog(this, R.style.FullHeightDialog);
		buyD = new Dialog(this, R.style.FullHeightDialog);
		turnInfoD = new Dialog(this, R.style.FullHeightDialog);
		exitD = new Dialog(this, R.style.FullHeightDialog);
		cellInfoD = new Dialog(this, R.style.FullHeightDialog);
		initDialog(infoD, R.layout.infodialog);
		initDialog(tradeD, R.layout.tradedialog);
		initDialog(tradeOnReqD, R.layout.tradedialog);
		initDialog(sale_buildD, R.layout.sale_builddialog);
		initDialog(buyD, R.layout.buydialog);
		initDialog(turnInfoD, R.layout.turnreport);
		initDialog(exitD, R.layout.exitprison);
		initDialog(cellInfoD, R.layout.cellinfodialog);

		goD = new Dialog(this, R.style.FullHeightDialog);
		initDialog(goD, R.layout.godialog);
		Button gobutton = (Button) goD.findViewById(R.id.gobutton);
		gobutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText ed = (EditText) goD.findViewById(R.id.gotext);
				moveto(Integer.parseInt(ed.getText().toString().toString()));
				goD.dismiss();

			}
		});
		Button addCartButton = (Button) goD.findViewById(R.id.addPrisonCard);
		addCartButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				board.getCurrentPlayer().prisoncard.add(new Card("", 0, 0, 0,
						false));
			}
		});

		View buyButton = buyD.findViewById(R.id.buyButton);
		buyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doBuy();
				buyD.dismiss();
			}
		});

	}

	private void buildupdate(LinearLayout l, TradeableCell c, ViewGroup vp) {
		if (c.getOwner() == null) {
			vp.removeView(l);
			return;
		}
		TextView price = (TextView) l.findViewById(1);
		ImageView plus = (ImageView) l.findViewById(2);
		// ImageView minus = (ImageView) l.findViewById(3);
		ImageView stars = (ImageView) l.findViewById(4);
		ImageView mortgage = (ImageView) l.findViewById(5);
		if (c.isMortgage()) {
			mortgage.setImageDrawable(bundle.mortgage_pay);
		} else {
			mortgage.setImageDrawable(bundle.mortgage_get);
		}
		if (c.getInfrasturcture() > 0) {
			mortgage.setEnabled(false);
		} else {
			mortgage.setEnabled(true);
		}

		TextView money = (TextView) sale_buildD.findViewById(R.id.playermoney);
		money.setText(board.getCurrentPlayer().money + "");
		if (c instanceof TownCell) {
			TownCell towncell = (TownCell) c;
			if (towncell.isCorp() && towncell.getInfrasturcture() < 5) {
				price.setText(towncell.getInfrasturcturePrice() + "");
				plus.setImageDrawable(bundle.plus);
				plus.setEnabled(true);
			} else {
				plus.setImageDrawable(null);
				plus.setEnabled(false);

			}
		}
		if (!c.isMortgage()) {
			stars.setImageDrawable(bundle.starsh[c.getInfrasturcture()]);
		} else {
			stars.setImageDrawable(bundle.mortgage[1]);
		}
	}

	private int calcCell(int i) {

		int j = i + 1;
		int x = 1, y = 1;
		while (j > 0) {
			if (j <= 5) {
				x = j;
				y = 1;
				j = 0;
				break;
			}
			j -= 5;
			if (j <= 7) {
				x = 5;
				y = 1 + j;
				j = 0;
				break;
			}
			j -= 7;
			if (j <= 4) {
				x = 5 - j;
				y = 8;
				j = 0;
				break;
			}
			j -= 4;
			if (j <= 6) {
				x = 1;
				y = 8 - j;
				j = 0;
				break;
			}
			j -= 6;
			if (j <= 3) {
				x = 1 + j;
				y = 2;
				j = 0;
				break;
			}
			j -= 3;
			if (j <= 5) {
				x = 4;
				y = 2 + j;
				j = 0;
				break;
			}
			j -= 5;
			if (j <= 2) {
				x = 4 - j;
				y = 7;
				j = 0;
				break;
			}
			j -= 2;
			if (j <= 4) {
				x = 2;
				y = 7 - j;
				j = 0;
				break;
			}
			j -= 4;
			if (j <= 1) {
				x = 3;
				y = 3;
				j = 0;
				break;
			}
			j -= 1;
			if (j <= 3) {
				x = 3;
				y = 3 + j;
				j = 0;
				break;
			}
			j -= 3;

		}

		return (x - 1) + (y - 1) * 5;
	}

	private boolean canShowTradeDialog() {
		pc = null;
		hc = null;
		PlayersForTrade[0] = board.getHuman();
		for (CellView cv : cells) {
			if (cv.isChecked()) {
				if (cv.getOwner() == PlayersForTrade[0].id) {
					hc = (TradeableCell) board.getCells()
							.get(cv.getObjCellId());
				} else {
					pc = (TradeableCell) board.getCells()
							.get(cv.getObjCellId());
					PlayersForTrade[1] = board.getPlayers().get(cv.getOwner());
				}
			}
		}
		if (hc == null || pc == null) {
			return false;
		}
		return true;
	}

	/*
	 * Clears cell checks if cell can't be trade
	 */
	private void cleanUpChecks() {
		for (Cell c : board.getCells()) {
			if (c instanceof TradeableCell) {
				TradeableCell tc = (TradeableCell) c;
				CellView cv = cells[tc.index];
				if (cv.isChecked()
						&& (tc.getOwner() == null || tc.getInfrasturcture() > 0 || tc
								.isMortgage())) {
					cv.setChecked(false);
				}
			}
		}
	}

	private LinearLayout composeCellLayoutForDialog(int vw, int vh,
			TradeableCell tc) {
		LinearLayout.LayoutParams paramsicon, paramstext, paramsstars;

		paramsicon = new LinearLayout.LayoutParams(vh, vh);
		paramstext = paramsstars = new LinearLayout.LayoutParams((vw - vh) / 2,
				vh);
		LinearLayout l = new LinearLayout(this);
		l.setOrientation(LinearLayout.HORIZONTAL);
		l.setBackgroundDrawable(bundle.sets[tc.getCorpid()]);
		ImageView iconv = new ImageView(this);
		iconv.setAdjustViewBounds(true);
		iconv.setImageDrawable(bundle.cells[tc.index + 1]);
		iconv.setLayoutParams(paramsicon);
		TextView tv;
		tv = new TextView(this);
		tv.setLayoutParams(paramstext);
		tv.setText("" + tc.getPrice());
		ImageView iv = new ImageView(this);
		iv.setAdjustViewBounds(true);
		iv.setLayoutParams(paramsstars);
		if (!tc.isMortgage()) {
			iv.setImageDrawable(bundle.starsh[tc.getInfrasturcture()]);
		} else {
			iv.setImageDrawable(bundle.mortgage[1]);
		}
		// iv.setMinimumWidth(vw);
		l.addView(iconv);
		l.addView(tv);
		l.addView(iv);
		return l;
	}

	private void createBoard(Board board) {
		dispatcher.setOnRequestTrade(this);
		if (board == null) {
			ResourceLoader rl = new ResourceLoader(this);
			BoardInfo boardInfo = rl.loadTheme(gameOptions.getTheme());
			dispatcher.setBoard(boardInfo.getBoard());
			this.board = dispatcher.getBoard();
			for (int i = 0; i < gameOptions.getPlayersNum(); i++) {
				Player p = new Player();
				p.money = gameOptions.getStartMoney();
				if (i == 0)
					p.isHuman = true;
				p.id = i;
				this.board.getPlayers().add(p);
			}
			this.board.setCurrentPlayer(this.board.getPlayers().get(0));
		} else {
			dispatcher.setBoard(board);
			this.board = dispatcher.getBoard();
		}

		populateGameBoard(this, gameBoard);
	}

	public void doBuy() {
		board.doBuy();
		updateCells();
	}

	public void dropDiceStep() {
		final int step = dispatcher.doSteps();
		switch (step) {
		case EventDispatcher.STEP_GAMEOVER: {
			if (board.getCurrentPlayer().isHuman) {
				showToast("GameOver");
			}
			break;
		}
		case EventDispatcher.STEP_PROCCESCELL: {
			// /Cell updated
			updateCells();
			if (!gameOptions.isAnimate()) {
				// continue processing
				dropDiceStep();
			}
			break;
		}
		case EventDispatcher.STEP_TRADEREQ: {
			// / Trade dialog shown
			Log.d("", "Show trade");
			break;
		}
		case EventDispatcher.STEP_FINISHED: {
			updateCells();
			updateControls();
			showOnCellDialog();
			break;
		}
		case EventDispatcher.STEP_LOSE: {
			if (board.getCurrentPlayer().lose) {
				showToast("Player lost game");
			}
		}
		default: {
			dropDiceStep();
		}
		}
	}

	public Board getBoard() {
		return board;
	}

	public EventDispatcher getDispatcher() {
		return dispatcher;
	}

	/**
	 * @return the gameOptions
	 */
	public GameOptions getGameOptions() {
		return gameOptions;
	}

	private LinearLayout getLineralLayout(LinearLayout.LayoutParams layoutParams) {
		final LinearLayout l = new LinearLayout(this);
		l.setLayoutParams(layoutParams);
		return l;
	}

	public List<CellView> getOpenentsCells() {
		List<CellView> list = new ArrayList<CellView>();
		for (int i = 0; i < cells.length; i++) {
			CellView cv = cells[i];
			if (cv.getOwner() != board.getCurrentPlayer().id) {
				list.add(cv);
			}
		}
		return list;
	}

	public List<CellView> getPlayerCells() {
		List<CellView> list = new ArrayList<CellView>();
		for (int i = 0; i < cells.length; i++) {
			CellView cv = cells[i];
			if (cv.getOwner() == board.getCurrentPlayer().id) {
				list.add(cv);
			}
		}
		return list;
	}

	public List<CellView> getSellectedCells(boolean includeStrars) {
		List<CellView> list = new ArrayList<CellView>();
		for (int i = 0; i < cells.length; i++) {
			CellView cv = cells[i];
			if (cv.isChecked()) {
				if (!includeStrars && cv.getStras() != 0) {
					continue;
				}

				list.add(cv);

			}
		}
		return list;
	}

	public List<CellView> getTradableCells() {
		List<CellView> list = new ArrayList<CellView>();
		for (int i = 0; i < cells.length; i++) {
			CellView cv = cells[i];
			if (cv.isClickable()) {
				list.add(cv);
			}
		}
		return list;
	}

	protected void initDialog(Dialog d, int layoutID) {
		d.setContentView(layoutID);
		View cv = d.findViewById(R.id.close);
		cv.setOnClickListener(new OnCloseDismiss(d));
	}

	private void invalidate() {
		findViewById(R.id.main).invalidate();
	}

	// private void loadCards() throws IOException {
	// InputStream fio = getAssets().open("game/Classic/msg.txt");
	// Scanner scanner = new Scanner(fio, "UTF-8");
	// while (scanner.hasNextLine()) {
	// String tmp = scanner.nextLine();
	// String[] vals = tmp.split("\\t");
	// int type = Integer.parseInt(vals[0]);
	// int pos = Integer.parseInt(vals[1]);
	// int price = Integer.parseInt(vals[2]);
	// String message = vals[3];
	// Card card = new Card(message, pos, price, type, type == Card.PRIZE);
	//
	// if (type == Card.PRIZE)
	// dispatcher.addCard(card);
	// else
	// dispatcher.addChance(card);
	// }
	// }

	private void movePlayer(int p, ImageView pv, CellView cv) {
		FrameLayout.LayoutParams params;
		params = new FrameLayout.LayoutParams(cw / 4, ch / 4);
		params.setMargins(0, p * cw / 8, 0, 0);
		
		ViewHelper.setTranslationX(pv,cv.getDesiredX());
		ViewHelper.setTranslationY(pv,cv.getDesiredY());
		pv.setLayoutParams(params);
		// if (pv.getParent() != null) {
		// pv.getParent().requestLayout();
		// }
	}

	protected void moveto(int cellID) {
		board.moveToPosition(cellID);
		updateCells();

		updateControls();
		showOnCellDialog();

	}

	protected void onCellClick(CellView cv) {
		Cell c = board.getCells().get(cv.getObjCellId());
		if (c instanceof TradeableCell
				&& ((TradeableCell) c).getOwner() != null) {
			TradeableCell tc = (TradeableCell) c;
			if (tc.getInfrasturcture() > 0 || tc.isMortgage())
				return;
			if (!cv.isChecked()) {
				Player p = tc.getOwner();

				if (p.isHuman) {
					for (TradeableCell ttc : p.myprops) {
						cells[ttc.index].setChecked(false);
					}
				} else {
					for (int i = 0; i < cells.length; i++) {
						CellView tcv = cells[i];

						if (tcv.getOwner() >= 0
								&& !board.getPlayers().get(tcv.getOwner()).isHuman)
							if (tcv.isChecked())
								tcv.setChecked(false);
					}
				}

			}
			cv.setChecked(!cv.isChecked());

		}
	}

	protected void onCellInfo(CellView v) {
		cellInfoID = v.getObjCellId();
		showDialogWindow(CELLINFO);

	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Create game dispatcher. It will hold board config
		dispatcher = new EventDispatcher();

		gameOptions = ((MonoplyApplication) getApplication()).getGameOptions();
		// prepare instance for resources
		bundle = ResourceBundle.getInstance(this);

		// prepare dashboard
		/* First, get the Display from the WindowManager */
		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
				.getDefaultDisplay();
		// go full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/* Now we can retrieve all display-related infos */

		display = getWindowManager().getDefaultDisplay();
		// Point size = new Point();
		// display.getSize(size);
		// fwidth = size.x;
		// fheight = size.y;
		// backward compatibility
		fwidth = display.getWidth();
		fheight = display.getHeight();
		rotation = display.getRotation();
		setContentView(R.layout.board);
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		gameBoard = (FrameLayout) findViewById(R.id.board);
		if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
			int h1 = (fheight - 25);
			int h2 = fwidth / 5 * 8;
			height = h1 < h2 ? h1 : h2;
			cw = fwidth / 5;
			ch = height / 8;
			cellsMap = CellInfo.getDefaultV();
			findViewById(R.id.board).setBackgroundResource(R.drawable.board);
		} else {
			int h1 = (fheight - 25);
			int h2 = fwidth / 8 * 5;
			height = h1 < h2 ? h1 : h2;
			cw = fwidth / 8;
			ch = (height) / 5;
			cellsMap = CellInfo.getDefaultH();
			Drawable d = getResources().getDrawable(R.id.board);
			Drawable rd = my.monopoly.util.RotateDrawable.rotateDrawable(90, d);
			findViewById(R.id.board).setBackgroundDrawable(rd);
		}
		width = fwidth;
		gameBoard.setLayoutParams(new LinearLayout.LayoutParams(width, height));
		SharedPreferences gameSettings = getSharedPreferences("Monoply2D",
				MODE_PRIVATE);
		currPlayerView = (ImageView) findViewById(R.id.CurrPlayer);
		dice1View = (ImageView) findViewById(R.id.dice1);
		dice2View = (ImageView) findViewById(R.id.dice2);
		ad1 = new AnimationDrawable();
		ad1.setOneShot(false);
		ad1.addFrame(getResources().getDrawable(R.drawable.dice1), 100);
		ad1.addFrame(getResources().getDrawable(R.drawable.dice2), 100);
		ad1.addFrame(getResources().getDrawable(R.drawable.dice3), 100);
		ad1.addFrame(getResources().getDrawable(R.drawable.dice4), 100);
		ad1.addFrame(getResources().getDrawable(R.drawable.dice5), 100);
		ad1.addFrame(getResources().getDrawable(R.drawable.dice6), 100);
		ad2 = ad1;
		ad1 = new AnimationDrawable();
		ad1.setOneShot(false);
		ad1.addFrame(getResources().getDrawable(R.drawable.dice1), 110);
		ad1.addFrame(getResources().getDrawable(R.drawable.dice2), 110);
		ad1.addFrame(getResources().getDrawable(R.drawable.dice3), 110);
		ad1.addFrame(getResources().getDrawable(R.drawable.dice4), 110);
		ad1.addFrame(getResources().getDrawable(R.drawable.dice5), 110);
		ad1.addFrame(getResources().getDrawable(R.drawable.dice6), 110);
		LinearLayout dcs = (LinearLayout) findViewById(R.id.dices);
		dcs.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return onDiceTouchEvent(v, event);
			}
		});

		info = findViewById(R.id.infot);
		info.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialogWindow(INFO);
			}
		});

		go = findViewById(R.id.go);
		go.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialogWindow(GO);
			}
		});

		sale_build = findViewById(R.id.sellt_build);
		sale_build.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialogWindow(SALE_BUILD);
			}
		});
		trade = findViewById(R.id.tradet);
		trade.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (canShowTradeDialog()) {
					showDialogWindow(TRADE);
				}
			}
		});

		undo = findViewById(R.id.undo);
		undo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// undo
				if (undoStack.canUndo()) {
					board = undoStack.loadBoardFromUndoStack();
					setBoard(board);
					dispatcher.setBoard(board);
					// createBoard(board);
					updateCells();
					updateControls();
				}

			}
		});
		BuildDialogs();
		// try {
		// loadCards();
		// } catch (IOException e1) {
		// showToast("Cant load cards\n\r Will play without cards");
		// e1.printStackTrace();
		// }
		String boardData = gameSettings.getString("board", null);
		if (boardData != null) {

			setBoard(BoardLoader.loadFromSting(boardData));
			dispatcher.setBoard(board);
		}
		createBoard(board);
		updateCells();
		undoStack = new BoardUndoStack(board);
		invalidate();
		updateControls();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TRADE: {
			return tradeD;
		}
		case TRADEREQUEST: {
			return tradeOnReqD;
		}
		case SALE_BUILD: {
			return sale_buildD;
		}
		case BUY: {
			return buyD;
		}
		case INFO: {
			return infoD;
		}
		case EXITPRISON: {
			return exitD;
		}
		case GO: {
			return goD;
		}
		case TURNINFO: {
			return turnInfoD;
		}
		case CELLINFO: {
			return cellInfoD;
		}
		default:
			return super.onCreateDialog(id);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		String base64 = BoardLoader.saveToString(board);
		if (base64 != null) {
			SharedPreferences gameSettings = getSharedPreferences("Monoply2D",
					MODE_PRIVATE);
			SharedPreferences.Editor prefEditor = gameSettings.edit();
			prefEditor.putString("board", base64);
			prefEditor.commit();
		}
		super.onDestroy();
	}

	public boolean onDiceTouchEvent(View v, MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN && ad1 != null
				&& !animationActive) {
			if (exitDialogResult == false && board.canLeavePrison()) {

				showDialogWindow(EXITPRISON);
			} else {
				exitDialogResult = false;
				if (board.canMove()) {
					undoStack.saveBoardToUndoStack(board,
							gameOptions.getUndoCount());
					if (!ad1.isRunning()) {
						dice1View.setImageDrawable(ad1);
						dice2View.setImageDrawable(ad2);
						ad1.start();
						ad2.start();
						TurnHandler handler = new TurnHandler();
						Message msg = new Message();
						msg.what = 1;
						handler.sendMessageDelayed(msg, 500);
					}
				} else {
					if (!board.isGameOver()) {
						showToast("Can't move whith negative balance");
					} else {
						showToast("GAME OVER");
					}
				}
				return true;
			}
		}
		return super.onTouchEvent(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPrepareDialog(int, android.app.Dialog)
	 */
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {

		switch (id) {
		case INFO: {
			populateInfoTablets();
			break;
		}
		case TRADE: {
			populateTradeDialog();
			break;
		}
		case TRADEREQUEST: {
			// Dialog already populated during requestTrade(...) execution
			break;
		}
		case BUY: {
			populateBuyDialog();
			break;
		}
		case SALE_BUILD: {
			populateSell_Build_Dialog();
			break;
		}
		case TURNINFO: {
			populateTurnInfo_Dialog();
			break;
		}
		case EXITPRISON: {
			populateExitPrison();
			break;
		}
		case CELLINFO: {
			populateCellInfo();
			break;
		}
		}
	}

	private void populateBuyDialog() {
		Player p = board.getCurrentPlayer();
		((ImageView) buyD.findViewById(R.id.playericon))
				.setImageDrawable(bundle.players[p.id + 1]);
		((TextView) buyD.findViewById(R.id.playermoney)).setText(p.money + "");
		ImageView cellImg = (ImageView) buyD.findViewById(R.id.cellicon);
		cellImg.setImageDrawable(bundle.sets[board.getCorpID()]);
		cellImg.setBackgroundDrawable(bundle.cells[board.getCellID() + 1]);
		((TextView) buyD.findViewById(R.id.priceText)).setText(board.getPrice()
				+ "");
	}

	private void populateCellInfo() {
		final CellView cv = (CellView) cellInfoD.findViewById(R.id.cellView);
		final Cell c = board.getCells().get(cellInfoID);
		final TownCell townc = (c instanceof TownCell) ? (TownCell) c : null;
		final RailroadCell rc = (c instanceof RailroadCell) ? (RailroadCell) c
				: null;
		final WaterElCell wc = (c instanceof WaterElCell) ? (WaterElCell) c
				: null;
		final TradeableCell tc = (c instanceof TradeableCell) ? (TradeableCell) c
				: null;
		populateCellView(c, cv);
		ImageView iv = (ImageView) cellInfoD.findViewById(R.id.info_priceIcon);
		iv.setBackgroundDrawable(bundle.cells[c.index + 1]);
		if (tc != null) {
			iv.setImageDrawable(bundle.sets[tc.getCorpid()]);
		} else {
			iv.setImageDrawable(null);
		}

		TextView tv;

		// info
		tv = (TextView) cellInfoD.findViewById(R.id.info_CellName);
		tv.setText(c.getName());
		tv = (TextView) cellInfoD.findViewById(R.id.info_currRent);
		if (tc != null)
			tv.setText(tc.getRent() + "");
		else
			tv.setText("-");

		// rent
		tv = (TextView) cellInfoD.findViewById(R.id.info_rent);
		if (tc != null)
			tv.setText(tc.getMinRent() + "");
		else
			tv.setText("-");
		// strar1
		tv = (TextView) cellInfoD.findViewById(R.id.info_rentStar1);
		if (townc != null)
			tv.setText(townc.getHouse1() + "");
		else if (rc != null)
			tv.setText(rc.getMinRent() * 2 + "");
		else if (wc != null)
			tv.setText(wc.getMinRent() * 2 + "");
		else
			tv.setText("-");
		// strar2
		tv = (TextView) cellInfoD.findViewById(R.id.info_rentStar2);
		if (townc != null)
			tv.setText(townc.getHouse2() + "");
		else if (rc != null)
			tv.setText(rc.getMinRent() * 3 + "");
		else
			tv.setText("-");
		// strar3
		tv = (TextView) cellInfoD.findViewById(R.id.info_rentStar3);
		if (townc != null)
			tv.setText(townc.getHouse3() + "");
		else if (rc != null)
			tv.setText(rc.getMinRent() * 4 + "");
		else
			tv.setText("-");
		// strar4
		tv = (TextView) cellInfoD.findViewById(R.id.info_rentStar4);
		if (townc != null)
			tv.setText(townc.getHouse4() + "");
		else
			tv.setText("-");
		// strar5
		tv = (TextView) cellInfoD.findViewById(R.id.info_rentStar5);
		if (townc != null)
			tv.setText(townc.getHotelrent() + "");
		else
			tv.setText("-");

		// build
		// price
		tv = (TextView) cellInfoD.findViewById(R.id.info_price);
		if (tc != null)
			tv.setText(tc.getPrice() + "");
		else
			tv.setText("-");
		// strar1
		tv = (TextView) cellInfoD.findViewById(R.id.info_buildStar1);
		if (townc != null)
			tv.setText(townc.getBuildhouse() + "");
		else
			tv.setText("-");
		// strar2
		tv = (TextView) cellInfoD.findViewById(R.id.info_buildStar2);
		if (townc != null)
			tv.setText(townc.getBuildhouse() + "");
		else
			tv.setText("-");
		// strar3
		tv = (TextView) cellInfoD.findViewById(R.id.info_buildStar3);
		if (townc != null)
			tv.setText(townc.getBuildhouse() + "");
		else
			tv.setText("-");
		// strar4
		tv = (TextView) cellInfoD.findViewById(R.id.info_buildStar4);
		if (townc != null)
			tv.setText(townc.getBuildhouse() + "");
		else
			tv.setText("-");
		// strar5
		tv = (TextView) cellInfoD.findViewById(R.id.info_buildStar5);
		if (townc != null)
			tv.setText(townc.getBuildhotel() + "");
		else
			tv.setText("-");

	}

	private void populateCellView(Cell c, CellView cv) {
		cv.setCell(c.index + 1);
		if (c instanceof TradeableCell) {
			TradeableCell tc = (TradeableCell) c;
			cv.setSetid(tc.getCorpid());
			if (tc.getOwner() != null) {
				cv.setOwner(tc.getOwner().id);
			} else {
				cv.setOwner(-1);
			}
			cv.setStras(tc.getInfrasturcture());
			cv.setMortgage(tc.isMortgage());
			// cv.setName(tc.getName());
		} else {
			cv.setOwner(-1);
			cv.setSetid(0);
			cv.setStras(0);
			cv.setMortgage(false);
		}
	}

	private void populateExitPrison() {
		TextView tv = (TextView) exitD.findViewById(R.id.cardsnum);
		tv.setText("" + board.getNextPlayer().prisoncard.size());
		tv = (TextView) exitD.findViewById(R.id.playermoney);
		tv.setText("" + board.getNextPlayer().money);
		tv = (TextView) exitD.findViewById(R.id.turnsInPrisonLeft);
		tv.setText("" + board.getNextPlayer().prison);
		View ev = exitD.findViewById(R.id.exitprison);
		ev.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				board.exitPrisonHuman();
				exitD.dismiss();
			}
		});
		exitD.setOnDismissListener(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				exitDialogResult = true;

			}
		});
	}

	private void populateGameBoard(Context context, FrameLayout layout) {
		FrameLayout.LayoutParams params;

		for (int i = 0; i < board.cellsCount; i++) {
			CellView cv = new CellView(context);
			cells[i] = cv;
			int dx, dy;
			if (fwidth < fheight) {

				params = new FrameLayout.LayoutParams(cw, ch);
				dx = (i % 5) * (cw);
				dy = (i / 5) * (ch);
				params.setMargins(dx, dy, 0, 0);
			}

			else {
				params = new FrameLayout.LayoutParams(cw, ch);
				dx = (i % 8) * (cw);
				dy = (i / 8) * (ch);
				params.setMargins(dx, dy, 0, 0);
			}
			cv.setLayoutParams(params);
			cv.setDesiredX(dx);
			cv.setDesiredY(dy);
			// cv.setOnClickListener(new View.OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// onCellClick((CellView) v);
			// }
			// });
			cv.setOnTouchListener(new View.OnTouchListener() {
				private long touchStrart;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					Log.d("CellTouch", event.getAction() + "");
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						touchStrart = System.currentTimeMillis();
					}
					if (event.getAction() == MotionEvent.ACTION_UP) {
						long diff = System.currentTimeMillis() - touchStrart;
						if (diff < 500) {
							onCellClick((CellView) v);
						} else {
							onCellInfo((CellView) v);
						}
					}
					return true;
				}
			});
			layout.addView(cv);
		}

		CellView[] cellViews = new CellView[cells.length];

		for (int i = 0; i < cells.length; i++) {
			cellViews[i] = cells[cellsMap[i]];
			cellViews[i].setObjCellId(i);
		}
		cells = cellViews;

		for (int i = 0; i < board.cellsCount; i++) {
			Cell c = board.getCells().get(i);
			CellView cv = cells[i];

			populateCellView(c, cv);

		}
		for (int i = 0; i < board.getPlayers().size(); i++) {
			ImageView p = new ImageView(context);
			PlayersViews[i] = p;
			p.setImageDrawable(bundle.players[i + 1]);
			postionPlayerToCell(i, board.getPlayers().get(i).position);
			layout.addView(p);
		}
	}

	private void populateInfoTablets() {
		LinearLayout[] vps = new LinearLayout[4];
		vps[0] = (LinearLayout) infoD.findViewById(R.id.vpanel1);
		vps[1] = (LinearLayout) infoD.findViewById(R.id.vpanel2);
		vps[2] = (LinearLayout) infoD.findViewById(R.id.vpanel3);
		vps[3] = (LinearLayout) infoD.findViewById(R.id.vpanel4);
		int ipw = (int) (fwidth * 0.9 / 4);
		int iph = (int) (fheight * 0.9 / 20);
		int vw, vh;
		LinearLayout.LayoutParams paramsImage, paramsText;

		vw = iph;
		vh = iph;
		paramsImage = new LinearLayout.LayoutParams(vh, vh);
		vw = ipw - iph;
		paramsText = new LinearLayout.LayoutParams((vw), vh);
		for (int i = 0; i < board.getPlayers().size(); i++) {
			vps[i].removeAllViews();
			vps[i].setLayoutParams(new LinearLayout.LayoutParams(ipw,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			ImageView pv = new ImageView(this);
			TextView tv = new TextView(this);
			pv.setImageDrawable(bundle.players[i + 1]);
			pv.setLayoutParams(paramsImage);
			pv.setScaleType(ImageView.ScaleType.FIT_START);
			tv.setText(getBoard().getPlayers().get(i).money + "");
			tv.setLayoutParams(paramsText);
			vps[i].addView(pv);
			vps[i].addView(tv);
			for (TradeableCell tc : board.getPlayers().get(i).myprops) {
				LinearLayout l = composeCellLayoutForDialog(ipw, iph, tc);
				vps[i].addView(l);
			}
		}
		infoD.findViewById(R.id.infoPanelsHolder).invalidate();
	}

	private void populateSell_Build_Dialog() {
		List<TradeableCell> cells = board.getCurrentPlayer().myprops;
		LinearLayout vp = (LinearLayout) sale_buildD.findViewById(R.id.vpanel1);
		vp.removeAllViews();
		((ImageView) sale_buildD.findViewById(R.id.playericon))
				.setImageDrawable(bundle.players[currPlayerID + 1]);
		((TextView) sale_buildD.findViewById(R.id.playermoney)).setText(board
				.getCurrentPlayer().money + "");
		int ipw = (int) (fwidth * 0.9);
		int iph = (int) (fheight * 0.8 / 20);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ipw,
				iph);
		LinearLayout.LayoutParams paramico = new LinearLayout.LayoutParams(
				(int) (iph * 1.5), iph);
		LinearLayout.LayoutParams parambutton = new LinearLayout.LayoutParams(
				(iph * 2), iph);
		LinearLayout.LayoutParams paramprice = new LinearLayout.LayoutParams(
				(ipw - iph * 8) / 2, iph);
		for (TradeableCell c : cells) {
			LinearLayout l = getLineralLayout(params);
			ImageView cellicon = new ImageView(this);
			cellicon.setLayoutParams(paramico);
			cellicon.setImageDrawable(bundle.cells[c.index + 1]);
			TextView price = new TextView(this);
			price.setLayoutParams(paramprice);
			price.setText(c.getPrice() + "");
			ImageView stars = new ImageView(this);
			stars.setLayoutParams(paramprice);
			// stars.setImageDrawable(bundle.stars[c.getInfrasturcture()]);
			// convert to drawable
			ImageView plus = new ImageView(this);
			plus.setLayoutParams(parambutton);
			// plus.setImageDrawable(R.drawable.plus);
			plus.setEnabled(false);
			ImageView minus = new ImageView(this);
			minus.setLayoutParams(parambutton);
			minus.setImageDrawable(bundle.minus);
			ImageView mortgage = new ImageView(this);
			mortgage.setLayoutParams(parambutton);
			// Will be set on build update

			price.setId(1);
			plus.setId(2);
			minus.setId(3);
			stars.setId(4);
			mortgage.setId(5);
			l.addView(cellicon);
			l.addView(price);
			l.addView(stars);
			l.addView(plus);
			l.addView(minus);
			l.addView(mortgage);
			vp.addView(l);
			minus.setOnClickListener(new buildListener(l, c, BUILD_MINUS, vp));
			plus.setOnClickListener(new buildListener(l, c, BUILD_PLUS, vp));
			mortgage.setOnClickListener(new buildListener(l, c, BUILD_MORTGAGE,
					vp));

			buildupdate(l, c, vp);
		}
		vp.invalidate();
	}

	private void populateTradeDialog() {
		LinearLayout[] vps = new LinearLayout[2];
		vps[0] = (LinearLayout) tradeD.findViewById(R.id.vpanel1);
		vps[1] = (LinearLayout) tradeD.findViewById(R.id.vpanel2);
		int ipw = (int) (fwidth / 2 * 0.9);
		int iph = (int) (fheight * 0.8 / 20);
		int vw, vh;
		LinearLayout.LayoutParams params;
		vw = (int) (ipw * 0.5);
		vh = (iph);
		params = new LinearLayout.LayoutParams(vw, vh);
		for (int i = 0; i < PlayersForTrade.length; i++) {
			vps[i].removeAllViews();
			vps[i].setLayoutParams(new LinearLayout.LayoutParams(vw * 2,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			ImageView pv = new ImageView(this);
			TextView tv = new TextView(this);
			pv.setImageDrawable(bundle.players[PlayersForTrade[i].id + 1]);
			pv.setLayoutParams(params);
			pv.setScaleType(ImageView.ScaleType.FIT_START);
			tv.setText(PlayersForTrade[i].money + "");
			tv.setLayoutParams(params);
			vps[i].addView(pv);
			vps[i].addView(tv);
			LinearLayout l = composeCellLayoutForDialog(ipw, iph, i == 0 ? hc
					: pc);
			vps[i].addView(l);
		}

		tradeD.findViewById(R.id.infoPanelsHolder).invalidate();
		View button = tradeD.findViewById(R.id.tradebutton);
		TextView price = (TextView) tradeD.findViewById(R.id.tradeprice);
		price.setText(pc.getPrice() - hc.getPrice() + "");
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final int priceDiff = pc.getPrice() - hc.getPrice();
				// TextView tv = (TextView)
				// tradeD.findViewById(R.id.tradeprice);
				// do trade
				final Player player = hc.getOwner();
				final Player partner = pc.getOwner();
				player.money -= priceDiff;
				partner.money += priceDiff;
				hc.setOwner(partner);
				pc.setOwner(player);
				tradeD.dismiss();
				updateCells();

			}
		});
	}

//	 private void postionPlayerToCell_STRANGE(int p, int c) {
//	
//	 ImageView pv = Players[p];
//	 CellView cv = cells[c];
//	 CellView oldcv;
//	 if (oldcell[p] >= 0 && oldcell[p] != c && gameOptions.isAnimate()
//	 && board.getCurrentPlayer().id == p) {
//	 oldcv = cells[oldcell[p]];
//	 AnimationSet playerMov1 = new AnimationSet(true);
//	 ScaleAnimation scale1 = new ScaleAnimation(1, 2, 1, 2);
//	 ScaleAnimation scale2 = new ScaleAnimation(1, 0.5f, 1, 0.5f);
//	 scale1.setDuration(1500);
//	
//	 scale2.setDuration(1500);
//	 scale2.setStartOffset(1500);
//	 float ox = 0; // oldcv.getDesiredX();
//	 float oy = 0; // oldcv.getDesiredY();
//	 float dx = cv.getDesiredX() - oldcv.getDesiredX();
//	 float dy = cv.getDesiredY() - oldcv.getDesiredY();
//	 TranslateAnimation trans1 = new TranslateAnimation(
//	 Animation.ABSOLUTE, ox, Animation.ABSOLUTE, dx,
//	 Animation.ABSOLUTE, oy, Animation.ABSOLUTE, dy);
//	 ;
//	 TranslateAnimation trans2 = new TranslateAnimation(
//	 Animation.ABSOLUTE, 100, Animation.ABSOLUTE, 0,
//	 Animation.ABSOLUTE, 100, Animation.ABSOLUTE, 0);
//	
//	 trans1.setDuration(1500);
//	 trans2.setDuration(3000);
//	 trans2.setStartOffset(0);
//	 playerMov1.setFillAfter(true);
//	 // playerMov1.addAnimation(trans1);
//	 playerMov1.addAnimation(trans2);
//	 // playerMov1.addAnimation(scale1);
//	 // playerMov1.addAnimation(scale2);
//	 pv.clearAnimation();
//	 pv.setAnimation(playerMov1);
//	 trans1.setAnimationListener(new Animation.AnimationListener() {
//	
//	 @Override
//	 public void onAnimationStart(Animation animation) {
//	 Log.d("trans1", "strat");
//	 }
//	
//	 @Override
//	 public void onAnimationRepeat(Animation animation) {
//	 Log.d("trans1", "repeat");
//	
//	 }
//	
//	 @Override
//	 public void onAnimationEnd(Animation animation) {
//	 Log.d("trans1", "End");
//	
//	 }
//	 });
//	 trans2.setAnimationListener(new MyAnimationListener(pv, p, cv));
//	 invalidate();
//	
//	 playerMov1.start();
//	
//	 oldcell[p] = c;
//	 } else {
//	 oldcell[p] = c;
//	 movePlayer(p, pv, cv);
//	
//	 }
//	
//	 }

	private void populateTurnInfo_Dialog() {

		CellView cv = (CellView) turnInfoD.findViewById(R.id.cellView);
		populateCellView(board.getCurrCell(), cv);
		Player p = board.getCurrentPlayer();
		((ImageView) turnInfoD.findViewById(R.id.playericon))
				.setImageDrawable(bundle.players[p.id + 1]);
		((TextView) turnInfoD.findViewById(R.id.playermoney)).setText(p.money
				+ "");
		LinearLayout infoHolder = (LinearLayout) turnInfoD
				.findViewById(R.id.turnMessagesHolder);
		infoHolder.removeAllViews();
		for (BoardMessage bm : board.getBoardMessages()) {
			LinearLayout turnInfoLine = (LinearLayout) inflater.inflate(
					R.layout.turminfoline, infoHolder, false);
			if (bm.getPlayer() != null) {
				ImageView playerImg = (ImageView) turnInfoLine
						.findViewById(R.id.playericon);
				playerImg
						.setBackgroundDrawable(bundle.players[bm.getPlayer().id + 1]);
			}
			if (bm.getCell() != null) {
				ImageView cellImg = (ImageView) turnInfoLine
						.findViewById(R.id.cellicon);
				if (bm.getCell() instanceof TradeableCell) {
					TradeableCell tc = (TradeableCell) bm.getCell();
					cellImg.setImageDrawable(bundle.sets[tc.getCorpid()]);
				} else {
					cellImg.setImageDrawable(bundle.sets[0]);
				}
				cellImg.setBackgroundDrawable(bundle.cells[bm.getCell().index + 1]);
			}
			((TextView) turnInfoLine.findViewById(R.id.turnmessage)).setText(bm
					.getMessage() + "");
			infoHolder.addView(turnInfoLine);
		}
	}

	private void postionPlayerToCell(int p, int c) {

		ImageView pv = PlayersViews[p];
		CellView cv = cells[c];
		CellView oldcv;
		 if (oldcell[p] >= 0 && oldcell[p] != c && gameOptions.isAnimate()
		 && board.getCurrentPlayer().id == p) {
		 oldcv = cells[oldcell[p]];
		 ObjectAnimator animx = ObjectAnimator.ofFloat(pv, "translationX",
		 oldcv.getDesiredX(), cv.getDesiredX());
		 ObjectAnimator animy = ObjectAnimator.ofFloat(pv, "translationY",
		 oldcv.getDesiredY(), cv.getDesiredY());
		 ObjectAnimator anim1sx = ObjectAnimator.ofFloat(pv, "scaleX", 1, 2);
		 ObjectAnimator anim1sy = ObjectAnimator.ofFloat(pv, "scaleY", 1, 2);
		 ObjectAnimator anim2sx = ObjectAnimator.ofFloat(pv, "scaleX", 2, 1);
		 ObjectAnimator anim2sy = ObjectAnimator.ofFloat(pv, "scaleY", 2, 1);
		
		 anim1sx.setDuration(1000);
		 anim1sy.setDuration(1000);
		 anim2sx.setDuration(1000);
		 anim2sy.setDuration(1000);
		
		 anim2sx.setStartDelay(1000);
		 anim2sy.setStartDelay(1000);
		
		 animx.setDuration(2000);
		 animy.setDuration(2000);
		
		 AnimatorSet aset = new AnimatorSet();
		 aset.playTogether(animx, animy, anim1sx, anim1sy, anim2sx, anim2sy);
		 aset.addListener(new MyAnimationListener(pv, p, cv));
		 aset.start();
		 oldcell[p] = c;
		 } else {
		oldcell[p] = c;
		movePlayer(p, pv, cv);
		
		 }

	}

	@Override
	public void requestTrade(EventDispatcher d, TradeableCell pc,
			TradeableCell hc, int price) {
		if (!gameOptions.isShowTrade()) {
			dropDiceStep();
			return;
		}
		LinearLayout[] vps = new LinearLayout[2];
		vps[0] = (LinearLayout) tradeOnReqD.findViewById(R.id.vpanel1);
		vps[1] = (LinearLayout) tradeOnReqD.findViewById(R.id.vpanel2);
		int ipw = (int) (fwidth / 2 * 0.9);
		int iph = (int) (fheight * 0.8 / 20);
		int vw, vh;
		LinearLayout.LayoutParams params;
		vw = (int) (ipw * 0.5);
		vh = (iph);
		params = new LinearLayout.LayoutParams(vw, vh);
		for (int i = 0; i < vps.length; i++) {
			vps[i].removeAllViews();
			vps[i].setLayoutParams(new LinearLayout.LayoutParams(vw * 2,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			ImageView pv = new ImageView(this);
			TextView tv = new TextView(this);
			if (i == 0) {
				pv.setImageDrawable(bundle.players[hc.getOwner().id + 1]);
			} else {
				pv.setImageDrawable(bundle.players[pc.getOwner().id + 1]);
			}
			pv.setLayoutParams(params);
			pv.setScaleType(ImageView.ScaleType.FIT_START);
			if (i == 0) {
				tv.setText(getBoard().getPlayers().get(hc.getOwner().id).money
						+ "");

			} else {

				tv.setText(getBoard().getPlayers().get(pc.getOwner().id).money
						+ "");
			}
			tv.setLayoutParams(params);
			vps[i].addView(pv);
			vps[i].addView(tv);
			LinearLayout l;
			if (i == 0) {
				l = composeCellLayoutForDialog(ipw, iph, hc);
			} else {
				l = composeCellLayoutForDialog(ipw, iph, pc);
			}
			vps[i].addView(l);
		}
		tradeOnReqD.findViewById(R.id.infoPanelsHolder).invalidate();
		View button = tradeOnReqD.findViewById(R.id.tradebutton);
		TextView priceV = (TextView) tradeOnReqD.findViewById(R.id.tradeprice);
		priceV.setText(price + "");
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dispatcher.performTrade();
				tradeOnReqD.dismiss();
				updateCells();
				dropDiceStep();

			}
		});
		tradeOnReqD.setCanceledOnTouchOutside(true);
		tradeOnReqD.setOnCancelListener(new Dialog.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dropDiceStep();
			}
		});
		showDialogWindow(TRADEREQUEST);

	}

	public void setBoard(Board readObject) {
		board = readObject;
		board.isRestrore = true;
		currPlayerID = board.getCurrentPlayer().id;
		currPlayerView.setImageDrawable(bundle.players[currPlayerID + 1]);
	}

	public void setDispatcher(EventDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	/**
	 * @param gameOptions
	 *            the gameOptions to set
	 */
	public void setGameOptions(GameOptions gameOptions) {
		this.gameOptions = gameOptions;
	}

	public void showDialogWindow(int id) {
		showDialog(id);

	}

	private void showOnCellDialog() {
		// if (board.isPrison()) {
		// showDialog(PRISON);
		// showToast("Prison");
		// }
		// if (board.isGoToPrison()) {
		// // showDialog(GO_TO_PRISON);
		// showToast(board.getMessage());
		// }
		if (board.canBuy()) {
			if (board.getCurrentPlayer().isHuman)
				showDialogWindow(BUY);
			// showToast("Can buy");
		} else {
			if (gameOptions.isShowComputer()
					&& !board.getCurrentPlayer().isHuman) {
				showDialogWindow(TURNINFO);
			}
			if (gameOptions.isShowHuman() && board.getCurrentPlayer().isHuman) {
				showDialogWindow(TURNINFO);
			}
		}
		// if (board.isCard()) {
		// showDialog(CARD);
		// }
		// if (board.isTax()) {
		// // showDialog(TAX);
		// showToast(board.getMessage());
		// }
		// if (board.isChance()) {
		// showDialog(CHANCE);
		// }
	}

	private void showToast(String message) {
		Toast t = Toast.makeText(Monopoly2DActivity.this, message, 300);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
	}

	public void updateCells() {
		currPlayerID = board.getCurrentPlayer().id;
		for (int i = 0; i < board.cellsCount; i++) {
			updateCellView(i);
		}
		// Players
		for (int i = 0; i < board.getPlayers().size(); i++) {
			postionPlayerToCell(i, board.getPlayers().get(i).position);
		}
		cleanUpChecks();
	}

	private void updateCellView(CellView gc, Cell c) {
		if (c instanceof TradeableCell) {
			TradeableCell tc = (TradeableCell) c;
			gc.setStras(tc.getInfrasturcture());
			gc.setMortgage(tc.isMortgage());
			Player p = tc.getOwner();
			if (p != null) {
				gc.setOwner(p.id);
			} else {
				gc.setOwner(-1);
			}
		}

	}

	private void updateCellView(int i) {
		CellView gc = cells[i];
		Cell c = board.getCells().get(i);
		updateCellView(gc, c);
	}

	private void updateControls() {
		boolean state = false;
		float alpha = 0;
		if (board.getCurrentPlayer().isHuman) {
			state = true;
			alpha = 1;
		}
		sale_build.setEnabled(state);
		ViewHelper.setAlpha(sale_build,alpha);
		trade.setEnabled(state);
		ViewHelper.setAlpha(trade,alpha);
		if (undoStack.canUndo()) {
			undo.setEnabled(true);
			ViewHelper.setAlpha(undo,1);
		} else {
			undo.setEnabled(false);
			ViewHelper.setAlpha(undo,0);
		}
	}
}

class OnCloseDismiss implements View.OnClickListener {
	Dialog d;

	public OnCloseDismiss(Dialog d) {
		this.d = d;
	}

	@Override
	public void onClick(View v) {
		d.dismiss();
	}

}
