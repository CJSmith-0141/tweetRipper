package tweetRipper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

public class customRipper {


	public static boolean postTweet(String tweet, String[] apiInfo) throws IOException
	{
		
		
		final OAuth10aService service = new ServiceBuilder()
                .apiKey(apiInfo[1])
                .apiSecret(apiInfo[2])
                .build(TwitterApi.instance());
		final OAuth1AccessToken accessToken = new OAuth1AccessToken(apiInfo[3],apiInfo[4]);
		
		String url = "https://api.twitter.com/1.1/statuses/update.json"; 
		String charset = "UTF-8";
		String status = tweet.replaceAll("\\\\/","/");//forward slash by itself isn't an escape character so I have to remove it. 
			
		String query = String.format("status=%s", URLEncoder.encode(status,charset));
		
		OAuthRequest request = new OAuthRequest(Verb.POST, url + "?"+ query, service);
		service.signRequest(accessToken, request);
		
		@SuppressWarnings("unused")
		Response response = request.send();
			
		
		return true;
	}
	
	
	public static String getLatestTweet(String userID, String[] apiInfo) throws MalformedURLException, IOException 
	{
		
		
		/*
		 * Taking care of all the OAuth queries with scribeJava
		 * tutorial found here: https://github.com/scribejava/scribejava/wiki/getting-started
		 */
		final OAuth10aService service = new ServiceBuilder()
                .apiKey(apiInfo[1])
                .apiSecret(apiInfo[2])
                .build(TwitterApi.instance());
		
		final OAuth1AccessToken accessToken = new OAuth1AccessToken(apiInfo[3],apiInfo[4]);
		
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/account/verify_credentials.json", service);
		service.signRequest(accessToken, request); 
		Response response = request.send();
		//System.out.println(response.getBody());

		
		
		
		//Building the HTTP GET fire, should receive AT LEAST the first non-retweeted non-reply tweet from whatever screen_name specified
		String url = "https://api.twitter.com/1.1/statuses/user_timeline.json"; 
		String charset = "UTF-8";
		String screen_name = userID;
		String count = "10";//gets the last ten tweets so that at least one of them are going to be the newest non-retweet non-reply 
		String exlude_replies = "true";
		String include_rts = "false";
		
		/*  Building the query, tutorial found at 
		 *	http://stackoverflow.com/questions/2793150/using-java-net-urlconnection-to-fire-and-handle-http-requests
		 *	heavily relied on. 
		 * 
		 */
		String query = String.format("screen_name=%s&count=%s&exclude_replies=%s&include_rts=%s",
				URLEncoder.encode(screen_name, charset),
				URLEncoder.encode(count, charset),
				URLEncoder.encode(exlude_replies, charset),
				URLEncoder.encode(include_rts, charset));
		
		request = new OAuthRequest(Verb.GET, url + "?"+ query, service);
		
		service.signRequest(accessToken, request);
		response = request.send();
		Scanner scanner = new Scanner(response.getBody());
		String output="";
		Pattern p = Pattern.compile("(?<!\\\\)\\\"");//This regular expression was the hardest part about getting this version to work...
		
		
		while(scanner.useDelimiter(p).hasNext()) {
		    String body = scanner.next();
		    	
		    	if(body.equals("text"))
		    	{
		    		scanner.next();
		    		output =scanner.next(); 
		    		break;
		    	}
		    	
		 //System.out.println(body+"\n");
		}
		
		scanner.close();
		
		
		
		
		
		
		
		
		return output;
	}
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		
		if(args.length!=1) System.err.println("WINDOWS USAGE:java -jar customRipper.jar [SCREEN NAME]\nPosts latest tweet from [SCREEN NAME] to @CJSmith0141");
		
		String[] apiInfo = new String[5];//if the config file is well formed, there should only be 5 lines of info
		
			/*
			 * retrieving the needed information for OAuth from a file to obfuscate from source code
			 * 
			 * Tutorial on new-ish java file IO library features found here:
			 * 
			 * http://stackoverflow.com/questions/4716503/reading-a-plain-text-file-in-java
			 */
			try(BufferedReader buff = new BufferedReader(new FileReader("config.txt")))
			{
				String line = buff.readLine();
				int i =0;
					
					while(line!=null)
					{
						apiInfo[i] = line.split("=")[1];//takes only the interesting bit of each line in the config file
						line=buff.readLine();
						i++;
						
					}
				
			}
		
		String tweetText = getLatestTweet(args[0], apiInfo);
		
		postTweet(tweetText, apiInfo);

	}
	
	

}
