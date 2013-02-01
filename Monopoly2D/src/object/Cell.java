package object;

import java.io.Serializable;

public abstract class Cell implements Serializable, Comparable<Cell> {
	
	public final static int CARD = 7;

	public final static int CHANCE = 8;

	public final static int FREE = 1;
	public final static int GO = 0;
	public final static int GO_JAIL = 2;
	public final static int JAIL = 3;
	public final static int RAIL = 5;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static int TAX = 9;
	public final static int TOWN = 4;
	public final static int WATELL = 6;
	public int index,corpid;
	public String name,description;
	public Cell(int index,String name, String description,int corpid) {
		super();
		this.name = name;
		this.description = description;
		this.index = index;
		this.corpid=corpid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Cell another) {
		return index - another.index;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	public abstract int getType();

	public abstract void process(Board board);

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}