package com.wunderground.pws.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class AstronomyTest {

	@Test
	public void testDeserializzazioneOsservazione() throws IOException {
		InputStream is = getClass().getResourceAsStream("/imariano13astronomy.json");
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		try {
			WuAstronomy condition = mapper.readValue(is, WuAstronomy.class);
			assertNotNull(condition);
			System.out.println(condition);
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
