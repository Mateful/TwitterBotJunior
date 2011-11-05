package de.fhb.twitterbot.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.fhb.twitterbot.commands.ExitCommand;
import de.fhb.twitterbot.commands.UpdateStatusCommand;

public class TwitterView implements Runnable {
	private TwitterController controller;
	private Thread thread;
	private BufferedReader inputReader;
	private boolean running;

	public TwitterView(TwitterController controller) {
		this.controller = controller;
		inputReader = new BufferedReader(new InputStreamReader(System.in));
		thread = new Thread(this);
		this.start();
	}

	private void start() {
		printMenu();
		running = true;
		thread.start();
	}

	@Override
	public void run() {
		while(running) {
			processInput(getInput());
		}
	}

	private void processInput(String input) {
		if(input.equals(Integer.toString(0))) {
			controller.receiveCommand(new ExitCommand());
		} else if(input.equals(Integer.toString(3))) {
			controller.receiveCommand(new UpdateStatusCommand(getNewStatusMessage()));
		}
	}

	private String getNewStatusMessage() {
		String newStatus;

		System.out.println("Enter your new status: ");
		newStatus = getInput();
		return newStatus;
	}

	private String getInput() {
		String input = "";

		try {
			input = inputReader.readLine();
		} catch(IOException e) {
			e.printStackTrace();
		}

		return input;
	}

	public void printMenu() {
		final String menu = "TwitterBotJunior by Marco and Benjamin\n" + "\n"
				+ "Press\n" + " <1> to follow another twitter user\n"
				+ " <2> to automatically answer mentions\n"
				+ " <3> to update your status manually\n"
				+ " <0> to close TwitterBotJunior\n" + "Your Choice: ";

		System.out.println(menu);
	}

}
