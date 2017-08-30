package com.wunderground.pws.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ConditionTest {

	@Test
	public void testDeserializzazioneOsservazione() throws IOException {
		InputStream is = getClass().getResourceAsStream("/imariano13.json");
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		try {
			Condition condition = mapper.readValue(is, Condition.class);
			assertNotNull(condition);
			System.out.println(condition);
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
