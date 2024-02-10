import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import com.google.gson.Gson;

import clan.Clan;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import player.Player;

public class Command {
    

    /**
	 * Unlink the player's Discord profile from their clash of clans user tag. 
	 * @param event
	 */
	static void unlinkPlayerProfile(SlashCommandInteractionEvent event, String givenUserName) {
		EmbedBuilder eb = new EmbedBuilder();
		try {
		     List<Player> playerDataList = readPlayerFileContents();
		     //create temp PlayerData to compare in list - can discriminate more by changing PlayerData toString()
		     Player temp = new Player(givenUserName, "unused field");
		     if (playerDataList.contains(temp)) {
		    	 //remove user
		    	 playerDataList.remove(temp);
		    	 String playerDataString = new Gson().toJson(playerDataList);
		    	 FileWriter fw = new FileWriter("playerInfo.txt");
		    	 fw.write(playerDataString);
		    	 fw.close();
		    	 //send unlink message
		    	 eb.setTitle("Successfully Unlinked Accounts");
			    	eb.setDescription("The Discord and Clash accounts for " + givenUserName + " have been unlinked!");
			    	eb.setColor(Color.GREEN);
			 		event.replyEmbeds(eb.build()).queue(); 
		     } else {   	 
		    	errorEmbed("Looks like " + givenUserName + " wasn't in the system to begin with!", event);
		     } //else
		     
		} catch (Exception e) {
			errorEmbed("Uh oh! Looks like something went wrong.", event);
	 		e.printStackTrace();
		} //try
		     
		/* these here in case need debugging
		 } catch (IOException e) {
			 System.out.println("Error - file not found");
			 e.printStackTrace();
		 } catch (NullPointerException e) {
			 System.out.println("Error - null pointer");
			 e.printStackTrace();
		 } catch (UnsupportedOperationException e) {
			 System.out.println("Error - unsupported operation");
			 e.printStackTrace();
		 } //try
		*/
		
	} //unlinkPlayerProfile
	

	/**
	 * Link the player Discord profile to their Clash account via their player tag. 
	 * Uses the users discord username, stored in playerInfo.txt file.
	 * @param event
	 */
	static void linkPlayerProfile(SlashCommandInteractionEvent event, String givenUserName) {
		EmbedBuilder eb = new EmbedBuilder();
		try {
		     List<Player> playerDataList = readPlayerFileContents();
		     Player temp = new Player(givenUserName, event.getOption("playertag").getAsString());
		     if (playerDataList.contains(temp)) {
		    	 //User already in system
		    	 eb.setTitle("User already in system");
		    	 eb.setDescription("Looks like someone's already added this base to the account. If you'd like to get it removed, do `/unlink`.");
		    	 eb.setColor(Color.RED);
		    	 event.replyEmbeds(eb.build()).queue(); 
		     } else {
		    	 //else add user to list, then save in file
		    	 playerDataList.add(fillInPlayerProfile(temp));
		    	 String playerDataString = new Gson().toJson(playerDataList);
		    	 FileWriter fw = new FileWriter("playerInfo.txt");
		    	 fw.write(playerDataString);
		    	 fw.close();
		    	 //user successfully added
		    	 eb.setTitle("Success!");
		    	 eb.setDescription("You've successfully added `" + givenUserName + "` to the system.\n"
		    			+ "You can now access the profile by doing `/player username " + givenUserName + "`");
		    	 eb.setColor(Color.GREEN);
		    	 event.replyEmbeds(eb.build()).queue(); 
		     } //else
		     
		} catch (Exception e) {
			errorEmbed("Uh oh! Looks like something went wrong.", event);
			e.printStackTrace();
		} //try
		     
		/* these here in case needed for debugging 
		 } catch (IOException e) {
			 System.out.println("Error - file not found");
			 e.printStackTrace();
		 } catch (NullPointerException e) {
			 System.out.println("Error - null pointer");
			 e.printStackTrace();
		 } catch (UnsupportedOperationException e) {
			 System.out.println("Error - unsupported operation");
			 e.printStackTrace();
		 } //try
		*/
		
        
	} //linkPlayerProfile


    static Player fillInPlayerProfile(Player player) throws FileNotFoundException {
        String playerData = ClashAPI.getPlayerData(player.getTag()).toString();
		Player fullPlayer = new Gson().fromJson(playerData, Player.class);
        fullPlayer.setDiscordName(player.getDiscordName());
        return fullPlayer;
    } //fillInPlayerProfile

    static List<Player> readPlayerFileContents() throws IOException {
			//open file
			 File playerInfo = new File("playerInfo.txt");
			 if (!playerInfo.exists()) {
				 playerInfo.createNewFile();
			 } //if
		     Scanner sc = new Scanner(playerInfo);
		     String fileData = "";
		     while (sc.hasNextLine()) {
		    	 fileData += sc.nextLine();
		     } //while
		     sc.close();
		     //System.out.println(new File(".").getAbsolutePath());
		     Player[] playerData = new Gson().fromJson(fileData, Player[].class);
		     List<Player> playerDataList;
		     if (playerData == null) {
		    	 playerDataList = new ArrayList<Player>();
		     } else {
		    	 List<Player> list = Arrays.asList(playerData);
		    	 playerDataList = new ArrayList<>(list);
		     } //if
		     return playerDataList;
	} //readPlayerFileContents

    static Clan getClan(String clanTag) throws FileNotFoundException {
		String clanData = ClashAPI.getClanData(clanTag).toString();
		Clan clan = new Gson().fromJson(clanData, Clan.class);
		return clan;
	} //getClan

