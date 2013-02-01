package my.monopoly.beans;

import object.Board;

public class BoardInfo {
	private String[] cell_images, set_images, starsV_images, starsH_images,
			players_images, flags_images;

	private String board_image, mortgage_image, plus, minus, get, pay;
	private Board board;
	private CellInfo cellInfo;
	
	public BoardInfo() {
		cell_images = new String[40];
		set_images = new String[40];
		starsV_images = new String[5];
		starsH_images = new String[5];
		players_images = new String[4];
		flags_images = new String[4];
	}

	/**
	 * @return the board
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * @param board the board to set
	 */
	public void setBoard(Board board) {
		this.board = board;
	}

	/**
	 * @return the cellInfo
	 */
	public CellInfo getCellInfo() {
		return cellInfo;
	}

	/**
	 * @param cellInfo the cellInfo to set
	 */
	public void setCellInfo(CellInfo cellInfo) {
		this.cellInfo = cellInfo;
	}

	/**
	 * @return the players_images
	 */
	public String[] getPlayers_images() {
		return players_images;
	}

	/**
	 * @param players_images the players_images to set
	 */
	public void setPlayers_images(String[] players_images) {
		this.players_images = players_images;
	}

	/**
	 * @return the flags_images
	 */
	public String[] getFlags_images() {
		return flags_images;
	}

	/**
	 * @param flags_images the flags_images to set
	 */
	public void setFlags_images(String[] flags_images) {
		this.flags_images = flags_images;
	}

	/**
	 * @return the cell_images
	 */
	public String[] getCell_images() {
		return cell_images;
	}

	/**
	 * @param cell_images
	 *            the cell_images to set
	 */
	public void setCell_images(String[] cell_images) {
		this.cell_images = cell_images;
	}

	/**
	 * @return the set_images
	 */
	public String[] getSet_images() {
		return set_images;
	}

	/**
	 * @param set_images
	 *            the set_images to set
	 */
	public void setSet_images(String[] set_images) {
		this.set_images = set_images;
	}

	/**
	 * @return the starsv_images
	 */
	public String[] getStarsV_images() {
		return starsV_images;
	}

	/**
	 * @param starsv_images
	 *            the starsv_images to set
	 */
	public void setStarsV_images(String[] starsv_images) {
		this.starsV_images = starsv_images;
	}

	/**
	 * @return the starsh_images
	 */
	public String[] getStarsH_images() {
		return starsH_images;
	}

	/**
	 * @param starsh_images
	 *            the starsh_images to set
	 */
	public void setStarsH_images(String[] starsh_images) {
		this.starsH_images = starsh_images;
	}

	/**
	 * @return the board_image
	 */
	public String getBoard_image() {
		return board_image;
	}

	/**
	 * @param board_image
	 *            the board_image to set
	 */
	public void setBoard_image(String board_image) {
		this.board_image = board_image;
	}

	/**
	 * @return the mortgage_image
	 */
	public String getMortgage_image() {
		return mortgage_image;
	}

	/**
	 * @param mortgage_image
	 *            the mortgage_image to set
	 */
	public void setMortgage_image(String mortgage_image) {
		this.mortgage_image = mortgage_image;
	}

	/**
	 * @return the plus
	 */
	public String getPlus() {
		return plus;
	}

	/**
	 * @param plus
	 *            the plus to set
	 */
	public void setPlus(String plus) {
		this.plus = plus;
	}

	/**
	 * @return the minus
	 */
	public String getMinus() {
		return minus;
	}

	/**
	 * @param minus
	 *            the minus to set
	 */
	public void setMinus(String minus) {
		this.minus = minus;
	}

	/**
	 * @return the get
	 */
	public String getGet() {
		return get;
	}

	/**
	 * @param get
	 *            the get to set
	 */
	public void setGet(String get) {
		this.get = get;
	}

	/**
	 * @return the pay
	 */
	public String getPay() {
		return pay;
	}

	/**
	 * @param pay
	 *            the pay to set
	 */
	public void setPay(String pay) {
		this.pay = pay;
	}

}
