package my.monopoly;

import my.monopoly.beans.GameOptions;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MenuActivity extends Activity {
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == OPTIONS) {
			final GameOptions gameOptions = app.getGameOptions();
			if (opt_startMoney != gameOptions.getStartMoney()
					|| opt_players != gameOptions.getPlayersNum()) {
				Toast.makeText(this, "Start new game to apply options",
						Toast.LENGTH_LONG).show();

			}
		}
	}

	protected static final int OPTIONS = 1;
	private int opt_startMoney = 0, opt_players = 0;

	private MonoplyApplication app;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		app = (MonoplyApplication) getApplication();
		super.onCreate(savedInstanceState);
		// go full screen
				requestWindowFeature(Window.FEATURE_NO_TITLE);
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// set the layout for this activity
		setContentView(R.layout.menu);
		View resume = findViewById(R.id.resume);
		resume.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MenuActivity.this, Monopoly2DActivity.class);
				startActivity(intent);
				// finish the current activity
				// MenuActivity.this.finish();
			}
		});

		View newgame = findViewById(R.id.newgame);
		newgame.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences gameSettings = getSharedPreferences(
						"Monoply2D", MODE_PRIVATE);
				SharedPreferences.Editor prefEditor = gameSettings.edit();
				prefEditor.remove("board");
				prefEditor.commit();
				Intent intent = new Intent();
				intent.setClass(MenuActivity.this, Monopoly2DActivity.class);
				startActivity(intent);
				// finish the current activity
				// MenuActivity.this.finish();
			}
		});

		View exit = findViewById(R.id.exit);
		exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// finish the current activity
				MenuActivity.this.finish();

			}
		});

		View options = findViewById(R.id.options);
		options.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final GameOptions gameOptions = app.getGameOptions();
				opt_players = gameOptions.getPlayersNum();
				opt_startMoney = gameOptions.getStartMoney();
				Intent intent = new Intent();
				intent.setClass(MenuActivity.this, OptionsActivity.class);
				startActivityForResult(intent, OPTIONS);

			}
		});

		View help = findViewById(R.id.help);
		help.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MenuActivity.this, HelpActivity.class);
				startActivity(intent);

			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}