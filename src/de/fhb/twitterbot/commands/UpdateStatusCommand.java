package de.fhb.twitterbot.commands;

import twitter4j.TwitterException;
import de.fhb.twitterbot.ai.TwitterBot;

public class UpdateStatusCommand extends Command {
	private String newStatus;

	public UpdateStatusCommand(String newStatus) {
		this.newStatus = newStatus;
	}

	@Override
	public void execute(TwitterBot twitterBot) throws TwitterException {
		twitterBot.updateStatus(newStatus);
	}

}
