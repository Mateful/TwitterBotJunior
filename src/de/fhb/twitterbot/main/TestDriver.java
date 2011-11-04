package de.fhb.twitterbot.main;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/*
 * app website: https://dev.twitter.com/apps/1364050/show
 * 
 * twitter4j.properties:
 
debug=true
oauth.consumerKey=H75TKHKJuWsI6vS46jO6w
oauth.consumerSecret=m79nd4QyLYy85wZJRE4eodY5IVog5fhoFLFF5ioqs
oauth.accessToken=404878505-CybLMDOinWsSFEkTL9wj0i7Gumv7BMwlYicOkSE
oauth.accessTokenSecret=VE9ZDMu9hwxvVAvk52hVE0uEvxNRavzK23P7D1JoOo
 */

public class TestDriver {

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
