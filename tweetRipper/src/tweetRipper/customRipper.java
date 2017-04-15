package tweetRipper;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

public class customRipper {

	public static String getLatestDHPCtweet() throws MalformedURLException, IOException 
	{
		/*
		 * Taking care of all the OAuth queries with scribeJava
		 * tutorial found here: https://github.com/scribejava/scribejava/wiki/getting-started
		 */
		final OAuth10aService service = new ServiceBuilder()
                .apiKey("xT6GflDcSj7bDkW5iHHH2US3Z")
                .apiSecret("RwelUazbRXtbfSe6AmJdxIQYDbPiQjOqtkQYx7Pc3ggjqLr0Hi")
                .build(TwitterApi.instance());
		
		final OAuth1AccessToken accessToken = new OAuth1AccessToken("853228250872639488-EHPVy5120AUBVGVQGbLeriWwG3d2XHJ",
				"cBGDMgi5vQfAQ8D17S277fjSdrf7RDz3gXTbOQcln5uk3");
		
		final OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/account/verify_credentials.json", service);
		service.signRequest(accessToken, request); // the access token from step 4
		final Response response = request.send();
		System.out.println(response.getBody());
		

		
		
		
		//Building the HTTP GET fire, should receive AT LEAST the first non-retweeted non-reply tweet from DHPC_TECH
		String url = "https://api.twitter.com/1.1/statuses/user_timeline.json"; 
		String charset = "UTF-8";
		String screen_name = "DHPC_TECH";
		String count = "10";//gets the last ten tweets so that at least one of them are going to be the newest non- retween non reply 
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
		
		URLConnection connection = new URL(url + "?" + query).openConnection();
		connection.setRequestProperty("Accept-Charset", charset);
		InputStream response2 = connection.getInputStream();
		
		try (Scanner scanner = new Scanner(response2)) {
		    String responseBody = scanner.useDelimiter("\\A").next();
		    System.out.println(responseBody);
		}
		
		
		
		
		
		
		
		
		return "Hey now....";
	}
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		
		System.out.println(getLatestDHPCtweet());

	}
	
	

}
