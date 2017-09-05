package com.wunderground.pws.persistence.repositories;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.wunderground.pws.model.entities.CurrentObservation;
import com.wunderground.pws.persistence.configuration.PersistenceApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = PersistenceApplication.class)
public class ConditionRepositoryTest {

	@Autowired
	private ConditionRepository repository;

	@Test
	public void sampleTestCase() {
		List<CurrentObservation> findByObservationTimeBetween = repository.findByObservationTimeBetween(ZonedDateTime.now().minusHours(1),ZonedDateTime.now());
		System.out.println("size = " + findByObservationTimeBetween.size());
		for (CurrentObservation currentObservation : findByObservationTimeBetween) {
			System.out.println(DateTimeFormatter.ISO_ZONED_DATE_TIME.format(currentObservation.getObservationTime()));
		}
	}

}
