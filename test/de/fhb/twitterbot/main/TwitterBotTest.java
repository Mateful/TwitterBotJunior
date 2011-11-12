package de.fhb.twitterbot.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import de.fhb.twitterbot.main.TwitterBot;

public class TwitterBotTest {
	private static TwitterBot bot;

	@BeforeClass
	public static void setUp() {
		bot = new TwitterBot();
	}

	@Test
	public void testGeneratedStatusResponseContainsName() {
		String answer = bot.generateStatusResponse("Ben");
		assertTrue(answer.contains("@Ben"));
	}

	@Test
	public void testGeneratedStatusResponseWithUberString() {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < Integer.MAX_VALUE / 100; i++)
			sb.append((char)(Math.random() * 255));
		String uberString = sb.toString();
		String answer = bot.generateStatusResponse(uberString);
		assertTrue(answer.contains("@" + uberString));
	}

	@Test(expected = RuntimeException.class)
	public void testGenerateStatusResponseThrowsExceptionWhenNameIsEmpty()
			throws Exception {
		bot.generateStatusResponse("");
	}
}
