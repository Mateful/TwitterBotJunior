package de.fhb.twitterbot.main;

import java.util.List;

import twitter4j.DirectMessage;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterBot {
	protected Twitter twitter;
	protected long lastMentionedId;

	public TwitterBot() {
		twitter = new TwitterFactory().getInstance();
		lastMentionedId = -1;
	}

	public TwitterBot(Twitter t) {
		if (t != null)
			twitter = t;
		
		lastMentionedId = -1;
	}

	public Twitter getTwitter() {
		return twitter;
	}

	public void setTwitter(Twitter twitter) {
		if (twitter != null)
			this.twitter = twitter;
	}

	public DirectMessage sendDirectMessage(String recipient, String text)
			throws TwitterException {
		return twitter.sendDirectMessage(recipient, text);
	}

	public Status updateStatus(String status) throws TwitterException {
		return twitter.updateStatus(status);
	}

	public void createFriendship(String name) throws TwitterException {
		twitter.createFriendship(name);
	}

	public QueryResult searchForTweets(String q) throws TwitterException {
		Query query = new Query(q);
		QueryResult result = twitter.search(query);
		return result;
	}
	
	public void printMentions() throws TwitterException {
	    List<Status> status = twitter.getMentions();
	    int countNewMentions = 0;
	    
    
	    for (Status s : status) {
	    	if (lastMentionedId == s.getId())
	    		break;
	    	++countNewMentions;
	    	System.out.println(s.getUser().getName() + ":" +
	                           s.getText());
	    }
	    
	    
	    
	    if (countNewMentions > 0) {
	    	lastMentionedId = status.get(0).getId();
	    } else
	    	System.out.println("no new mentions!");
	}
}
