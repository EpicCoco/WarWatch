package player;

public class Hero {

	String name;
	int level;
	String village;
	public static final int[] KING_LEVELS = {5, 10, 30, 40, 50, 65, 75, 80, 90};
	public static final int[] QUEEN_LEVELS = {0, 0, 30, 40, 50, 65, 75, 80, 90};
	public static final int[] WARDEN_LEVELS = {0, 0, 0, 0, 20, 40, 50, 55, 65};
	public static final int[] RC_LEVELS = {0, 0, 0, 0, 0, 0, 25, 30, 40};
	public static final int[] HOME_LEVELS = {5, 10, 60, 80, 120, 170, 225, 245, 285};
	
	public Hero(String name, int level) {
		this.name = name;
		this.level = level;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}
	/**
	 * @return the village
	 */
	public String getVillage() {
		return village;
	}

	/**
	 * @return the kingLevels
	 */
	public static int[] getKingLevels() {
		return KING_LEVELS;
	}

	/**
	 * @return the queenLevels
	 */
	public static int[] getQueenLevels() {
		return QUEEN_LEVELS;
	}

	/**
	 * @return the wardenLevels
	 */
	public static int[] getWardenLevels() {
		return WARDEN_LEVELS;
	}

	/**
	 * @return the rcLevels
	 */
	public static int[] getRcLevels() {
		return RC_LEVELS;
	}
	
	
} //Heroes