    static Clan getClanWars(String clanTag) throws FileNotFoundException {
		String clanData = ClashAPI.getClanWarsData(clanTag).toString();
		Clan clan = new Gson().fromJson(clanData, Clan.class);
        return clan;
	} //getClan
    
    /*
	 * 
	 */
	static void clanMemberListPrint(SlashCommandInteractionEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		try {
			String clanTag = event.getOption("tag").getAsString();
			Clan clan = getClan(clanTag);
			eb.setTitle("**" + clan.getName() + "**", "https://link.clashofclans.com/en?action=openclanprofile&tag=" + clan.getTag().substring(1));
			clan.getMemberList();
			//make embed builder
		} catch (Exception e) {
			e.printStackTrace();
		} //try
	} //clanMemberListPrint
	
	static void clanStatsPrint(SlashCommandInteractionEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		try {
			String clanTag = event.getOption("tag").getAsString();
			Clan clan = getClan(clanTag);
			
			//make embed builder
			eb.setTitle("**" + clan.getName() + "**", "https://link.clashofclans.com/en?action=openclanprofile&tag=" + clan.getTag().substring(1));
			eb.setDescription(clan.getDescription());
			eb.addField("Leader", "> " + clan.getLeader().getName() + " " + clan.getLeader().getTag(), false);
			eb.addField("Coleaders", clan.getColeaderListString(), true);
			eb.addField("Clan Stats", 
					"> War League: " + clan.getWarLeague().getName() + "\n" +
					"> Capital League: " + clan.getCapitalLeague().getName() + "\n" +
					"> War Wins/Ties/Losses: " + clan.getWarWins() + "/" + clan.getWarTies() + "/" + clan.getWarLosses() + "\n"
					, true);
            //TODO fix. make it so that wars (whatever they're called through GSON) get added to a clan. could just add it to existing "clan" variable
            System.out.println(getClanWars(clan.getTag()).getClanWars().get(0));

			eb.addField("War History", "text3", false);
            getClanWars(clan.getTag());
			eb.setAuthor(clan.getTag());
			eb.setColor(Color.CYAN);
			eb.setThumbnail(clan.getBadgeUrls().getSmall());
		
			MessageEmbed me = eb.build();
			event.replyEmbeds(me).queue(); 
		} catch (Exception e) {
			//general error message
			errorEmbed("Looks like there was an error. Make sure you're submitting the clan tag properly (example: #2Q9G8QJ2P)", event);
			e.printStackTrace();
		} //try		
		
	} //clanStatsPrint
	
	/**
	 * 
	 * @param event
	 * TODO clan capital gold contribution*, war attack tracking, activity tracking?
	 */
	static void playerStatsPrint(SlashCommandInteractionEvent event, String playerTag) {
		
		EmbedBuilder eb = new EmbedBuilder();
		NumberFormat numberFormat = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
		numberFormat.setMinimumFractionDigits(1);
		
		try {
			//get player data
			String playerData = ClashAPI.getPlayerData(playerTag).toString();
			Player player = new Gson().fromJson(playerData, Player.class);
			//get player's clan data (for war log)
			//String playerClanData = ClashAPI.getClanWarsData(player.getClan().getTag()).toString();
			//Clan playerClan = new Gson().fromJson(playerClanData, Clan.class);
			
			int donations = player.searchAchievement("Friend in Need").getValue();
			
            /* player donation/recieved ratio
            int donoRatio;
			if (player.getDonationsRecieved() != 0) {
				donoRatio = player.getDonations()/player.getDonationsRecieved();
			} else {
				donoRatio = 0;
			} //if
            */

			eb.setTitle("**" + player.getName() + "**", "https://link.clashofclans.com/en?action=OpenPlayerProfile&tag=%23" + player.getTag().substring(1));
			eb.setDescription(player.getRole() + " in " + player.getClan().getName());
			eb.addField("Player Stats", 
					"> Trophies: " + player.getTrophies() + "\n" +
					"> " + player.getLeague().getName() + "\n" +
					"> Recent Donations: " + player.getDonations() + "\n" +
					"> Total Donations: " + numberFormat.format(donations) + "\n" +
					"> Clan Capital: " + numberFormat.format(player.getClanCapitalContributions()) + "\n" +
					"> **Usefulness Score: " + player.getUsefulness() + "**"
					, true);
			eb.addField("Heroes", player.getPrettyHeroes() + "> **Heroes Complete: " + player.getHeroCompletePercent() + "%**", true);
			//playerClan.getClanWars().get(0).getResult()
			eb.addField("War History", "Coming soon!", false);
			eb.setAuthor(player.getTag());
			eb.setThumbnail(player.getTownHallImageLink());
			eb.setColor(Color.CYAN);
			
			MessageEmbed me = eb.build();
			event.replyEmbeds(me).queue(); 
			
		} catch (Exception e) {
			errorEmbed("Looks like there was an error. Make sure you're submitting the player tag properly (example: #P9YJ2QQ89)", event);
			e.printStackTrace();
		} //try
	} //playerStatsPrint

    static void errorEmbed(String errorMessage, SlashCommandInteractionEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Something went wrong...");
    	eb.setDescription(errorMessage + "\nIf you think this is an error, report it to `krilp`.");
    	eb.setColor(Color.RED);
 		event.replyEmbeds(eb.build()).queue(); 
	} //errorEmbed
} //Commands
