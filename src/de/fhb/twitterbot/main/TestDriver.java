package de.fhb.twitterbot.main;

/*
 * app website: https://dev.twitter.com/apps/1364050/show
 */

public class TestDriver {
	public static void main(String[] args) {
		TwitterView tv = new TwitterView(new TwitterBot());
		tv.start();
	}
}
