package de.fhb.twitterbot.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;

import de.fhb.twitterbot.commands.ExitCommand;
import de.fhb.twitterbot.commands.FollowCommand;
import de.fhb.twitterbot.commands.ToggleAnsweringCommand;
import de.fhb.twitterbot.commands.UpdateStatusCommand;

public class TwitterView implements Runnable, Observer {
	private TwitterBot twitterbot;
	private Thread thread;
	private BufferedReader inputReader;
	private boolean running;

	public TwitterView(TwitterBot controller) {
		this.twitterbot = controller;
		inputReader = new BufferedReader(new InputStreamReader(System.in));
		twitterbot.addObserver(this);
	}

	public void start() {
		thread = new Thread(this);
		running = true;
		thread.start();
	}

	@Override
	public void run() {
		while(running) {
			printMenu();
			processInput(getInput());
		}
	}

	private void processInput(String input) {
		switch(getCommandNumber(input)) {
		case 0:
			twitterbot.receiveCommand(new ExitCommand());
			break;
		case 1:
			System.out.println("Enter name: ");
			twitterbot.receiveCommand(new FollowCommand(getInput()));
			break;
		case 2:
			twitterbot.receiveCommand(new ToggleAnsweringCommand());
			break;
		case 3:
			System.out.println("Enter your new status: ");
			twitterbot.receiveCommand(new UpdateStatusCommand(getInput()));
			break;
		default:
			System.out.println("Unknown Command");
			break;
		}
	}

	private int getCommandNumber(String input) {
		int commandNumber;
		try {
			commandNumber = Integer.parseInt(input);
		} catch(NumberFormatException e) {
			commandNumber = -1;
		}
		return commandNumber;
	}

	private String getInput() {
		String input = "";
		try {
			input = inputReader.readLine();
		} catch(IOException e) {
			printErrorMessage(e.getMessage());
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
				+ (twitterbot.isAnswering() ? "enabled" : "disabled") + '\n');
	}

	private void printMessage(String string) {
		System.out.println(string);
	}

	private void printErrorMessage(String message) {
		System.out.println(message);
	}

	@Override
	public void update(Observable observable, Object argument) {
		if(argument instanceof Exception)
			printErrorMessage(((Exception)argument).getMessage());
		else if(argument instanceof String)
			printMessage((String)argument);
	}
}
