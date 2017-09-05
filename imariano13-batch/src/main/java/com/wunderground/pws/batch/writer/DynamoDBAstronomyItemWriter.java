package com.wunderground.pws.batch.writer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.wunderground.pws.model.entities.MoonPhase;
import com.wunderground.pws.persistence.repositories.AstronomyRepository;

public class DynamoDBAstronomyItemWriter implements ItemWriter<MoonPhase> {

	private static final Logger log = LoggerFactory.getLogger(DynamoDBAstronomyItemWriter.class);

	private AstronomyRepository astronomyRepository;

	public DynamoDBAstronomyItemWriter(AstronomyRepository astronomyRepository) {
		super();
		this.astronomyRepository = astronomyRepository;
	}

	@Override
	public void write(List<? extends MoonPhase> items) throws Exception {
		log.debug("saving {} items", items.size());
		for (MoonPhase moonPhase : items) {
			saveLastAstronomy(moonPhase);
		}
	}

	private void saveLastAstronomy(MoonPhase moonPhase) {
		log.info("saving last astronomy");
		long count = astronomyRepository.count();
		log.debug("last astronomy count {}", count);
		switch ((int) count) {
		case 0:
			break;
		case 1:
			break;
		default:
			Iterable<MoonPhase> allAstronomy = astronomyRepository.findAll();
			List<MoonPhase> astronomyList = new ArrayList<>();
			allAstronomy.iterator().forEachRemaining(astronomyList::add);
			Collections.sort(astronomyList, new Comparator<MoonPhase>() {
				@Override
				public int compare(MoonPhase o1, MoonPhase o2) {
					if(o1.getTimestamp().isAfter(o2.getTimestamp())){
						return 1;
					}
					return -1;
				}
			});
			log.debug("deleting astronomy {}", astronomyList.get(0));
			astronomyRepository.delete(astronomyList.get(0));
			break;
		}
		log.debug("saving NEW last astronomy {}", moonPhase);
		astronomyRepository.save(moonPhase);
	}

}
