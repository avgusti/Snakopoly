package resource;

//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.util.Log;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import my.monopoly.R;

public final class ResourceBundle {

	public static final int[] DICES = { R.drawable.none, R.drawable.dice1,
			R.drawable.dice2, R.drawable.dice3, R.drawable.dice4,
			R.drawable.dice5, R.drawable.dice6 };
	public static final int PLUS = R.drawable.plus, MINUS = R.drawable.minus,
			BOARD = R.drawable.board;
	public static final int MORTGAGE_GET = R.drawable.mortgage_get,
			MORTGAGE_PAY = R.drawable.mortgage_pay;
	public static final int[] MORTGAGE = { R.drawable.none,
			R.drawable.mortgage_icon };
	public static final int[] STRASH = { R.drawable.none, R.drawable.starsh1,
			R.drawable.starsh2, R.drawable.starsh3, R.drawable.starsh4,
			R.drawable.starsh5 };
	public static final int[] STRASV = { R.drawable.none, R.drawable.starsv1,
			R.drawable.starsv2, R.drawable.starsv3, R.drawable.starsv4,
			R.drawable.starsv5 };
//	public static final int[] STRAS = { R.drawable.none, R.drawable.st1,
//			R.drawable.st2, R.drawable.st3, R.drawable.st4, R.drawable.st5 };
	public static final int[] PLAYERS = { R.drawable.none, R.drawable.p1,
			R.drawable.p2, R.drawable.p3, R.drawable.p4 };
	public static final int[] PLAYERS_FLAGS = { R.drawable.none,
			R.drawable.p1f, R.drawable.p2f, R.drawable.p3f, R.drawable.p4f };
	// /???
	public static final int[] SETS = { R.drawable.set0, R.drawable.set1,
			R.drawable.set2, R.drawable.set3, R.drawable.set4, R.drawable.set5,
			R.drawable.set6, R.drawable.set7, R.drawable.set8, R.drawable.set9,
			R.drawable.set10 };
	public static final int[] CELLS = { R.drawable.none, R.drawable.cell_go,
			R.drawable.cell_press, R.drawable.cell_copycenter,
			R.drawable.cell_tax, R.drawable.cell_bookstore, R.drawable.cell_wr,
			R.drawable.cell_pizza, R.drawable.cell_electricity,
			R.drawable.cell_bar, R.drawable.cell_restaurant,
			R.drawable.cell_prison, R.drawable.cell_hostel,
			R.drawable.cell_motel, R.drawable.cell_chance,
			R.drawable.cell_hotel, R.drawable.cell_nr, R.drawable.cell_radio,
			R.drawable.cell_card, R.drawable.cell_movie, R.drawable.cell_tv,
			R.drawable.cell_free, R.drawable.cell_cellphone,
			R.drawable.cell_tax, R.drawable.cell_game,
			R.drawable.cell_computer, R.drawable.cell_er, R.drawable.cell_gym,
			R.drawable.cell_water, R.drawable.cell_pool, R.drawable.cell_golf,
			R.drawable.cell_gojail, R.drawable.cell_motorcicle,
			R.drawable.cell_car, R.drawable.cell_chance, R.drawable.cell_track,
			R.drawable.cell_sr, R.drawable.cell_bus, R.drawable.cell_card,
			R.drawable.cell_railroad, R.drawable.cell_airport };

	public static final int[] PLAYERCOLORS = { R.drawable.none,
			R.color.player1, R.color.player2, R.color.player3, R.color.player4 };
	public static final int[] SETCOLORS = { R.drawable.none, R.color.set1,
			R.color.set2, R.color.set3, R.color.set4, R.color.set5,
			R.color.set6, R.color.set7, R.color.set8, R.color.set9,
			R.color.set10 };

	private static ResourceBundle bundle;

	public static ResourceBundle getInstance(Context context) {
		return new ResourceBundle(context);
	}

	public static ResourceBundle getSharedInstance() {
		if (bundle != null) {
			return bundle;
		} else {
			throw new RuntimeException("Shared instance should be init first");
		}

	}

	public static ResourceBundle setSharedInstance(Context context) {
		if (bundle != null)
			Log.d(ResourceBundle.class.getName(),
					"Shared intstance already set");
		bundle = new ResourceBundle(context);
		return bundle;
	}

	public static void resetBundle() {
		bundle = null;
	}

	private Context context;

	private ResourceBundle(Context context) {
		this.context = context;
		init();
	}

	public Drawable[] dices, stars, players, playersFlags, sets, cells, starsh,
			starsv, mortgage;
	public Drawable plus, minus, mortgage_get, mortgage_pay, board;

	private void init() {
		dices = new Drawable[DICES.length];
		//stars = new Drawable[STRAS.length];
		starsh = new Drawable[STRASH.length];
		starsv = new Drawable[STRASV.length];
		sets = new Drawable[SETS.length];
		players = new Drawable[PLAYERS.length];
		playersFlags = new Drawable[PLAYERS.length];
		cells = new Drawable[CELLS.length];
		mortgage = new Drawable[MORTGAGE.length];
		load(dices, DICES);
//		load(stars, STRAS);
		load(starsh, STRASH);
		load(starsv, STRASV);
		load(sets, SETS);
		load(players, PLAYERS);
		load(playersFlags, PLAYERS_FLAGS);
		load(cells, CELLS);
		load(mortgage, MORTGAGE);

		plus = context.getResources().getDrawable(PLUS);
		minus = context.getResources().getDrawable(MINUS);
		mortgage_get = context.getResources().getDrawable(MORTGAGE_GET);
		mortgage_pay = context.getResources().getDrawable(MORTGAGE_PAY);
		board = context.getResources().getDrawable(BOARD);
	}

	public void load(Drawable[] d, int[] ids) {
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] == 0)
				d[i] = null;
			else
				d[i] = context.getResources().getDrawable(ids[i]);
		}
	}

	public Drawable[] getDices() {
		return dices;
	}

	public void setDices(Drawable[] dices) {
		this.dices = dices;
	}

//	public Drawable[] getStras() {
//		return stars;
//	}
//
//	public void setStras(Drawable[] stras) {
//		this.stars = stras;
//	}

	public Drawable[] getPlayers() {
		return players;
	}

	public void setPlayers(Drawable[] players) {
		this.players = players;
	}

	public Drawable[] getSets() {
		return sets;
	}

	public void setSets(Drawable[] sets) {
		this.sets = sets;
	}

	public Drawable[] getCells() {
		return cells;
	}

	public void setCells(Drawable[] cells) {
		this.cells = cells;
	}

}
