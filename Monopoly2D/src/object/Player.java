package object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import util.SortedArrayList;

public class Player implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	public int money;
	public List<Card> prisoncard= new ArrayList<Card>();;
	public int prison;
	public int position;
	public SortedArrayList<TradeableCell> myprops =new SortedArrayList<TradeableCell>();
	public boolean isHuman;
	public boolean lose=false;
	public void refreshCorps()
	{
		for(TradeableCell cell:myprops)
		{
			if(checkCorp(cell)) cell.corp=true;
			else cell.corp=false;
		}
	}
	public boolean checkCorp(TradeableCell cell) {
		int i=0;
		for(TradeableCell c:myprops)
		{
			if (c.corpid==cell.corpid) i++;
		}
		if(cell.getType()==Cell.TOWN && i==3) 
			return true;
		if(cell.getType()==Cell.RAIL && i>1) 
			return true;
		if(cell.getType()==Cell.WATELL && i==2) 
			return true;
		return false;
	}
	public boolean checkInfr(TradeableCell cell) {
		int i=0;
		for(TradeableCell c:myprops)
		{
			if (c.corpid==cell.corpid && Cell.TOWN==cell.getType() && ((TownCell)cell).getInfrasturcture()>0) i++;
		}
		if(i>0) return true;
		return false;
		
	}
	public int countCorp(TradeableCell cell) {
		int i=0;
		for(TradeableCell c:myprops)
		{
			if (c.corpid==cell.corpid) i++;
		}
		if(cell.getType()==Cell.TOWN) 
			return i-3;
		if(cell.getType()==Cell.RAIL) 
			return i-2; // starts from 2
		if(cell.getType()==Cell.WATELL) 
			return i-2;
		return -100; //unknown type
	}
	

}
