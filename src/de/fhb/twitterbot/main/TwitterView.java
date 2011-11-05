package de.fhb.twitterbot.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.fhb.twitterbot.commands.ExitCommand;
import de.fhb.twitterbot.commands.FollowCommand;
import de.fhb.twitterbot.commands.ToggleAnsweringCommand;
import de.fhb.twitterbot.commands.UpdateStatusCommand;

public class TwitterView implements Runnable {
	private TwitterController controller;
	private Thread thread;
	private BufferedReader inputReader;
	private boolean running;

	public TwitterView(TwitterController controller) {
		this.controller = controller;
		inputReader = new BufferedReader(new InputStreamReader(System.in));
	}

	public void start() {
		thread = new Thread(this);
		running = true;
		thread.start();
	}

	@Override
	public void run() {
		while (running) {
			printMenu();
			processInput(getInput());
		}
	}

	private void processInput(String input) {
		int commandNumber;

		try {
			commandNumber = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			commandNumber = -1;
		}

		switch (commandNumber) {
		case 0:
			controller.receiveCommand(new ExitCommand());
			break;
		case 1:
			System.out.println("Enter name: ");
			controller.receiveCommand(new FollowCommand(getInput()));
			break;
		case 2:
			controller.receiveCommand(new ToggleAnsweringCommand());
			break;
		case 3:
			System.out.println("Enter your new status: ");
			controller.receiveCommand(new UpdateStatusCommand(getInput()));
			break;
		default:
			System.out.println("Unknown Command");
			break;
		}
	}

	private String getInput() {
		String input = "";

		try {
			input = inputReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return input;
	}

	private void printMenu() {
		final String menu = "TwitterBotJunior by Marco and Benjamin\n" + "\n"
				+ "Press\n" + " <1> to follow another twitter user\n"
				+ " <2> to toggle automatic answering of mentions\n"
				+ " <3> to update your status manually\n"
				+ " <0> to close TwitterBotJunior\n";
		System.out.print(menu);
		System.out.print("Automatic answering of mentions is "
				+ (controller.isAnswering() ? "enabled" : "disabled") + '\n');

		System.out.println("Your Choice: ");
	}
	
	public void printErrorMessage(String message) {
		System.out.println(message);
	}
}
