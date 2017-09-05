package com.wunderground.pws.batch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.wunderground.pws.model.WuAstronomy;
import com.wunderground.pws.model.entities.MoonPhase;

public class AstronomyItemProcessor implements ItemProcessor<WuAstronomy, MoonPhase>{

	private static final Logger log = LoggerFactory.getLogger(AstronomyItemProcessor.class);
	
	@Override
	public MoonPhase process(final WuAstronomy astronomy) throws Exception {
		log.info("start processing");
		log.debug(astronomy.toString());
		log.info("end processing");
		return astronomy.getMoonPhase();
	}

}
