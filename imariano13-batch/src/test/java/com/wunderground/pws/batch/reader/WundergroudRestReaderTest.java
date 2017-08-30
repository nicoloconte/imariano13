package com.wunderground.pws.batch.reader;

import static org.junit.Assert.fail;

import org.junit.Test;

public class WundergroudRestReaderTest {

	@Test
	public void readTest() {
		WundergroudRestReader reader = new WundergroudRestReader("http://api.wunderground.com/api/{0}/conditions/q/CA/San_Francisco.json?q={1}", "6a2727817a00a19f");
		try {
			reader.read();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}