package ml.rl.hw6;

import static org.junit.Assert.*;

import org.junit.Test;

public class BarBrawlTest {

	@Test
	public void test1() throws Exception {
		int numPatrons = 4;
		int[][] atEstablishment = { { 0, 1, 1, 1 }, { 0, 0, 0, 1 }, { 1, 1, 1, 1 }, { 0, 0, 1, 1 }, { 0, 1, 1, 1 }, { 0, 0, 0, 1 }, { 1, 1, 1, 1 },
				{ 0, 1, 1, 1 }, { 1, 1, 1, 1 }, { 0, 1, 1, 1 }, { 0, 0, 0, 1 }, { 0, 0, 1, 1 }, { 0, 0, 0, 1 }, { 1, 1, 1, 1 }, { 1, 1, 1, 1 }, { 1, 1, 1, 1 },
				{ 1, 1, 1, 1 }, { 0, 1, 1, 1 }, { 0, 0, 1, 1 }, { 1, 1, 1, 1 }, { 1, 1, 1, 1 }, { 0, 1, 1, 1 }, { 1, 1, 1, 1 }, { 1, 1, 1, 1 }, { 0, 0, 0, 1 },
				{ 0, 1, 1, 1 }, { 1, 1, 1, 1 }, { 0, 1, 1, 1 }, { 0, 0, 1, 1 }, { 0, 1, 1, 1 }, { 1, 1, 1, 1 }, { 1, 1, 1, 1 } };
		int[] fightOccurred = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		String res = "I DON'T KNOW,I DON'T KNOW,NO FIGHT,I DON'T KNOW,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT,NO FIGHT";
		new BarBrawl(numPatrons, atEstablishment, fightOccurred).run();
	}
}
