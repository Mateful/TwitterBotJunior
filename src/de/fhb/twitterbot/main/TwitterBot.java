package de.fhb.twitterbot.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
	public final String LAST_ANSWERED_ID_FILE = "lastAnswered.txt";
	public final String ANSWER_FILE = "answers.txt";

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

		answering = false;
		answers = new ArrayList<String>();
		readAnswers(ANSWER_FILE);

		twitterStream.addListener(new UserStreamAdapter() {
			@Override
			public void onStatus(Status s) {
				try {
					onIncomingStatus(s);
					
					if(s.getText().contains("@" + twitter.getScreenName()))
						onMention(s);
				} catch(Exception e) {
					notifyObservers(e);
				}
			}
		});

		twitterStream.user();
	}

	private void onIncomingStatus(Status s) {
		notifyObservers(s.getUser().getScreenName() + " sent: " + s.getText());
	}

	private void readAnswers(String filename) {
		try {
			String input;
			BufferedReader br = new BufferedReader(new FileReader(filename));
			do {
				input = br.readLine();
				if(input != null) {
					// System.out.println(input);
					answers.add(input);
				}
			} while(input != null);

			br.close();
		} catch(FileNotFoundException e) {} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private void answerMention(Status s) {
		String newStatusMessage;
		Random random = new Random();

		try {
			newStatusMessage = "@" + s.getUser().getScreenName() + " "
					+ answers.get(random.nextInt(answers.size()));
			updateStatus(newStatusMessage);
			notifyObservers("generated answer: " + newStatusMessage);
		} catch(TwitterException e) {
			notifyObservers(e);
		}
	}
	
	public void receiveCommand(Command command) {
		try {
			command.execute(this);
		} catch(TwitterException e) {
			e.printStackTrace();
		}
	}

	public Status updateStatus(String status) throws TwitterException {
		return twitter.updateStatus(status);
	}

	public void createFriendship(String name) throws TwitterException {
		twitter.createFriendship(name);
	}

	private void onMention(Status s) {
		if(answering) {
			answerMention(s);
		}
	}
	
	public void setAnswering(boolean b) {
		answering = b;
	}

	public boolean isAnswering() {
		return answering;
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
