package object;

public abstract class TradeableCell extends Cell implements Error {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public int getCorpid() {
		return corpid;
	}

	protected boolean corp;// used to mark if corporation exist
	protected int infrasturcture = 0;

	public int getInfrasturcture() {
		return infrasturcture;
	}

	protected String err;
	protected boolean mortgage;
	protected int price;
	protected int rent;
	protected Player owner;

	/**
	 * Used for trade operations only
	 * @param owner the owner to set
	 */
	public void setOwner(Player owner) {
		if(this.owner!=null)
		{
			this.owner.myprops.remove(this);
		}
		this.owner = owner;
		owner.myprops.insertSorted(this);
	}

	

	public TradeableCell(int index, String name, String description,
			int corpid, int price, int rent) {
		super(index, name, description, corpid);
		this.price = price;
		this.rent = rent;
	}

	/**
	 * @return the corp
	 */
	public boolean isCorp() {
		return corp;
	}

	/**
	 * @param corp the corp to set
	 */
	public void setCorp(boolean corp) {
		this.corp = corp;
	}

	/**
	 * @param corpid the corpid to set
	 */
	public void setCorpid(int corpid) {
		this.corpid = corpid;
	}

	/**
	 * @param rent the rent to set
	 */
	public void setRent(int rent) {
		this.rent = rent;
	}

	public abstract int getRent();

	public abstract int getMaxRent();

	public String getError() {
		return this.err;
	}

	public boolean getMortgage() {
		if (infrasturcture == 0 && mortgage == false) {
			mortgage = true;
			owner.money += price/2;
			return true;
		}
		err = Error.SELL_INFRASTRUCTURE_FIRST;
		return false;
	}

	public boolean payMortgage() {
		if (owner.money >= price && mortgage) {
			owner.money -= price;
			mortgage = false;
			return true;
		}

		err = Error.NOT_ENOUGH_MONEY;
		return false;
	}

	public boolean isMortgage() {
		return mortgage;
	}

	public Player getOwner() {
		return owner;
	}

	public boolean buy(Player o) {
		if (o.money >= price) {
			this.owner = o;
			o.money -= price;
			o.myprops.insertSorted(this);
			o.refreshCorps();
			return true;
		}
		err=Error.NOT_ENOUGH_MONEY;
		return false;
	}

	public boolean sell() {
		
		if (infrasturcture == 0) {
			corp = false;
			if (mortgage){
				owner.money += price / 2;
				mortgage=false;
				owner.refreshCorps();
			}
			else
				owner.money += price;
			owner.myprops.remove(this);
			this.owner = null;
			owner.refreshCorps();
			return true;
		}
		err=Error.CANT_SELL_CORPORATION;
		return false;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getPrice() {
		return price;
	}

	public int getMinRent() {
		// TODO Auto-generated method stub
		return rent;
	}

}