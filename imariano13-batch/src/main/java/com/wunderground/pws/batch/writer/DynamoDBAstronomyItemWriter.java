package com.wunderground.pws.batch.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.wunderground.pws.model.entities.MoonPhase;
import com.wunderground.pws.service.AstronomyService;

public class DynamoDBAstronomyItemWriter implements ItemWriter<MoonPhase> {

	private static final Logger log = LoggerFactory.getLogger(DynamoDBAstronomyItemWriter.class);

	private AstronomyService astronomyService;

	public DynamoDBAstronomyItemWriter(AstronomyService astronomyService) {
		super();
		this.astronomyService = astronomyService;
	}

	@Override
	public void write(List<? extends MoonPhase> items) throws Exception {
		log.debug("saving {} items", items.size());
		for (MoonPhase moonPhase : items) {
			astronomyService.saveAstronomy(moonPhase);
		}
	}

}
