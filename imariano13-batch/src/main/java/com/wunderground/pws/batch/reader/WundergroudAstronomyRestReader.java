package com.wunderground.pws.batch.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wunderground.pws.model.WuAstronomy;

public class WundergroudAstronomyRestReader extends AbstractWundergroudRestReader<WuAstronomy> {

	private static final Logger log = LoggerFactory.getLogger(WundergroudAstronomyRestReader.class);

	private ObjectMapper objectMapper;

	public WundergroudAstronomyRestReader(String restUrl, String wuToken) {
		super(restUrl, wuToken);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		this.objectMapper = objectMapper;
	}

	@Override
	public WuAstronomy convertToObject(String response) throws Exception {
		WuAstronomy wuAstronomy = objectMapper.readValue(response, WuAstronomy.class);
		log.debug("converted astronomy is {}", wuAstronomy.toString());
		if (wuAstronomy == null || wuAstronomy.getMoonPhase() == null) {
			log.error("ATTENTION! NULL astronomy returned");
			return null;
		}
		return wuAstronomy;
	}

	@Override
	public void doAdditionalLogic(String response) {
		log.debug("nothing to do!");
	}

	@Override
	public Logger getLogger() {
		return log;
	}
}
