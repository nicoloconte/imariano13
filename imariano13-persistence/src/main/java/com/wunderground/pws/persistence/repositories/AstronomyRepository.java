package com.wunderground.pws.persistence.repositories;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.wunderground.pws.model.entities.MoonPhase;

@EnableScan
public interface AstronomyRepository extends CrudRepository<MoonPhase, String>{

}
