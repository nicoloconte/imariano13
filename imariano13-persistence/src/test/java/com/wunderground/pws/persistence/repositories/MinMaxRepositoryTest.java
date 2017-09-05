package com.wunderground.pws.persistence.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.ZonedDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.wunderground.pws.model.entities.ObservationMinMax;
import com.wunderground.pws.persistence.configuration.PersistenceApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = PersistenceApplication.class)
public class MinMaxRepositoryTest {

	private DynamoDBMapper dynamoDBMapper;

	@Autowired
	private AmazonDynamoDB amazonDynamoDB;

	@Autowired
	private MinMaxRepository repository;

	@Before
	public void setup() throws Exception {
		dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

		CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(ObservationMinMax.class);
		tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
		try {
			amazonDynamoDB.describeTable("ConditionsMinMax");
		} catch (ResourceNotFoundException ex) {
			amazonDynamoDB.createTable(tableRequest);
		}
	}

	@Test
	public void sampleTestCase() {
		ObservationMinMax minMax = new ObservationMinMax();
		minMax.setId("2017");
		minMax.setTempC(new Double("35"));
		minMax.setTempCObservationTime(ZonedDateTime.now());

		repository.save(minMax);

		minMax = repository.findOne("2017");
		assertNotNull(minMax);

		minMax.setTempC(new Double("30"));
		minMax.setTempCObservationTime(ZonedDateTime.now());
		repository.save(minMax);

		minMax = repository.findOne("2017");
		assertNotNull(minMax);
		assertEquals(new Double("30"), minMax.getTempC());

		dynamoDBMapper.batchDelete(repository.findAll());
	}

}
