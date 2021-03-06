package com.wunderground.pws.persistence.repositories;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.wunderground.pws.model.entities.ObservationMinMax;

@EnableScan
public interface MinMaxRepository extends CrudRepository<ObservationMinMax, String>{

}
