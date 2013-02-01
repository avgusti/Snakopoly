package resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import my.monopoly.beans.BoardInfo;
import my.monopoly.beans.CellInfo;
import object.Board;
import object.Card;
import object.Cell;
import object.CellHelper;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

public class ResourceLoader {
	public interface LoaderProgressCallBack {
		public void onProgress(Integer progress, String msg);
	}
	public class bgThemeSearch extends AsyncTask<String, Integer, Boolean>
	{
		private LoaderProgressCallBack callBack;
		private String msg;
		// 100 means completion
		private volatile int progress = 0;
		public List<String> themes;

		public void setLoaderCallBack(LoaderProgressCallBack callBack) {
			this.callBack = callBack;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			progress=0;
			msg="Searching for themes";
			publishProgress(progress);
			themes = listThemes();
			return true;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			publishProgress(100);
			
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		@Override
			protected void onProgressUpdate(Integer... progress) {
				if (callBack != null)
					callBack.onProgress(progress[0], msg);
			}
		
		
		
	}
	public class bgThemeLoader extends AsyncTask<String, Integer, Boolean> {
		private LoaderProgressCallBack callBack;
		private String msg;
		private String path;
		// 100 means completion
		private volatile int progress = 0;
		public BoardInfo boardInfo;

		public void setLoaderCallBack(LoaderProgressCallBack callBack) {
			this.callBack = callBack;
		}

		protected Boolean doInBackground(String... urls) {
			if (urls.length > 0) {
				path = urls[0];
				msg = "Loading theme";
				progress = 0;
				publishProgress(progress);
				boardInfo = loadTheme(path);
				msg = "Theme loaded. Cashing images";
				progress = 10;
				publishProgress(progress);
				Drawable[] dices, stars, players, playersFlags, sets, cells, starsh, starsv, mortgage;
				Drawable plus, minus, mortgage_get, mortgage_pay, board;

				// dices = new Drawable[ResourceBundle.DICES.length];
				// stars = new Drawable[STRAS.length];
				starsh = new Drawable[ResourceBundle.STRASH.length];
				starsv = new Drawable[ResourceBundle.STRASV.length];
				sets = new Drawable[ResourceBundle.SETS.length];
				players = new Drawable[ResourceBundle.PLAYERS.length];
				playersFlags = new Drawable[ResourceBundle.PLAYERS.length];
				cells = new Drawable[ResourceBundle.CELLS.length];
				// mortgage = new Drawable[ResourceBundle.MORTGAGE.length];
				// load(dices, DICES);
				load(starsh, boardInfo.getStarsH_images());
				load(starsv, boardInfo.getStarsV_images());
				load(sets, boardInfo.getSet_images());
				load(players, boardInfo.getPlayers_images());
				load(playersFlags, boardInfo.getFlags_images());
				load(cells, boardInfo.getCell_images());
				ResourceBundle b;
				try{
				b = ResourceBundle.getSharedInstance();
				}
				catch (Exception e) {
					b=ResourceBundle.setSharedInstance(context);
				}
				b.sets = sets;
				b.starsh = starsh;
				b.starsv = starsv;
				b.players = players;
				b.playersFlags = playersFlags;
				b.cells = cells;
				// load(mortgage, MORTGAGE);

				// plus = context.getResources().getDrawable(PLUS);
				// minus = context.getResources().getDrawable(MINUS);
				// mortgage_get =
				// context.getResources().getDrawable(MORTGAGE_GET);
				// mortgage_pay =
				// context.getResources().getDrawable(MORTGAGE_PAY);
				// board = context.getResources().getDrawable(BOARD);
				return true;
			}
			return false;
		}

		private void load(Drawable[] ds, String[] names) {
			for (int i = 0; i < names.length; i++) {
				msg = names[i];
				if(names[i]!=null)
				{
				ds[i] = loadDrawable(path + names[i]);
				}
				else
				{
					ds[i]=null;
				}
				publishProgress(++progress);
			}
		}

		protected void onProgressUpdate(Integer... progress) {
			if (callBack != null)
				callBack.onProgress(progress[0], msg);
		}
	
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			if (result)
				callBack.onProgress(100, "All done");
			else
				callBack.onProgress(-1, "Failed");
	
			super.onPostExecute(result);
		}

		}

	private static final String TAG = null;
	private Context context;

	public ResourceLoader(Context context) {
		super();
		this.context = context;
	}

	public List<String> listThemes() {
		return searchForThemes();
	}

	private InputStream openResouceStream(String path) throws IOException {
		if (path.startsWith(":sd:"))
			return new FileInputStream(path.substring(4));
		if (path.startsWith(":assets:"))
			return context.getAssets().open(path.substring(8));
		return null;
	}

