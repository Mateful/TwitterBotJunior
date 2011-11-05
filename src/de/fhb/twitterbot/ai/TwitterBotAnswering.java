package de.fhb.twitterbot.ai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import twitter4j.Status;
import twitter4j.TwitterException;

class TwitterBotAnswering implements Runnable {
	public final String LAST_ANSWERED_ID_FILE = "lastAnswered.txt";
	public final String ANSWER_FILE = "answers.txt";

	private Thread thread;
	private boolean running;
	private TwitterBot twitterBot;
	private long lastAnsweredID;
	private ArrayList<String> answers;

	public TwitterBotAnswering(TwitterBot twitterBot) {
		this.twitterBot = twitterBot;
		running = false;
		answers = new ArrayList<String>();
		readAnswers(ANSWER_FILE);
	}

	private void readLastAnsweredID(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			lastAnsweredID = Long.parseLong(br.readLine());
			br.close();
		} catch (FileNotFoundException e) {
			lastAnsweredID = 0;
		} catch (IOException e) {
			lastAnsweredID = 0;
			e.printStackTrace();
		} catch (NumberFormatException e) {
			lastAnsweredID = 0;
		}
	}

	private void writeLastAnsweredID(String filename) {
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(filename));
			br.write("" + lastAnsweredID);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readAnswers(String filename) {
		try {
			String input;
			BufferedReader br = new BufferedReader(new FileReader(filename));
			do {
				input = br.readLine();
				if (input != null) {
					// System.out.println(input);
					answers.add(input);
				}
			} while (input != null);

			br.close();
		} catch (FileNotFoundException e) {
			lastAnsweredID = 0;
		} catch (IOException e) {
			lastAnsweredID = 0;
			e.printStackTrace();
		} catch (NumberFormatException e) {
			lastAnsweredID = 0;
		}
	}

	public void start() {
		thread = new Thread(this);
		running = true;
		thread.start();
	}

	public void stop() {
		running = false;
	}

	public void answerMentions() throws TwitterException {
		List<Status> status = twitterBot.getMentions();
		int countNewMentions = 0;
		String newStatusMessage;
		Random random = new Random();

		for (Status s : status) {
			if (lastAnsweredID == s.getId())
				break;
			++countNewMentions;
			System.out.println("message from " + s.getUser().getName() + ":" + s.getText());
			try {
				newStatusMessage = "@" + s.getUser().getScreenName() + " "
						+ answers.get(random.nextInt(answers.size()));
				twitterBot.updateStatus(newStatusMessage);
				System.out.println("answer: " + newStatusMessage);
			} catch (TwitterException e) {
				// duplicate status messages are not noteworthy
			}
			System.out.println();
		}

		if (countNewMentions > 0) {
			lastAnsweredID = status.get(0).getId();
		}
	}

	@Override
	public void run() {
		final long intervalInMillis = 10000;
		long timeLastCheck = 0;
		
		readLastAnsweredID(LAST_ANSWERED_ID_FILE);

		while (running) {
			if (System.currentTimeMillis() - timeLastCheck > intervalInMillis) {
				try {
					answerMentions();
					writeLastAnsweredID(LAST_ANSWERED_ID_FILE);
				} catch (TwitterException e) {
					twitterBot.notifyObservers(e);
					stop();
				}

				timeLastCheck = System.currentTimeMillis();
			}

			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				twitterBot.notifyObservers(e);
			}
		}
	}

	public boolean isRunning() {
		return running;
	}
}
