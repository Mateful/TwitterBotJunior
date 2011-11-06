package de.fhb.twitterbot.commands;

import de.fhb.twitterbot.main.TwitterBot;

public class ExitCommand extends Command {
	@Override
	public void execute(TwitterBot twitterBot) {
		System.exit(0);
	}
}
