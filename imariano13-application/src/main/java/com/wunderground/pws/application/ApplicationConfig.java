package com.wunderground.pws.application;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.wunderground.pws.batch.configuration.BatchAstronomyConfiguration;
import com.wunderground.pws.batch.configuration.BatchConditionsConfiguration;
import com.wunderground.pws.persistence.configuration.DynamoDBConfiguration;
import com.wunderground.pws.rest.configuration.RestConfiguration;
import com.wunderground.pws.service.configuration.ServiceConfiguration;

@Configuration
@Import({ BatchConditionsConfiguration.class, BatchAstronomyConfiguration.class, DynamoDBConfiguration.class, ServiceConfiguration.class, RestConfiguration.class })
public class ApplicationConfig {

}
