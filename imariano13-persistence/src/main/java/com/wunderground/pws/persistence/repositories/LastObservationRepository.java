package com.wunderground.pws.persistence.repositories;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.wunderground.pws.model.LastObservation;

@EnableScan
public interface LastObservationRepository extends CrudRepository<LastObservation, String>{

}
