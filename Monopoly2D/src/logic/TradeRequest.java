package logic;

import object.TradeableCell;

public interface TradeRequest {
public void requestTrade(EventDispatcher d,TradeableCell pc, TradeableCell hc, int price);
}
