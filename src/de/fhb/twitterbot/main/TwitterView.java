package de.fhb.twitterbot.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;

import de.fhb.twitterbot.Exceptions.TokenNotFoundException;
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
		login();
	}

	private void login() {
		boolean loggedIn = false;
		while(!loggedIn) {
			String name = chooseAccount();
			try {
				twitterbot.loadAccessToken(name);
				loggedIn = true;
			} catch(TokenNotFoundException e) {
				printErrorMessage(name + " token was not found.");
				System.out.println("Create new token? (y/n):");
				if(getInput().equals("y")) {
					createToken();
					loggedIn = true;
				}
			}
		}
		twitterbot.startStream();
	}

	private String chooseAccount() {
		System.out
				.println("Enter your Twitter username you want to use the bot with. Leave blank for logging in with MatefulBot.");
		String input = getInput();
		if(input.equals(""))
			input = "MatefulBot";
		return input;
	}

	private void createToken() {
		twitterbot.startAuthentification();
		System.out
				.println("Open the following URL and grant access to your account:");
		System.out.println(twitterbot.getAuthentificationLink());
		System.out.print("Enter the PIN:");
		twitterbot.getAccessTokenFromTwitter(getInput());
		twitterbot.saveAccessToken();
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
			try {
				Thread.sleep(100);
			} catch(InterruptedException e) {
				printErrorMessage(e.getMessage());
			}
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
			System.err.println("Unknown Command");
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
		final String menu = "TwitterBotJunior by Marco and Benjamin\n"
				+ "---------------------------------------" + "\n"
				+ "Logged in as: " + twitterbot.getUserName() + "\n"
				+ "Press\n" + " <1> to follow another twitter user\n"
				+ " <2> to toggle automatic answering of mentions\n"
				+ " <3> to update your status manually\n"
				+ " <0> to close TwitterBotJunior\n";
		System.out.print(menu);
		System.out.print("Automatic answering of mentions is "
				+ (twitterbot.isAnswering() ? "enabled" : "disabled") + '\n');
	}

	private void printMessage(String message) {
		System.out.println(message);
	}

	private void printErrorMessage(String errorMessage) {
		System.err.println(errorMessage);
	}

	@Override
	public void update(Observable observable, Object argument) {
		if(argument instanceof Exception)
			printErrorMessage(((Exception)argument).getMessage());
		else if(argument instanceof String)
			printMessage((String)argument);
	}
}
