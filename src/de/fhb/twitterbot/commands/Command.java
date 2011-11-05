package de.fhb.twitterbot.commands;

import twitter4j.TwitterException;
import de.fhb.twitterbot.ai.TwitterBot;

public abstract class Command {
	public abstract void execute(TwitterBot twitterBot) throws TwitterException;
}
