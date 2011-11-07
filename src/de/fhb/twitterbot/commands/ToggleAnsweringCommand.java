package de.fhb.twitterbot.commands;

import twitter4j.TwitterException;
import de.fhb.twitterbot.main.TwitterBot;

public class ToggleAnsweringCommand extends Command {
	@Override
	public void execute(TwitterBot twitterBot) throws TwitterException {
		twitterBot.setAnswering(!twitterBot.isAnswering());
	}
}
