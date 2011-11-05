package de.fhb.twitterbot.commands;

import twitter4j.TwitterException;
import de.fhb.twitterbot.ai.TwitterBot;

public class ToggleAnsweringCommand extends Command {

	@Override
	public void execute(TwitterBot twitterBot) throws TwitterException {
		if(!twitterBot.isAnswering())
			twitterBot.startAnswering();
		else
			twitterBot.stopAnswering();

	}

}
