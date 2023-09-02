import java.io.File;
import java.util.List;
import java.util.Scanner;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
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
					Command.playerStatsPrint(event, playerTag);
					return;
				} //if
				List<Player> playerDataList = Command.readPlayerFileContents();
				//get the username of the player being searched
				String userDataName = commandType.equals("self") ? event.getMember().getEffectiveName() : event.getOption("username").getAsString();
				Player temp = new Player(userDataName, "unused field");
				if (playerDataList.contains(temp)) {
					playerTag = playerDataList.get(playerDataList.indexOf(temp)).getTag();
				} else {
					Command.errorEmbed("An account under " + userDataName + " wasn't found! Do `/link (playertag)` to link accounts.", event);
					return;
				} //if
				Command.playerStatsPrint(event, playerTag);
				break;
			case "clan":
				switch (event.getSubcommandName().trim()) {
					case "tag":
						Command.clanStatsPrint(event);
					break;
					case "list":
						Command.clanMemberListPrint(event);
					break;
						
				} //switch
				break;
			case "linkself":
				Command.linkPlayerProfile(event, event.getMember().getEffectiveName());
				break;
			case "unlinkself":
				Command.unlinkPlayerProfile(event, event.getMember().getEffectiveName());
				break;
			case "linkplayer":
				Command.linkPlayerProfile(event, event.getOption("username").getAsString());
				break;
			case "unlinkplayer":
				Command.unlinkPlayerProfile(event, event.getOption("username").getAsString());
				break;	
			} //switch
		} catch (Exception e) {
			Command.errorEmbed("Looks like there was an error with your command.", event);
			e.printStackTrace();
		}
	} //onSlashCommandInteraction
	
	
} //Main
