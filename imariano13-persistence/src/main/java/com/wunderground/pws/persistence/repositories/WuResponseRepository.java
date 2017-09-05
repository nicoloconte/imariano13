package com.wunderground.pws.persistence.repositories;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.wunderground.pws.model.entities.WuResponseLog;

@EnableScan
public interface WuResponseRepository extends CrudRepository<WuResponseLog, String>{

}