	public BoardInfo loadTheme(String namePath) {
		Properties game = new Properties();
		BoardInfo boardInfo = new BoardInfo();
		LinkedList<Cell> cells = new LinkedList<Cell>();
		try {
			game.load(openResouceStream(namePath + "game"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Board board = new Board();
		CellInfo cellInfo = new CellInfo();
		int[] mapv = new int[40];
		int[] maph = new int[40];
		LinkedList<Card> cards = new LinkedList<Card>();
		LinkedList<Card> chances = new LinkedList<Card>();

		board.TAX_PERCENTAGE = Integer.parseInt((String) game.get("tax"));
		board.GO_AMOUNT = Integer.parseInt((String) game.get("go"));
		board.START_AMOUNT = Integer.parseInt((String) game.get("money"));
		String[] tmps = new String[40];
		for (int i = 0; i < 40; i++)
			tmps[i] = game.getProperty("c" + i);
		boardInfo.setCell_images(tmps);
		tmps = new String[11];
		for (int i = 0; i < 10; i++)
			tmps[i] = game.getProperty("set" + i);
		boardInfo.setSet_images(tmps);
		tmps = new String[6];
		for (int i = 0; i < 6; i++)
			tmps[i] = game.getProperty("stv" + i);
		boardInfo.setStarsV_images(tmps);
		tmps = new String[6];
		for (int i = 0; i < 6; i++)
			tmps[i] = game.getProperty("sth" + i);
		boardInfo.setStarsH_images(tmps);
		tmps = new String[5];
		for (int i = 0; i < 5; i++)
			tmps[i] = game.getProperty("player" + i);
		boardInfo.setPlayers_images(tmps);
		tmps = new String[5];
		for (int i = 0; i < 5; i++)
			tmps[i] = game.getProperty("flag" + i);
		boardInfo.setFlags_images(tmps);

		// id, name,type,corpid, price, buildhouse, buildhotel,rent,
		// rent,1,2,3,4,5
		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(
					openResouceStream(namePath + "board")));
			int i = 0;
			do {
				String s = r.readLine();
				if (s == null)
					break;
				if (s.startsWith("//"))
					continue;

				String[] ss = s.split(",");
				final int id = Integer.parseInt(ss[0]);
				final String name = ss[1];
				final String description = ss[2];
				final int type = Integer.parseInt(ss[3]);
				final int corpID = Integer.parseInt(ss[4]);
				final int price = Integer.parseInt(ss[5]);
				final int buildHouse = Integer.parseInt(ss[6]);
				final int buildHotel = Integer.parseInt(ss[7]);
				final int rent = Integer.parseInt(ss[8]);
				final int rent1 = Integer.parseInt(ss[9]);
				final int rent2 = Integer.parseInt(ss[10]);
				final int rent3 = Integer.parseInt(ss[11]);
				final int rent4 = Integer.parseInt(ss[12]);
				final int rent5 = Integer.parseInt(ss[13]);
				final int indV = Integer.parseInt(ss[14]);
				final int indH = Integer.parseInt(ss[15]);
				cells.add(CellHelper.createCell(id, name, description, type,
						corpID, price, buildHouse, buildHotel, rent, rent1,
						rent2, rent3, rent4, rent5));
				board.setCells(cells);
				maph[i] = indH;
				mapv[i] = indV;
				i++;
			} while (true);
			r.close();
			cellInfo.setMapH(maph);
			cellInfo.setMapV(mapv);

			InputStream fio = openResouceStream(namePath + "msg");
			Scanner scanner = new Scanner(fio, "UTF-8");
			while (scanner.hasNextLine()) {
				String[] vals = scanner.nextLine().split("\\t");
				int type = Integer.parseInt(vals[0]);
				int pos = Integer.parseInt(vals[1]);
				int price = Integer.parseInt(vals[2]);
				String message = vals[3];
				Card card = new Card(message, pos, price, type,
						type == Card.PRIZE);

				if (type == Card.PRIZE)
					cards.add(card);
				else
					chances.add(card);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		boardInfo.setBoard(board);
		boardInfo.setCellInfo(cellInfo);
		board.setCards(cards);
		board.setChances(chances);
		
		return boardInfo;
	}

	void searchForThemesInAssets(AssetManager mgr, String path,
			List<String> themes) {
		try {
			String list[] = mgr.list(path);

			if (list != null)
				for (int i = 0; i < list.length; ++i) {
					if (list[i].equalsIgnoreCase("board")) {
						themes.add(":assets:" + path + "/");
					}
					if (list[i].equalsIgnoreCase("images")) {
						continue;
					}

					searchForThemesInAssets(mgr, path + "/" + list[i], themes);

				}
		} catch (IOException e) {
			Log.v(TAG, "List error: can't list" + path);
		}

	}

	void searchForThemesOnSD(String path, List<String> themes) {
		File file = new File(path);
		String list[] = file.list();
		if (list != null)
			for (int i = 0; i < list.length; ++i) {
				if (list[i].equalsIgnoreCase("board")) {
					themes.add(":sd:" + path + "/");
				}
				if (list[i].equalsIgnoreCase("images")) {
					continue;
				}

				searchForThemesOnSD(path + "/" + list[i], themes);
			}
	}

	public List<String> searchForThemes() {
		List<String> themes = new ArrayList<String>();
		searchForThemesInAssets(context.getAssets(), "game", themes);
		//@TODO check for sdcard
		
		// search sd
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			File f = context.getExternalFilesDir(null);
			String path = f.getAbsolutePath() + "/game";
			searchForThemesOnSD(path, themes);
		}
		return themes;
	}

	// ///////////////////////////////////////
	private Drawable loadDrawable(String path) {
		if (path != null) {
			InputStream stream;
			try {
				stream = openResouceStream(path);
				Bitmap bitmap = BitmapFactory.decodeStream(stream, null, null);
				stream.close();
				return new BitmapDrawable(context.getResources(), bitmap);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

}
