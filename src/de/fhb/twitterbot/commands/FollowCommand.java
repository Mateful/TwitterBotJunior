package de.fhb.twitterbot.commands;

import twitter4j.TwitterException;
import de.fhb.twitterbot.ai.TwitterBot;

public class FollowCommand extends Command {
	private String followerName;

	public FollowCommand(String followerName) {
		this.followerName = followerName;
	}

	@Override
	public void execute(TwitterBot twitterBot) throws TwitterException {
		twitterBot.createFriendship(followerName);
	}

}
