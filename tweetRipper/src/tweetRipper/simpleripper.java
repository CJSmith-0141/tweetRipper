package tweetRipper;


import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
//import twitter4j.conf.ConfigurationBuilder;


public class simpleripper {
	
	public static void main(String args[]) throws TwitterException{
			
	TwitterFactory tf = new TwitterFactory();
	
	Twitter twitter = tf.getInstance();
	
	String DHPC_id = "DHPC_Tech";
	
	ResponseList<Status> DHPC_tweets = twitter.getUserTimeline(DHPC_id);
	
	Status first = DHPC_tweets.get(0);
	
	String copy = first.getText();
	
	System.out.println(copy);
	
	Status copyStatus = twitter.updateStatus(copy);
	
	System.out.println("Here it goes again:" + copyStatus.getText() );
	
	}
} 