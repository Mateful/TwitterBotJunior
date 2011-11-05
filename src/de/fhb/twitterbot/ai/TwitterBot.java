package de.fhb.twitterbot.ai;

import java.util.List;
import java.util.Observable;

import twitter4j.DirectMessage;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterBot extends Observable {
	private Twitter twitter;
	private long lastMentionedId;

	private TwitterBotAnswering twitterBotAnswering;

	public TwitterBot() {
		this(new TwitterFactory().getInstance());
	}

	public TwitterBot(Twitter t) {
		if(t != null)
			twitter = t;
		else
			twitter = new TwitterFactory().getInstance();

		twitterBotAnswering = new TwitterBotAnswering(this);

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

	public List<Status> getMentions() throws TwitterException {
		return twitter.getMentions();
	}

	public void startAnswering() {
		twitterBotAnswering.start();
	}

	public void stopAnswering() {
		twitterBotAnswering.stop();
	}

	public boolean isAnswering() {
		return twitterBotAnswering.isRunning();
	}

	void notifyObservers(Exception e) {
		setChanged();
		notifyObservers(e);
	}

	public long getLastMentionedId() {
		return lastMentionedId;
	}

	public void setLastMentionedId(long lastMentionedId) {
		this.lastMentionedId = lastMentionedId;
	}
}
