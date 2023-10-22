package clan.war;

import java.util.ArrayList;

import clan.Clan;

public class War {
    String result;
    int teamSize;
    ArrayList<Clan> members; //0 is self, 1 is opponent
    
    public String getResult() {
        return result;
    }
    public int getTeamSize() {
        return teamSize;
    }
    public ArrayList<Clan> getMembers() {
        return members;
    }

    
} //War
