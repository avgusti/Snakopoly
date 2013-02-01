package my.monopoly;

import java.util.List;
import java.lang.CharSequence;
import my.monopoly.beans.BoardInfo;
import my.monopoly.beans.GameOptions;
import resource.ResourceLoader;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.ArrayAdapter;

public class OptionsActivity extends Activity {

	ResourceLoader.bgThemeSearch bgs;
	ResourceLoader.bgThemeLoader bg;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */

	@Override
	protected void onStart() {
		super.onStart();
		progressD = new Dialog(this, R.style.FullHeightDialog);
		progressD.setContentView(R.layout.progressdialog);
		progressBar = (ProgressBar) progressD.findViewById(R.id.progressBar);
		progressMessage = (TextView) progressD
				.findViewById(R.id.progressMessage);

		ResourceLoader loader = new ResourceLoader(this);
		showDialog(1);
		bgs = loader.new bgThemeSearch();
		bgs.setLoaderCallBack(new ResourceLoader.LoaderProgressCallBack() {

			@Override
			public void onProgress(Integer progress, String msg) {
				progressMessage.setText(msg);
				progressBar.setProgress(progress);
				if (progress == 100) {
					themesAdapter = new ArrayAdapter<String>(
							OptionsActivity.this,
							android.R.layout.simple_spinner_item, bgs.themes);

					themesAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					themes.setAdapter(themesAdapter);
					populateControls();
					bg.execute(bgs.themes.get(0));
				}
			}
		});
		// BoardInfo info= loader.loadTheme(list.get(0));
		bg = loader.new bgThemeLoader();
		bg.setLoaderCallBack(new ResourceLoader.LoaderProgressCallBack() {

			@Override
			public void onProgress(Integer progress, String msg) {
				progressMessage.setText(msg);
				progressBar.setProgress(progress);
				if (progress == 100) {
					progressD.dismiss();
				}
			}
		});
		bgs.execute();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		app = (MonoplyApplication) this.getApplication();
		app.saveGameOptions();
		super.onDestroy();
	}

	final static String TAG = OptionsActivity.class.getName();
	private MonoplyApplication app;
	private GameOptions options;
	private CheckBox showAnimation, showTrade, showComputer, showHuman;
	private EditText maxUndo, startMoney;
	private Spinner players, themes;
	private Dialog progressD;
	private TextView progressMessage;
	private ProgressBar progressBar;
	private ArrayAdapter<String> themesAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// reserved for debuging purpose
		// displayFiles(getAssets(), "",0);
		// go full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		app = (MonoplyApplication) this.getApplication();
		app.loadGameOptions();
		options = app.getGameOptions();
		// set the layout for this activity
		setContentView(R.layout.options);
		View returntogame = findViewById(R.id.returntogame);
		returntogame.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				app.setGameOptions(options);
				OptionsActivity.this.finish();
			}
		});
		View reset = findViewById(R.id.reset);
		reset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				options = new GameOptions();
				app.setGameOptions(options);
				populateControls();
			}
		});
		showAnimation = (CheckBox) findViewById(R.id.animate);
		showTrade = (CheckBox) findViewById(R.id.trade);
		showComputer = (CheckBox) findViewById(R.id.computer);
		showHuman = (CheckBox) findViewById(R.id.human);
		maxUndo = (EditText) findViewById(R.id.undocount);
		startMoney = (EditText) findViewById(R.id.initialMoneyNum);
		players = (Spinner) findViewById(R.id.playersNum);
		themes = (Spinner) findViewById(R.id.themespiner);
		players.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				updateOptions();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// do nothing
			}
		});
		themes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				updateOptions();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// do nothing
			}
		});

		defineCheckCallback(showAnimation);
		defineCheckCallback(showTrade);
		defineCheckCallback(showComputer);
		defineCheckCallback(showHuman);
		defineEditCallBack(maxUndo);
		defineEditCallBack(startMoney);

		populateControls();

	}

	boolean updateFlag = false;

	private void populateControls() {
		updateFlag = true;
		showAnimation.setChecked(options.isAnimate());
		showTrade.setChecked(options.isShowTrade());
		showComputer.setChecked(options.isShowComputer());
		showHuman.setChecked(options.isShowHuman());
		maxUndo.setText(options.getUndoCount() + "");
		startMoney.setText(options.getStartMoney() + "");
		players.setSelection(options.getPlayersNum() - 2);
		if (bgs != null && bgs.themes != null) {
			int i = 0;
			for (String s : bgs.themes) {

				if (s.equals(options.getTheme()))
					break;
				i++;
			}
			if (i >= bgs.themes.size())
				i = 0;
			themes.setSelection(i, false);
		}
		updateFlag = false;
	}

	private void updateOptions() {
		if (updateFlag)
			return;
		updateFlag = true;
		options.setAnimate(showAnimation.isChecked());
		options.setShowTrade(showTrade.isChecked());
		options.setShowComputer(showComputer.isChecked());
		options.setShowHuman(showHuman.isChecked());

		options.setUndoCount(getEditTextNumVal(maxUndo));
		options.setStartMoney(getEditTextNumVal(startMoney));
		options.setPlayersNum(players.getSelectedItemPosition() + 2);
		if (themes.getSelectedItem() != null)
			options.setTheme((String) themes.getSelectedItem());
		updateFlag = false;

	}

	private int getEditTextNumVal(EditText et) {
		int count = 0;
		final String count_Str = et.getText().toString();
		if (count_Str.length() > 0) {
			count = Integer.parseInt(count_Str);
		}
		return count;
	}

	private void defineCheckCallback(CheckBox checkBox) {
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (!updateFlag)
					updateOptions();

			}
		});
	}

	private void defineEditCallBack(EditText t) {
		t.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				updateOptions();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == 1) {
			return progressD;
		}
		return super.onCreateDialog(id);
	}

	// void displayFiles(AssetManager mgr, String path, int level) {
	// try {
	// String list[] = mgr.list(path);
	//
	// if (list != null)
	// for (int i = 0; i < list.length; ++i) {
	// if (level >= 1) {
	// displayFiles(mgr, path + "/" + list[i], level + 1);
	// } else {
	// displayFiles(mgr, list[i], level + 1);
	// }
	// }
	// } catch (IOException e) {
	// Log.v(TAG, "List error: can't list" + path);
	// }
	//
	// }
}
