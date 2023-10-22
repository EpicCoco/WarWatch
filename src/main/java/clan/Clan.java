package clan;

import java.util.ArrayList;
import java.util.Collections;

import clan.war.War;
import player.League;
import player.Player;

public class Clan {

	String tag;
	String name;
	String description;
	int level;
	ArrayList<Player> memberList;
	BadgeURLs badgeUrls;
	League warLeague;
	League capitalLeague;
	int warWins;
	int warLosses;
	int warTies;
	ArrayList<War> clanWarLog;

	/**
	 * @return the memberList
	 */
	public ArrayList<War> getClanWars() {
		return clanWarLog;
	} //getClanWars

	/**
	 * @return the memberList
	 */
	public ArrayList<Player> getMemberList() {
		return memberList;
	} //getMemberList
	
	public Player getLeader() {
		Collections.sort(memberList, (o1, o2) -> o1.getRoleNumber() - o2.getRoleNumber());
		return memberList.get(0);
	} //getLeader
	
	public ArrayList<Player> getColeaderList() {
		int numColeaders = 0;
		for (Player p : memberList) {
			if (p.getRole().equals("Coleader")) {
				numColeaders++;
			} //if
		} //for
		ArrayList<Player> toReturn = new ArrayList<Player>(memberList.subList(1, numColeaders + 1));
		return toReturn;
	} //getColeaderList
	
	public String getColeaderListString() {
		String toReturn = "";
		ArrayList<Player> coleaders = getColeaderList();
		for (Player p : coleaders) {
			toReturn += "> " + p.getName() + " " + p.getTag() + "\n";
		} //for
		return toReturn;
	} //getColeaderList
	
	public String getMemberListString() {
		String toReturn = "";
		for (Player p : memberList) {
			toReturn += p.getName() + "";
		} //for
		return toReturn;
	} //getMemberListString
	
	public int clanSize() {
		return memberList.size();
	} //clanSize
	
	/**
	 * @return the badgeUrls
	 */
	public BadgeURLs getBadgeUrls() {
		return badgeUrls;
	}
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
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @return the warWins
	 */
	public int getWarWins() {
		return warWins;
	}

	/**
	 * @return the warLosses
	 */
	public int getWarLosses() {
		return warLosses;
	}
	
	/**
	 * @return the warTies
	 */
	public int getWarTies() {
		return warTies;
	}

	/**
	 * @return the warLeague
	 */
	public League getWarLeague() {
		return warLeague;
	}

	/**
	 * @return the capitalLeague
	 */
	public League getCapitalLeague() {
		return capitalLeague;
	}
	
	
	
} //Clan
