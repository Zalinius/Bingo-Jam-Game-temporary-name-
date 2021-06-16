package com.zalinius.bingojam;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BingoJamExampleTest {

	@Test
	void testName() throws Exception {
		BingoJamGame game = new BingoJamGame();
		assertNotNull(game);
	}
	
}
