package player;

import java.util.ArrayList;
import clan.Clan;

public class Player {

	String tag;
	String name;
	String discordName;
	int townHallLevel;
	int townHallWeaponLevel;
	int expLevel;
	int trophies;
	int bestTrophies;
	int warStars; 
	String role;
	String warPreference;
	int donations;
	int donationsRecieved;
	int clanCapitalContributions;
	ArrayList<Hero> heroes;
	ArrayList<Achievement> achievements;
	Clan clan;
	League league;
	
	
	
	public Player(String discordName, String tag) {
		this.discordName = discordName;
		this.tag = tag;
	}
	
	public void setDiscordName(String discordName) {
		this.discordName = discordName;
	} //setDiscordName
	
	public League getLeague() {
		return league;
	}
	
	public Clan getClan() {
		return clan;
	}
	
	public ArrayList<Hero> getHeroes() {
		return heroes;
	}
	
	public ArrayList<Achievement> getAchievements() {
		return achievements;
	}
	
	public Achievement searchAchievement(String achievementName) {
		for (int i = 0; i < achievements.size(); i++) {
			if (achievements.get(i).getName().compareTo(achievementName) == 0) {
				return achievements.get(i);
			} //if
		} //for
		return new Achievement("null", 0);
	} //searchHero
	
	public Hero searchHero(String heroName) {
		for (int i = 0; i < heroes.size(); i++) {
			if (heroes.get(i).getName().compareTo(heroName) == 0) {
				return heroes.get(i);
			} //if
		} //for
		return new Hero("null", 0);
	} //searchHero
	
	public String getPrettyHeroes() {
		String toReturn = "";
		for (int i = 0; i < heroes.size(); i++) {
			Hero hero = heroes.get(i);
			if (!hero.getVillage().equals("builderBase")) {
				toReturn += "> " + hero.getName() + ": " + hero.getLevel() + "\n";
			} //if
		} //for
		return toReturn;
	} //getPrettyHeroes
	
	
	
	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the townHallLevel
	 */
	public int getTownHallLevel() {
		return townHallLevel;
	}
	/**
	 * @return the expLevel
	 */
	public int getExpLevel() {
		return expLevel;
	}
	/**
	 * @return the trophies
	 */
	public int getTrophies() {
		return trophies;
	}
	/**
	 * @return the bestTrophies
	 */
	public int getBestTrophies() {
		return bestTrophies;
	}
	/**
	 * @return the warStars
	 */
	public int getWarStars() {
		return warStars;
	}
	/**
	 * @return the role
	 */
	public String getRole() {
		switch (role) {
			case "coLeader":
				return "Coleader";
			case "leader":
				return "Leader";
			case "admin":
				return "Elder";
			default:
				return "Member";
		} //switch
	}
	
	public int getRoleNumber() {
		switch (role) {
			case "coLeader":
				return 2;
			case "leader":
				return 1;
			case "admin":
				return 3;
			default:
				return 4;
		} //switch
	}
	/**
	 * @return the warPreference
	 */
	public String getWarPreference() {
		return warPreference;
	}
	/**
	 * @return the donations
	 */
	public int getDonations() {
		return donations;
	}
	/**
	 * @return the donationsRecieved
	 */
	public int getDonationsRecieved() {
		return donationsRecieved;
	}

	/**
	 * @return the townHallWeaponLevel
	 */
	public int getTownHallWeaponLevel() {
		return townHallWeaponLevel;
	}

	/**
	 * @return the clanCapitalContributions
	 */
	public int getClanCapitalContributions() {
		return clanCapitalContributions;
	}
	
	public String getDiscordName() {
		return discordName;
	}
	


	public double getHeroCompletePercent() {
		double heroCompletePercent = 
				(((double)(searchHero("Barbarian King").getLevel() + 
				searchHero("Archer Queen").getLevel() + 
				searchHero("Grand Warden").getLevel() + 
				searchHero("Royal Champion").getLevel())) / 
				((double)Hero.HOME_LEVELS[getTownHallLevel() - 7]));
		return ((double)((int)(heroCompletePercent *10000.0)))/100.0;
	} //getHeroCompletePercent
	
	public int getUsefulness() {
		int MIN_DONO = 50; //minimum number of donations needed to get extra clan points
		double MIN_CCCONTRIBUTION = 1000000.0; //minimum number of capital gold needed to get extra clan points
		int usefulnessScore = (int)
				((getTownHallLevel() * 0.1) * 
				((getDonations() < MIN_DONO ? MIN_DONO : getDonations()) / 10.0) * 
				(getHeroCompletePercent() / 100.0) * 
				((getClanCapitalContributions() > MIN_CCCONTRIBUTION ? (double)(getClanCapitalContributions()/1000000.0) : 1)));
		return usefulnessScore;
	} //getUsefulness
	
	/**
	 * Equals method to determine sort order
	 */
	@Override
	public boolean equals(Object p2) {
		if (this.toString().equals(p2.toString())) {
			return true;
		} //if
		return false;
	} //equals
	
	//can change the toString later to allow for playertag discrimination
	@Override
	public String toString() {
		return discordName;
		//return "(" + discordName + ":" + playerTag + ")";
	}
	
