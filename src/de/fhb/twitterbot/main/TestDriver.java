package de.fhb.twitterbot.main;

import twitter4j.TwitterException;

/*
 * app website: https://dev.twitter.com/apps/1364050/show
 * 
 * twitter4j.properties:
 * debug=true
 * oauth.consumerKey=H75TKHKJuWsI6vS46jO6w
 * oauth.consumerSecret=m79nd4QyLYy85wZJRE4eodY5IVog5fhoFLFF5ioqs
 * oauth.accessToken=404878505-CybLMDOinWsSFEkTL9wj0i7Gumv7BMwlYicOkSE
 * oauth.accessTokenSecret=VE9ZDMu9hwxvVAvk52hVE0uEvxNRavzK23P7D1JoOo
 */

public class TestDriver {

	public static void main(String[] args) {
		// new commentar
		TwitterBot bot = new TwitterBot();
		try {
			while (true) {
				Thread.sleep(10000);
				bot.printMentions();
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
