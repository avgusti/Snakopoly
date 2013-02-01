package object;

public interface Error {

	public static String NOT_ENOUGH_MONEY = "Not enough money";
	public static String MAXIMUM_BUILDINGS="Maximum number of buildings reached";
	public static String SELL_INFRASTRUCTURE_FIRST = "First sell infrastructure";
	public static String NO_COROPATION = "Corporation do not exist";
	public static String CANT_SELL_CORPORATION = "Can't sell corporation";

	public abstract String getError();

}