package com.wunderground.pws.persistence.repositories;

import java.time.ZonedDateTime;
import java.util.List;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.wunderground.pws.model.entities.CurrentObservation;

@EnableScan
public interface ConditionRepository extends CrudRepository<CurrentObservation, String>{

	public List<CurrentObservation> findByObservationTimeBetween(ZonedDateTime from, ZonedDateTime to);
}