	public String getTownHallImageLink() {
		switch (townHallLevel) {
		
		case 1:
			return "https://static.wikia.nocookie.net/clashofclans/images/f/fd/Town_Hall1.png/revision/latest/scale-to-width-down/100?cb=20170827034930";
		case 2:
			return "https://static.wikia.nocookie.net/clashofclans/images/7/7d/Town_Hall2.png/revision/latest/scale-to-width-down/100?cb=20170827050036";
		case 3:
			return "https://static.wikia.nocookie.net/clashofclans/images/d/dd/Town_Hall3.png/revision/latest/scale-to-width-down/100?cb=20170827050050";
		case 4:
			return "https://static.wikia.nocookie.net/clashofclans/images/e/e7/Town_Hall4.png/revision/latest/scale-to-width-down/100?cb=20170827050104";
		case 5:
			return "https://static.wikia.nocookie.net/clashofclans/images/a/a3/Town_Hall5.png/revision/latest/scale-to-width-down/100?cb=20170827050118";
		case 6:
			return "https://static.wikia.nocookie.net/clashofclans/images/5/52/Town_Hall6.png/revision/latest/scale-to-width-down/100?cb=20170827050220";
		case 7:
			return "https://static.wikia.nocookie.net/clashofclans/images/7/75/Town_Hall7.png/revision/latest/scale-to-width-down/100?cb=20170827051024";
		case 8:
			return "https://static.wikia.nocookie.net/clashofclans/images/f/fa/Town_Hall8.png/revision/latest/scale-to-width-down/100?cb=20170827051039";
		case 9:
			return "https://static.wikia.nocookie.net/clashofclans/images/e/e0/Town_Hall9.png/revision/latest/scale-to-width-down/100?cb=20170827045259";
		case 10:
			return "https://static.wikia.nocookie.net/clashofclans/images/5/5c/Town_Hall10.png/revision/latest/scale-to-width-down/110?cb=20170827040043";
		case 11:
			return "https://static.wikia.nocookie.net/clashofclans/images/9/96/Town_Hall11.png/revision/latest/scale-to-width-down/105?cb=20210410001514";
		case 12:
			switch (townHallWeaponLevel) {
			case 1:
				return "https://static.wikia.nocookie.net/clashofclans/images/c/c7/Town_Hall12-1.png/revision/latest/scale-to-width-down/110?cb=20180603203226";
			case 2:
				return "https://static.wikia.nocookie.net/clashofclans/images/2/28/Town_Hall12-2.png/revision/latest/scale-to-width-down/110?cb=20180603203239";
			case 3:
				return "https://static.wikia.nocookie.net/clashofclans/images/2/28/Town_Hall12-3.png/revision/latest/scale-to-width-down/110?cb=20180603203254";
			case 4:
				return "https://static.wikia.nocookie.net/clashofclans/images/2/28/Town_Hall12-4.png/revision/latest/scale-to-width-down/110?cb=20180603203306";
			case 5:
				return "https://static.wikia.nocookie.net/clashofclans/images/7/7c/Town_Hall12-5.png/revision/latest/scale-to-width-down/110?cb=20180603203336";
			} //switch
		case 13:
			switch (townHallWeaponLevel) {
			case 1:
				return "https://static.wikia.nocookie.net/clashofclans/images/9/98/Town_Hall13-1.png/revision/latest/scale-to-width-down/120?cb=20200831024426";
			case 2:
				return "https://static.wikia.nocookie.net/clashofclans/images/a/a0/Town_Hall13-2.png/revision/latest/scale-to-width-down/120?cb=20200831024426";
			case 3:
				return "https://static.wikia.nocookie.net/clashofclans/images/1/17/Town_Hall13-3.png/revision/latest/scale-to-width-down/120?cb=20200831024427";
			case 4:
				return "https://static.wikia.nocookie.net/clashofclans/images/7/78/Town_Hall13-4.png/revision/latest/scale-to-width-down/120?cb=20200831024427";
			case 5:
				return "https://static.wikia.nocookie.net/clashofclans/images/1/10/Town_Hall13-5.png/revision/latest/scale-to-width-down/120?cb=20200831024428";
			} //switch
		case 14:
			switch (townHallWeaponLevel) {
			case 1:
				return "https://static.wikia.nocookie.net/clashofclans/images/e/e0/Town_Hall14-1.png/revision/latest/scale-to-width-down/110?cb=20210413000722";
			case 2:
				return "https://static.wikia.nocookie.net/clashofclans/images/c/c4/Town_Hall14-2.png/revision/latest/scale-to-width-down/110?cb=20210413000738";
			case 3:
				return "https://static.wikia.nocookie.net/clashofclans/images/d/d7/Town_Hall14-3.png/revision/latest/scale-to-width-down/110?cb=20210413000808";
			case 4:
				return "https://static.wikia.nocookie.net/clashofclans/images/5/5d/Town_Hall14-4.png/revision/latest/scale-to-width-down/110?cb=20210413000837";
			case 5:
				return "https://static.wikia.nocookie.net/clashofclans/images/1/1c/Town_Hall14-5.png/revision/latest/scale-to-width-down/110?cb=20210413000854";
			} //switch
		case 15:
			switch (townHallWeaponLevel) {
			case 1:
				return "https://static.wikia.nocookie.net/clashofclans/images/5/5b/Town_Hall15-1.png/revision/latest/scale-to-width-down/115?cb=20221120065403";
			case 2:
				return "https://static.wikia.nocookie.net/clashofclans/images/e/ec/Town_Hall15-2.png/revision/latest/scale-to-width-down/115?cb=20221120065412";
			case 3:
				return "https://static.wikia.nocookie.net/clashofclans/images/3/3d/Town_Hall15-3.png/revision/latest/scale-to-width-down/115?cb=20221120065433";
			case 4:
				return "https://static.wikia.nocookie.net/clashofclans/images/3/38/Town_Hall15-4.png/revision/latest/scale-to-width-down/115?cb=20221120070058";
			case 5:
				return "https://static.wikia.nocookie.net/clashofclans/images/e/e6/Town_Hall15-5.png/revision/latest/scale-to-width-down/115?cb=20221120065456";
			} //switch
		default:
			return "";
		} //switch
	} //getTownHallImageLink
	
} //Player
