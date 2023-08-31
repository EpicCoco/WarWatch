import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ClashAPI {
	
	//private final static String apitoken3 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6IjI4YTMxOGY3LTAwMDAtYTFlYi03ZmExLTJjNzQzM2M2Y2NhNSJ9.eyJpc3MiOiJzdXBlcmNlbGwiLCJhdWQiOiJzdXBlcmNlbGw6Z2FtZWFwaSIsImp0aSI6ImNiNTQ2NGU3LTBhM2UtNDliYS1iYWExLTNmNWYxODBkYzIzZCIsImlhdCI6MTY5MDMxNTIxOCwic3ViIjoiZGV2ZWxvcGVyLzcyOGIzNzJhLTUzZDgtODIwZC0wNDBmLTA0ZDgyYzc1MzdkMSIsInNjb3BlcyI6WyJjbGFzaCJdLCJsaW1pdHMiOlt7InRpZXIiOiJkZXZlbG9wZXIvc2lsdmVyIiwidHlwZSI6InRocm90dGxpbmcifSx7ImNpZHJzIjpbIjE5OC4yMzIuMTIzLjE2NiJdLCJ0eXBlIjoiY2xpZW50In1dfQ.HpoL3qQsxzsCf0k9mbhql5bR9ACbXSuvtQKOeswMdlo3Va_OLZT4JAl0E4GpZRZw5jTaNKD-nZjlCqOI2w0TXw";
	private final static String apitoken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6IjI4YTMxOGY3LTAwMDAtYTFlYi03ZmExLTJjNzQzM2M2Y2NhNSJ9.eyJpc3MiOiJzdXBlcmNlbGwiLCJhdWQiOiJzdXBlcmNlbGw6Z2FtZWFwaSIsImp0aSI6IjQxNTljNmMxLTEyZTYtNDdhZS1hNzZiLTZiMGIyNmI2NDE5ZCIsImlhdCI6MTY5MTM1Njg2NCwic3ViIjoiZGV2ZWxvcGVyLzcyOGIzNzJhLTUzZDgtODIwZC0wNDBmLTA0ZDgyYzc1MzdkMSIsInNjb3BlcyI6WyJjbGFzaCJdLCJsaW1pdHMiOlt7InRpZXIiOiJkZXZlbG9wZXIvc2lsdmVyIiwidHlwZSI6InRocm90dGxpbmcifSx7ImNpZHJzIjpbIjM1LjE1MS4yNTAuMTM5Il0sInR5cGUiOiJjbGllbnQifV19.iOYupTmhRTIgLqS1Aa8Xq0i2E9sE0ESZFFxBPttYVnmIJj7JYdbV4tUYXyiRrKtNbBb9hi1o7e1SXOQbdTkwwg";
	// ^^ at home
	private final static String apiendpoint = "https://api.clashofclans.com/v1";
	//private final static String restOfUrl = "/clans/%232Q9G8QJ2P/members";
	private static String playertag = "#P9YJ2QQ89";
	
	public static String start() {
		try {
			
			playertag = URLEncoder.encode(playertag, "UTF-8");
			
			HttpURLConnection connection = (HttpURLConnection) new URL(apiendpoint + "/players/" + playertag).openConnection();
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("authorization", "Bearer " + apitoken);
            int statusCode = connection.getResponseCode();
            
            if (statusCode < 200 || statusCode >= 400) {
            	return "failure :(" + statusCode;
            } //if
            //InputStream input = connection.getInputStream();
            try(BufferedReader br = new BufferedReader(
            	new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            	StringBuilder response = new StringBuilder();
            	String responseLine = null;
            	while ((responseLine = br.readLine()) != null) {
            	    response.append(responseLine.trim());
            	} //while
            	System.out.println("response:" + response.toString());
            	JsonObject jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();
            	return jsonObject.get("townHallLevel").getAsString();
            } //try
            
		} catch (Exception e) {
			e.printStackTrace();
		} //catch
	
		return "exception";
		
	} //start
	
	
	public static JsonObject getPlayerData(String playerTag) throws FileNotFoundException {
		try {
			playerTag = URLEncoder.encode(playerTag, "UTF-8");
			return getData(playerTag, "players");
			
		} catch (Exception e) {
			e.printStackTrace();
		} //try
		
		return null;
	} //getPlayerData
	
	public static JsonObject getClanData(String clanTag) throws FileNotFoundException {
		try {
			clanTag = URLEncoder.encode(clanTag, "UTF-8");
			return getData(clanTag, "clans");
			
		} catch (Exception e) {
			e.printStackTrace();
		} //try
		
		return null;
	} //getClanData
	
	public static JsonObject getData(String tag, String type) throws FileNotFoundException {
		
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(apiendpoint + "/" + type + "/" + tag).openConnection();
			//System.out.println(connection.toString());
	        connection.setRequestProperty("Accept", "application/json");
	        connection.setRequestProperty("authorization", "Bearer " + apitoken);
	        //connection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
	        //connection.setRequestProperty("Accept","*/*");
	        int statusCode = connection.getResponseCode();
	        
	        if (statusCode != 200) {
	        	System.out.println("failed connection :( bcoz " + statusCode);
	        	InputStream error = connection.getErrorStream();
	        	System.out.println(error + "\n");
	        } //if
	        
	        //InputStream input = connection.getInputStream();
	        try(BufferedReader br = new BufferedReader(
	        	new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
	        	StringBuilder response = new StringBuilder();
	        	String responseLine = null;
	        	while ((responseLine = br.readLine()) != null) {
	        	    response.append(responseLine.trim());
	        	} //while
	        	//System.out.println(response.toString());
	        	JsonObject jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();
	        	return jsonObject;
	        } //try
		} catch (Exception e) {
			e.printStackTrace();
		} //try
		return new JsonObject();
    } //getData
	
} //ClashAPI