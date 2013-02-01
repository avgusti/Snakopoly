package my.monopoly;

import my.monopoly.beans.GameOptions;
import android.app.Application;
import android.content.SharedPreferences;

public class MonoplyApplication extends Application {
 private GameOptions gameOptions=new GameOptions();
 

/* (non-Javadoc)
 * @see android.app.Application#onCreate()
 */
@Override
public void onCreate() {
	super.onCreate();
	loadGameOptions();
}


/* (non-Javadoc)
 * @see android.app.Application#onTerminate()
 */
@Override
public void onTerminate() {
	saveGameOptions();
	
	super.onTerminate();
}


public void loadGameOptions() {
	SharedPreferences gameSettings = getSharedPreferences("Monoply2D",
			MODE_PRIVATE);
    gameOptions=new GameOptions();
    gameOptions.setAnimate(gameSettings.getBoolean("animate", true));
    gameOptions.setShowTrade(gameSettings.getBoolean("trade", true));
    gameOptions.setShowComputer(gameSettings.getBoolean("computer", true));
    gameOptions.setShowHuman(gameSettings.getBoolean("human", true));
    gameOptions.setUndoCount(gameSettings.getInt("undo", 10));
    gameOptions.setStartMoney(gameSettings.getInt("startmoney", 2000));
    gameOptions.setPlayersNum(gameSettings.getInt("players", 4));
    gameOptions.setTheme(gameSettings.getString("theme", ":assets:game/Classic/"));
}



public void saveGameOptions() {
	SharedPreferences gameSettings = getSharedPreferences("Monoply2D",
			MODE_PRIVATE);
	SharedPreferences.Editor prefEditor = gameSettings.edit();
    prefEditor.putBoolean("animate", gameOptions.isAnimate());
    prefEditor.putBoolean("trade", gameOptions.isShowTrade());
    prefEditor.putBoolean("computer", gameOptions.isShowComputer());
    prefEditor.putBoolean("human", gameOptions.isShowHuman());
    prefEditor.putInt("undo", gameOptions.getUndoCount());
    prefEditor.putInt("startmoney", gameOptions.getStartMoney());
    prefEditor.putInt("players", gameOptions.getPlayersNum());
    prefEditor.putString("theme", gameOptions.getTheme());
    prefEditor.commit();
}


/**
 * @return the gameOptions
 */
public GameOptions getGameOptions() {
	return gameOptions;
}

/**
 * @param gameOptions the gameOptions to set
 */
public void setGameOptions(GameOptions gameOptions) {
	this.gameOptions = gameOptions;
}
 
}
