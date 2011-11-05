package de.fhb.twitterbot.main;

import java.util.Observable;
import java.util.Observer;

import twitter4j.TwitterException;
import de.fhb.twitterbot.ai.TwitterBot;
import de.fhb.twitterbot.commands.Command;

public class TwitterController implements Observer {
	private TwitterBot twitterBot;
	private TwitterView view;

	public TwitterController() {
		twitterBot = new TwitterBot();
		twitterBot.addObserver(this);

		view = new TwitterView(this);
		view.start();
	}

	public void receiveCommand(Command command) {
		try {
			command.execute(twitterBot);
		} catch(TwitterException e) {
			e.printStackTrace();
		}
	}

	public boolean isAnswering() {
		return twitterBot.isAnswering();
	}

	@Override
	public void update(Observable observable, Object argument) {
		if(argument instanceof TwitterException)
			view.printErrorMessage(((TwitterException)argument).getMessage());
	}
}
