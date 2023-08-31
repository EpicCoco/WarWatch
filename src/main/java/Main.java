import java.awt.Color;
import java.io.File;
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
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import player.Player;

//player tag: #2Q9G8QJ2P

public class Main extends ListenerAdapter {
	
	public static JDA jda;
	public static ClashAPI clashapi;
	

	/* 
	 * on joining:
	 * must link accounts
	 * must change nickname to in game name
	 */
	
	/*
	 * make it so if they don't provide parmeters for clan tag, it automatically grabs it from their profile ..?
	 * need to fix where file stores clan information (gson tojson doesn't take in objects)
	 */
	
	public static void main(String[] args) {
		try {
			Scanner sc = new Scanner(new File("discordkey"));
			String token = sc.nextLine();

			JDA jda = JDABuilder.createDefault(token)
	                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS) // enables explicit access to message.getContentDisplay()
	                .setMemberCachePolicy(MemberCachePolicy.ALL).enableIntents(GatewayIntent.GUILD_MEMBERS).setChunkingFilter(ChunkingFilter.ALL)
	                .build();
	        jda.addEventListener(new Main());
	        //set activity
	        jda.getPresence().setActivity(Activity.playing("Clash of Clans"));
	        //set online status
	        jda.getPresence().setStatus(OnlineStatus.ONLINE);
	        //update commands
	        jda.updateCommands().addCommands(
	        		//player commands
	        		Commands.slash("player", "Look up info on a player")
	        			.addSubcommands(
	        					new SubcommandData("self", "Look up info on  yourself (assuming your accounts are linked!)"),
	        					new SubcommandData("tag", "Look up based on player tag")
	        						.addOption(OptionType.STRING, "playertag", "Player Tag (#P9YJ2QQ89 for example)", true),
	        					new SubcommandData("name", "Look up based on username (assuming their accounts arre linked!)")
	        						.addOption(OptionType.STRING, "username", "Discord username (krilp for example)", true)
	        			),
	        			
	        		//clan commands
	        		Commands.slash("clan", "Look up info on a clan")
	        			.addSubcommands(
	        					new SubcommandData("tag", "Look up clan based on clan tag")
	        						.addOption(OptionType.STRING, "tag", "Clan Tag (#2Q9G8QJ2P for example)", true),
	        					new SubcommandData("list", "Display a list of all clan members")
	        			),
	        		    
	        		    
	        		//linking commands    
	        		Commands.slash("linkself", "Link your own Discord and clash accounts")
	        			.addOption(OptionType.STRING, "playertag", "Player Tag (#P9YJ2QQ89 for example)", true),	
	        		//linkplayer is admin only
	        		Commands.slash("linkplayer", "Link another player's Discord and Clash accounts (admin only)")
	        			.setDefaultPermissions(DefaultMemberPermissions.DISABLED)
	        			.addOption(OptionType.STRING, "username", "Discord username (krilp for example)")
	        			.addOption(OptionType.STRING, "playertag", "Player Tag (#P9YJ2QQ89 for example)"),
	        		Commands.slash("unlinkself", "Unlink your own Discord and clash accounts"),
        			//unlink player is admin only
	        		Commands.slash("unlinkplayer", "Unlink another player's Discord and clash accounts") 
	        			.setDefaultPermissions(DefaultMemberPermissions.DISABLED)
	        			.addOption(OptionType.STRING, "username", "Discord username (krilp for example)")
	        			.addOption(OptionType.STRING, "playertag", "Player Tag (#P9YJ2QQ89 for example)")
	        			
	        		//commands done
	        		).queue();
	        
	     
		} catch(Exception e) {
			e.printStackTrace();
		} //try
		
	} //main 
	
	/**
	 * Triggers other methods based on the event
	 */
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		try {
			switch (event.getName()) {
			case "player":
				String commandType = event.getSubcommandName().trim();
				String playerTag;
				if (commandType.equals("tag")) {
					playerTag = event.getOption("playertag").getAsString();
					playerStatsPrint(event, playerTag);
					return;
				} //if
				List<Player> playerDataList = readPlayerFileContents();
				//get the username of the player being searched
				String userDataName = commandType.equals("self") ? event.getMember().getEffectiveName() : event.getOption("username").getAsString();
				Player temp = new Player(userDataName, "unused field");
				if (playerDataList.contains(temp)) {
					playerTag = playerDataList.get(playerDataList.indexOf(temp)).getTag();
				} else {
					errorEmbed("An account under " + userDataName + " wasn't found! Do `/link (playertag)` to link accounts.", event);
					return;
				} //if
				playerStatsPrint(event, playerTag);
				break;
			case "clan":
				switch (event.getSubcommandName().trim()) {
					case "tag":
					clanStatsPrint(event);
					break;
					case "list":
						
					break;
						
				} //switch
				break;
			case "linkself":
				linkPlayerProfile(event, event.getMember().getEffectiveName());
				break;
			case "unlinkself":
				unlinkPlayerProfile(event, event.getMember().getEffectiveName());
				break;
			case "linkplayer":
				linkPlayerProfile(event, event.getOption("username").getAsString());
				break;
			case "unlinkplayer":
				unlinkPlayerProfile(event, event.getOption("username").getAsString());
				break;	
			} //switch
		} catch (Exception e) {
			errorEmbed("Looks like there was an error with your command.", event);
			e.printStackTrace();
		}
	} //onSlashCommandInteraction
	
	private List<Player> readPlayerFileContents() throws IOException {
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
	
	private void errorEmbed(String errorMessage, SlashCommandInteractionEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Something went wrong...");
    	eb.setDescription(errorMessage + "\nIf you think this is an error, report it to `krilp`.");
    	eb.setColor(Color.RED);
 		event.replyEmbeds(eb.build()).queue(); 
	} //errorEmbed
	
	
	/**
	 * Unlink the player's Discord profile from their clash of clans user tag. 
	 * @param event
	 */
	private void unlinkPlayerProfile(SlashCommandInteractionEvent event, String givenUserName) {
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
	private void linkPlayerProfile(SlashCommandInteractionEvent event, String givenUserName) {
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
		    	 playerDataList.add(temp);
		    	 String playerDataString = new Gson().toJson(playerDataList);
		    	 FileWriter fw = new FileWriter("playerInfo.txt");
		    	 fw.write(playerDataString);
		    	 fw.close();
		    	 //user successfully added
		    	 eb.setTitle("Success!");
		    	 eb.setDescription("You've successfully added " + givenUserName + " to the system.\n"
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
	
	/*
	 * 
	 */
	private void clanMemberListPrint(SlashCommandInteractionEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		
	} //clanMemberListPrint
	
	private void clanStatsPrint(SlashCommandInteractionEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		try {
			String clanTag = event.getOption("clantag").getAsString();
			String clanData = ClashAPI.getClanData(clanTag).toString();
			Clan clan = new Gson().fromJson(clanData, Clan.class);
			
			//make embed builder
			eb.setTitle("**" + clan.getName() + "**", "https://link.clashofclans.com/en?action=openclanprofile&tag=" + clan.getTag().substring(1));
			eb.setDescription(clan.getDescription());
			clan.getMemberList();
			eb.addField("Leader", "> " + clan.getLeader().getName() + " " + clan.getLeader().getTag(), false);
			eb.addField("Coleaders", clan.getColeaderListString(), true);
			eb.addField("Clan Stats", 
					"> War League: " + clan.getWarLeague().getName() + "\n" +
					"> Capital League: " + clan.getCapitalLeague().getName() + "\n" +
					"> War Wins/Ties/Losses: " + clan.getWarWins() + "/" + clan.getWarTies() + "/" + clan.getWarLosses() + "\n"
					, true);
			eb.addField("War History", "text3", false);
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
	private void playerStatsPrint(SlashCommandInteractionEvent event, String playerTag) {
		
		EmbedBuilder eb = new EmbedBuilder();
		NumberFormat numberFormat = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
		numberFormat.setMinimumFractionDigits(1);
		
		try {
			
			String playerData = ClashAPI.getPlayerData(playerTag).toString();
			Player player = new Gson().fromJson(playerData, Player.class);
			
			int donations = player.searchAchievement("Friend in Need").getValue();
			int donoRatio;
			if (player.getDonationsRecieved() != 0) {
				donoRatio = player.getDonations()/player.getDonationsRecieved();
			} else {
				donoRatio = 0;
			} //if

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
			eb.addField("War History", "text3", false);
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
	
	
} //Main
