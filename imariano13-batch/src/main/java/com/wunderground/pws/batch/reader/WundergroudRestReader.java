package com.wunderground.pws.batch.reader;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wunderground.pws.model.Condition;

public class WundergroudRestReader implements ItemReader<Condition> {

	private static final Logger log = LoggerFactory.getLogger(WundergroudRestReader.class);

	private RestTemplate restTemplate;
	private ObjectMapper objectMapper;
	private String restUrl;
	private String wuToken;

	private boolean wuCalled = Boolean.FALSE;

	public WundergroudRestReader(String restUrl, String wuToken) {
		super();
		this.restTemplate = new RestTemplate();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		this.objectMapper = objectMapper;
		this.restUrl = restUrl;
		this.wuToken = wuToken;
	}

	@Override
	public Condition read() throws Exception {
		log.debug("wuCalled[{}]", wuCalled);
		if(wuCalled){
			wuCalled = Boolean.FALSE;
			return null;
		}
		return callWundergroud();
	}

	private Condition callWundergroud() throws Exception {
		log.info("start calling WU");
		log.debug("endpoint is {}", restUrl);
		log.debug("WU token is {}", wuToken);
		String response = restTemplate.getForObject(restUrl, String.class, wuToken, RandomUtils.nextLong());
		log.debug("response from WU is {}", response);
		Condition condition = convertToCondition(response);
		wuCalled = Boolean.TRUE;
		return condition;
	}

	private Condition convertToCondition(String response) throws Exception {
		Condition condition = objectMapper.readValue(response, Condition.class);
		log.debug("converted condition is {}", condition.toString());
		if(condition == null || condition.getCurrentObservation() == null){
			log.error("ATTENTION! NULL condition returned");
			return null;
		}
		return condition;
	}
}
