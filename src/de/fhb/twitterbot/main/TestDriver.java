package de.fhb.twitterbot.main;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/*
 * app website: https://dev.twitter.com/apps/1364050/show
 */

public class TestDriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Twitter twitter = new TwitterFactory().getInstance();
		Status status;
		
		try {
			status = twitter.updateStatus("new (automated) status message");
			System.out.println("Successfully updated the status to ["
					+ status.getText() + "].");

			twitter.createFriendship("kwoxerde");
			System.out.println("Successfully followed [kwoxerde].");

			DirectMessage message = twitter.sendDirectMessage("kwoxerde",
					"test message from mateful to kwoxer");
			System.out.println("Direct message successfully sent to "
					+ message.getRecipientScreenName());
			
			// CHANGED again
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

}
