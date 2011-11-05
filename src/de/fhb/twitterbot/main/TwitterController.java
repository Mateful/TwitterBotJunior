package de.fhb.twitterbot.main;

import twitter4j.TwitterException;
import de.fhb.twitterbot.commands.Command;
import de.fhb.twitterbot.commands.ExitCommand;

public class TwitterController {
	private TwitterBot twitterBot;
	
	public TwitterController() {
		twitterBot = new TwitterBot();
	}
	
	public void receiveCommand(Command command) {
		try {
			command.execute(twitterBot);
		} catch(TwitterException e) {
			e.printStackTrace();
		}
	}
}
