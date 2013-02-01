package my.monopoly.beans;

public class GameOptions {
boolean animate=true, showTrade=true, showComputer=true, showHuman=true;
int playersNum=4;
int startMoney=2000;
int undoCount=10;
String theme="default";

/**
 * @return the theme
 */
public String getTheme() {
	return theme;
}

/**
 * @param theme the theme to set
 */
public void setTheme(String theme) {
	this.theme = theme;
}

/**
 * @return the startMoney
 */
public int getStartMoney() {
	return startMoney;
}

/**
 * @param startMoney the startMoney to set
 */
public void setStartMoney(int startMoney) {
	this.startMoney = startMoney;
}

/**
 * @return the playersNum
 */
public int getPlayersNum() {
	return playersNum;
}

/**
 * @return the undoCount
 */
public int getUndoCount() {
	return undoCount;
}

/**
 * @return the animate
 */
public boolean isAnimate() {
	return animate;
}

/**
 * @return the showComputer
 */
public boolean isShowComputer() {
	return showComputer;
}

/**
 * @return the showHuman
 */
public boolean isShowHuman() {
	return showHuman;
}

/**
 * @return the showTrade
 */
public boolean isShowTrade() {
	return showTrade;
}

/**
 * @param animate the animate to set
 */
public void setAnimate(boolean animate) {
	this.animate = animate;
}

/**
 * @param playersNum the playersNum to set
 */
public void setPlayersNum(int playersNum) {
	this.playersNum = playersNum;
}

/**
 * @param showComputer the showComputer to set
 */
public void setShowComputer(boolean showComputer) {
	this.showComputer = showComputer;
}

/**
 * @param showHuman the showHuman to set
 */
public void setShowHuman(boolean showHuman) {
	this.showHuman = showHuman;
}

/**
 * @param showTrade the showTrade to set
 */
public void setShowTrade(boolean showTrade) {
	this.showTrade = showTrade;
}

/**
 * @param undoCount the undoCount to set
 */
public void setUndoCount(int undoCount) {
	this.undoCount = undoCount;
}

}
