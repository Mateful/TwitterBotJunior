package de.fhb.twitterbot.main;

import java.util.List;
import java.util.Observable;

import twitter4j.DirectMessage;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterBot extends Observable implements Runnable {
	private Twitter twitter;
	private long lastMentionedId;
	private Thread thread;
	private boolean answering;

	public TwitterBot() {
		this(new TwitterFactory().getInstance());
	}

	public TwitterBot(Twitter t) {
		if (t != null)
			twitter = t;
		else
			twitter = new TwitterFactory().getInstance();
		
		lastMentionedId = -1;
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

	public void startAnswering() {
		answering = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void stopAnswering() {
		answering = false;
	}
	
	public boolean isAnswering() {
		return answering;
	}
	
	@Override
	public void run() {
		long timeLastCheck = 0;
		final long intervalInMillis = 10000;
		
		while(answering) {
			if (System.currentTimeMillis() - timeLastCheck > intervalInMillis) {
				try {
					printMentions();
				} catch (TwitterException e) {
					notifyObservers(e);
					stopAnswering();
				}
			}
			
			try {
				Thread.sleep(20);
			} catch(InterruptedException e) {
				notifyObservers(e);
			}
		}
	}
	
	private void notifyObservers(Exception e) {
		setChanged();
		notifyObservers(e);
	}
}
