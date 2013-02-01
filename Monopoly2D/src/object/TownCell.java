package object;

public class TownCell extends TradeableCell {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static float inframult = 0.1f;

	final private int buildhouse;
	final private int buildhotel;

	final private int house1;
	final private int house2;
	final private int house3;
	final private int house4;
	final private int hotelrent;

	/**
	 * @return the buildhouse
	 */
	public int getBuildhouse() {
		return buildhouse;
	}

	/**
	 * @return the buildhotel
	 */
	public int getBuildhotel() {
		return buildhotel;
	}

	/**
	 * @return the house1
	 */
	public int getHouse1() {
		return house1;
	}

	/**
	 * @return the house2
	 */
	public int getHouse2() {
		return house2;
	}

	/**
	 * @return the house3
	 */
	public int getHouse3() {
		return house3;
	}

	/**
	 * @return the house4
	 */
	public int getHouse4() {
		return house4;
	}

	/**
	 * @return the hotelrent
	 */
	public int getHotelrent() {
		return hotelrent;
	}

	
	
	public TownCell(int index, String name, String description, int corpid,
			int price, int rent, int buildhouse, int buildhotel, int house1,
			int house2, int house3, int house4, int hotelrent) {
		super(index, name, description, corpid, price, rent);
		this.buildhouse = buildhouse;
		this.buildhotel = buildhotel;
		this.house1 = house1;
		this.house2 = house2;
		this.house3 = house3;
		this.house4 = house4;
		this.hotelrent = hotelrent;
	}

	public TownCell(int index, String name, String description, int corpid,
			int price, int rent) {
		super(index, name, description, corpid, price, rent);
		rent = (int) (inframult * price);
		buildhotel = buildhouse = rent * 20;
		house1 = rent * 5;
		house2 = (int) (house1 * 1.5);
		house3 = (int) (house2 * 1.5);
		house4 = (int) (house3 * 1.5);
		hotelrent = (int) (house4 * 1.5);
	}



	public int getRent() {
		if (owner == null)
			return 0;
		if(mortgage)
			return 0;
		if (infrasturcture == 0) {
			if (!corp) {
				return rent;
			} else {
				return rent * 2;
			}
		}
		if (infrasturcture == 1)
			return house1;
		if (infrasturcture == 2)
			return house2;
		if (infrasturcture == 3)
			return house3;
		if (infrasturcture == 4)
			return house4;
		if (infrasturcture == 5)
			return hotelrent;
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see object.Cell#getOwner()
	 */

	public boolean addInfrasturcture() {
		if (corp) {
			if (infrasturcture < 5) {
				if (infrasturcture < 4)
					if (owner.money >= buildhouse) {
						owner.money -= buildhouse;
						this.infrasturcture++;
						return true;
					} else {
						err = Error.NOT_ENOUGH_MONEY;
						return false;
					}

				if (infrasturcture == 4 && owner.money >= buildhotel) {
					owner.money -= buildhotel;
					this.infrasturcture++;
					return true;
				} else {
					err = Error.NOT_ENOUGH_MONEY;
					return false;
				}

			}
			err = Error.MAXIMUM_BUILDINGS;
			return false;
		}
		err = Error.NO_COROPATION;
		return false;
	}

	public int getInfrasturcturePrice() {
		if (corp) {
			if (infrasturcture < 4)
				return buildhouse;
			if (infrasturcture < 5)
				return buildhotel;
		}
		return 0;
	}

	public void selllInfrasturcture() {
		if (infrasturcture > 0) {
			if (infrasturcture < 5) {
				this.infrasturcture--;
				owner.money += buildhouse / 2;
			}
			if (infrasturcture == 5) {
				this.infrasturcture--;
				owner.money += buildhotel / 2;
			}
		}
		owner.refreshCorps();
	}

	public int getInfrasturcture() {
		return infrasturcture;
	}

	@Override
	public int getType() {
		return Cell.TOWN;
	}

	@Override
	public void process(Board board) {
		Player player = board.getCurrentPlayer();
		if(this.owner != null && this.owner.id!=player.id){
			player.money -= this.getRent();
			owner.money+=this.getRent();
			board.addMessage("Rent payed "+this.getRent());
		}
	}

	@Override
	public int getMaxRent() {
		return hotelrent;
	}

}
