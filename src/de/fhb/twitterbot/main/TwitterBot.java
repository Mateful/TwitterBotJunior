package de.fhb.twitterbot.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.UserStreamAdapter;
import de.fhb.twitterbot.commands.Command;

public class TwitterBot extends Observable {
	public final String ANSWER_FILE = "answers.txt";
	public final String STANDARD_ANSWER = "42";

	private Twitter twitter;
	private TwitterStream twitterStream;

	private boolean answering;
	private ArrayList<String> answers;

	public TwitterBot() {
		this(new TwitterFactory().getInstance(), new TwitterStreamFactory()
				.getInstance());
	}

	public TwitterBot(Twitter t, TwitterStream s) {
		twitter = t;
		twitterStream = s;

		answering = true;
		answers = new ArrayList<String>();
		readAnswers(ANSWER_FILE);
		addListener();
		twitterStream.user();
	}

	private void addListener() {
		twitterStream.addListener(new UserStreamAdapter() {
			@Override
			public void onStatus(Status s) {
				try {
					onIncomingStatus(s);

					if (s.getText().contains("@" + twitter.getScreenName()))
						onMention(s);
				} catch (Exception e) {
					notifyObservers(e);
				}
			}
		});
	}

	private void onIncomingStatus(Status s) {
		notifyObservers(s.getUser().getScreenName() + "'s status update: "
				+ s.getText());
	}

	private void readAnswers(String filename) {
		try {
			String input;
			BufferedReader br = new BufferedReader(new FileReader(filename));
			do {
				input = br.readLine();
				if (input != null) {
					answers.add(input);
				}
			} while (input != null);

			br.close();
		} catch (Exception e) {
			System.err.println(e.getMessage() + "\nAnswer set to: "
					+ STANDARD_ANSWER);
			answers.add(STANDARD_ANSWER);
		}
	}

	private void answerMention(Status s) {
		String newStatusMessage;
		Random random = new Random();

		newStatusMessage = "@" + s.getUser().getScreenName() + " "
				+ answers.get(random.nextInt(answers.size()));
		notifyObservers("generated answer: " + newStatusMessage);
		updateStatus(newStatusMessage);
	}

	public void receiveCommand(Command command) {
		try {
			command.execute(this);
		} catch (TwitterException e) {
			notifyObservers(e);
		}
	}

	public void updateStatus(String status) {
		try {
			twitter.updateStatus(status);
		} catch (TwitterException e) {
			if (!isDuplcateStatusUpdateError(e))
				notifyObservers(e);
		}
	}

	public void createFriendship(String name) throws TwitterException {
		twitter.createFriendship(name);
	}

	private void onMention(Status s) {
		if (answering) {
			answerMention(s);
		}
	}

	public void setAnswering(boolean b) {
		answering = b;
	}

	public boolean isAnswering() {
		return answering;
	}

	private boolean isDuplcateStatusUpdateError(TwitterException e) {
		return e.getStatusCode() == 403;
	}

	private void notifyObservers(Exception e) {
		setChanged();
		super.notifyObservers(e);
	}

	private void notifyObservers(String s) {
		setChanged();
		super.notifyObservers(s);
	}
}
