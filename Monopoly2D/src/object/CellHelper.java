package object;

public class CellHelper {


	final private Cell c;
	final private TownCell twc;
	final private TradeableCell tc;
	final private RailroadCell rc;
	final private WaterElCell elc;
	final private boolean isTrade, isTown, isRail, isEl;
	
	public CellHelper(Cell c) {
		this.c = c;
		tc = c instanceof TradeableCell ? (TradeableCell) c : null;
		twc = c instanceof TownCell ? (TownCell) c : null;
		rc = c instanceof RailroadCell ? (RailroadCell) c : null;
		elc = c instanceof WaterElCell ? (WaterElCell) c : null;

		isTrade = tc != null;
		isTown = twc != null;
		isRail = rc != null;
		isEl = elc != null;

	}

	public int getCorpid() {
		return isTrade ? tc.corpid : 0;
	}

	public int getPrice() {
		return isTrade ? tc.getPrice() : 0;
	}

	public int getBuildhouse() {
		return isTown ? twc.getBuildhouse() : 0;
	}

	public int getBuildhotel() {
		return isTown ? twc.getBuildhotel() : 0;
	}

	public int getHouse1() {
		return isTown ? twc.getHouse1() : 0;
	}

	public int getHouse2() {
		return isTown ? twc.getHouse2() : 0;
	}

	public int getHouse3() {
		return isTown ? twc.getHouse3() : 0;
	}

	public int getHouse4() {
		return isTown ? twc.getHouse4() : 0;
	}

	public int getHotelrent() {
		return isTown ? twc.getHotelrent() : 0;
	}

	public int getRent() {
		return isTrade ? tc.getMinRent() : 0;
	}

	public int getType() {
		return c.getType();
	}

	public String getName() {
		return c.getName();
	}

	public static Cell createCell(int index, String name,String description, int type, int corpid,
			int price, int buildHouse, int buildHotel, int rent, int rent1,
			int rent2, int rent3, int rent4, int rent5) {
		Cell cell=null;
		switch (type) {
		case Cell.GO:
		{
			cell=new GoCell(index, name, description, corpid);
			break;
		}
		case Cell.WATELL:{
			cell=new WaterElCell(index, name, description, corpid, price, rent);
			break;
		}
		case Cell.TOWN:
		{
			cell=new TownCell(index, name, description, corpid, price, rent, buildHouse, buildHotel, rent1, rent2, rent3, rent4, rent5);
			break;
		}
			
		case Cell.RAIL:
		{
			cell=new RailroadCell(index, name, description, corpid, price, rent);
			break;
		}
		case Cell.GO_JAIL:
		{
			cell=new GoJailCell(index, name, description, corpid);
			break;
		}
		case Cell.JAIL:
		{
			cell=new JailCell(index, name, description, corpid);
			break;
		}
		case Cell.TAX:
		{
			cell=new TaxCell(index, name, description, corpid);
			break;
		}
		case Cell.CARD:
		{
			cell=new CardCell(index, name, description, corpid);
			break;
		}
		case Cell.CHANCE:
		{
			cell=new ChanceCell(index, name, description, corpid);
			break;
		}
		case Cell.FREE:
		{
			cell=new FreeCell(index, name, description, corpid);
			break;
		}
		
		default:
			break;
		}
		
		
		return cell;
		
		
	}
}
