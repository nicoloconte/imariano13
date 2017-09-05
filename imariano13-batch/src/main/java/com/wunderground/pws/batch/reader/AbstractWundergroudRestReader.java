package com.wunderground.pws.batch.reader;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractWundergroudRestReader<T extends Object> implements ItemReader<T> {

	private RestTemplate restTemplate;
	private String restUrl;
	private String wuToken;

	private boolean wuCalled = Boolean.FALSE;

	public AbstractWundergroudRestReader(String restUrl, String wuToken) {
		super();
		this.restTemplate = new RestTemplate();
		this.restUrl = restUrl;
		this.wuToken = wuToken;
	}

	@Override
	public T read() throws Exception {
		getLogger().debug("wuCalled[{}]", wuCalled);
		if (wuCalled) {
			wuCalled = Boolean.FALSE;
			return null;
		}
		return callWundergroud();
	}

	private T callWundergroud() throws Exception {
		getLogger().info("start calling WU");
		getLogger().debug("endpoint is {}", restUrl);
		getLogger().debug("WU token is {}", wuToken);
		String response = restTemplate.getForObject(restUrl, String.class, wuToken, RandomUtils.nextLong());
		getLogger().debug("response from WU is {}", response);
		doAdditionalLogic(response);
		T converted = convertToObject(response);
		wuCalled = Boolean.TRUE;
		return converted;
	}

	public abstract T convertToObject(String response) throws Exception;
	
	public abstract void doAdditionalLogic(String response);
	
	public abstract Logger getLogger();
}
