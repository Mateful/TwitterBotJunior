package de.fhb.twitterbot.main;

import java.io.FileNotFoundException;
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
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import de.fhb.twitterbot.Exceptions.TokenNotFoundException;
import de.fhb.twitterbot.commands.Command;
import de.fhb.twitterbot.util.Serializer;
import de.fhb.twitterbot.util.TextFileReader;

public class TwitterBot extends Observable {
	public final String STANDARD_ACCOUNT = "MatefulBot";
	private final String ANSWER_FILE = "answers.txt";
	private final String STANDARD_ANSWER = "42";

	private Twitter twitter;
	private TwitterStream twitterStream;
	private RequestToken requestToken;
	private AccessToken accessToken;
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
		readAnswersFile();
		addListener();
	}

	private void readAnswersFile() {
		try {
			answers = TextFileReader.readTextFileLineByLine(ANSWER_FILE);
		} catch(IOException e) {
			System.err.println(e.getMessage() + "\nAnswer set to: "
					+ STANDARD_ANSWER);
			answers.add(STANDARD_ANSWER);
		}
	}

	private void addListener() {
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
	}

	public void startAuthentification() {
		try {
			requestToken = twitter.getOAuthRequestToken();
		} catch(TwitterException e) {
			notifyObservers(e);
		}
	}

	public String getAuthentificationLink() {
		return requestToken.getAuthorizationURL();
	}

	public void getAccessTokenFromTwitter(String pin) {
		try {
			if(pin.length() > 0)
				accessToken = twitter.getOAuthAccessToken(requestToken, pin);
			else
				accessToken = twitter.getOAuthAccessToken();
		} catch(TwitterException e) {
			notifyObservers(e);
		}
	}

	public void startStream() {
		twitter.setOAuthAccessToken(accessToken);
		twitterStream.setOAuthAccessToken(accessToken);
		twitterStream.user();
	}

	private void onIncomingStatus(Status s) {
		notifyObservers(s.getUser().getScreenName() + "'s status update: "
				+ s.getText());
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
		} catch(TwitterException e) {
			notifyObservers(e);
		}
	}

	public void updateStatus(String status) {
		try {
			twitter.updateStatus(status);
		} catch(TwitterException e) {
			if(!isDuplcateStatusUpdateError(e))
				notifyObservers(e);
		}
	}

	public void createFriendship(String name) throws TwitterException {
		twitter.createFriendship(name);
	}

	private void onMention(Status s) {
		if(answering)
			answerMention(s);
	}

	public void loadDefaultAccessToken() {
		try {
			loadAccessToken(STANDARD_ACCOUNT);
		} catch(TokenNotFoundException e) {
			notifyObservers(e);
		}
	}

	public void loadAccessToken(String token) throws TokenNotFoundException {
		try {
			accessToken = (AccessToken)Serializer.load(token);
		} catch(FileNotFoundException e) {
			throw new TokenNotFoundException(token + " token not found.");
		} catch(IOException e) {
			notifyObservers(e);
		} catch(ClassNotFoundException e) {
			notifyObservers(new Exception(
					"AccessToken class was not found, the library is probably corrupted."));
		}
	}

	public void saveAccessToken() {
		try {
			Serializer.save(accessToken, accessToken.getScreenName());
		} catch(IOException e) {
			notifyObservers(e);
		}
	}

	public String getUserName() {
		return accessToken.getScreenName();
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
