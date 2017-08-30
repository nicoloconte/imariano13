package com.wunderground.pws.persistence.repositories;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.wunderground.pws.model.CurrentObservation;

@EnableScan
public interface ConditionRepository extends CrudRepository<CurrentObservation, String>{

}
