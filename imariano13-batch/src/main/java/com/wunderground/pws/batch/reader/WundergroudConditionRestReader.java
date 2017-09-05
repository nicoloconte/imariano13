package com.wunderground.pws.batch.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wunderground.pws.model.Condition;
import com.wunderground.pws.model.entities.WuResponseLog;
import com.wunderground.pws.persistence.repositories.WuResponseRepository;

public class WundergroudConditionRestReader extends AbstractWundergroudRestReader<Condition> {

	private static final Logger log = LoggerFactory.getLogger(WundergroudConditionRestReader.class);

	private ObjectMapper objectMapper;
	private WuResponseRepository wuResponseRepository;

	public WundergroudConditionRestReader(String restUrl, String wuToken, WuResponseRepository wuResponseRespository) {
		super(restUrl, wuToken);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		this.objectMapper = objectMapper;
		this.wuResponseRepository = wuResponseRespository;
	}

	@Override
	public Condition convertToObject(String response) throws Exception {
		Condition condition = objectMapper.readValue(response, Condition.class);
		log.debug("converted condition is {}", condition.toString());
		if (condition == null || condition.getCurrentObservation() == null) {
			log.error("ATTENTION! NULL condition returned");
			return null;
		}
		return condition;
	}

	@Override
	public void doAdditionalLogic(String response) {
		log.debug("additional logic is saving WU response to DB");
		WuResponseLog wuResponseLog = new WuResponseLog();
		wuResponseLog.setResponse(response);
		wuResponseRepository.save(wuResponseLog);
	}

	@Override
	public Logger getLogger() {
		return log;
	}
}
