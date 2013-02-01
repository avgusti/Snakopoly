package my.monopoly.beans;

public class CellInfo {
	private int[] mapH,mapV;

	public CellInfo(){
		mapV=new int[40];
		mapH=new int[40];

	}
	
	public void add(int id, int v, int h)
	{
		mapH[id]=h;
		mapV[id]=v;
	}
	/**
	 * @return the mapH
	 */
	public int[] getMapH() {
		return mapH;
	}

	/**
	 * @param mapH the mapH to set
	 */
	public void setMapH(int[] mapH) {
		this.mapH = mapH;
	}

	/**
	 * @return the mapV
	 */
	public int[] getMapV() {
		return mapV;
	}

	/**
	 * @param mapV the mapV to set
	 */
	public void setMapV(int[] mapV) {
		this.mapV = mapV;
	}

	public static int[] getDefaultH() {
		int[] pos = new int[40];
		pos[0] = 7;
		pos[1] = 15;
		pos[2] = 23;
		pos[3] = 31;
		pos[4] = 39;
		pos[5] = 38;
		pos[6] = 37;
		pos[7] = 36;
		pos[8] = 35;
		pos[9] = 34;
		pos[10] = 33;
		pos[11] = 32;
		pos[12] = 24;
		pos[13] = 16;
		pos[14] = 8;
		pos[15] = 0;
		pos[16] = 1;
		pos[17] = 2;
		pos[18] = 3;
		pos[19] = 4;
		pos[20] = 5;
		pos[21] = 6;
		pos[22] = 14;
		pos[23] = 22;
		pos[24] = 30;
		pos[25] = 29;
		pos[26] = 28;
		pos[27] = 27;
		pos[28] = 26;
		pos[29] = 25;
		pos[30] = 17;
		pos[31] = 9;
		pos[32] = 10;
		pos[33] = 11;
		pos[34] = 12;
		pos[35] = 13;
		pos[36] = 21;
		pos[37] = 20;
		pos[38] = 19;
		pos[39] = 18;

		return pos;
	}

	public static int[] getDefaultV() {
		int[] pos = new int[40];
		pos[0] = 0;
		pos[1] = 1;
		pos[2] = 2;
		pos[3] = 3;
		pos[4] = 4;
		pos[5] = 9;
		pos[6] = 14;
		pos[7] = 19;
		pos[8] = 24;
		pos[9] = 29;
		pos[10] = 34;
		pos[11] = 39;
		pos[12] = 38;
		pos[13] = 37;
		pos[14] = 36;
		pos[15] = 35;
		pos[16] = 30;
		pos[17] = 25;
		pos[18] = 20;
		pos[19] = 15;
		pos[20] = 10;
		pos[21] = 5;
		pos[22] = 6;
		pos[23] = 7;
		pos[24] = 8;
		pos[25] = 13;
		pos[26] = 18;
		pos[27] = 23;
		pos[28] = 28;
		pos[29] = 33;
		pos[30] = 32;
		pos[31] = 31;
		pos[32] = 26;
		pos[33] = 21;
		pos[34] = 16;
		pos[35] = 11;
		pos[36] = 12;
		pos[37] = 17;
		pos[38] = 22;
		pos[39] = 27;
		return pos;
	}

}
