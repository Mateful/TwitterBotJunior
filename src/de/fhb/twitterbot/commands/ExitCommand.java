package de.fhb.twitterbot.commands;

import de.fhb.twitterbot.ai.TwitterBot;

public class ExitCommand extends Command {

	@Override
	public void execute(TwitterBot twitterBot) {
		System.exit(0);
	}

}
