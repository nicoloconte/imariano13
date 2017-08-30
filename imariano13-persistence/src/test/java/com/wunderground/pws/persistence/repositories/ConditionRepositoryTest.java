package com.wunderground.pws.persistence.repositories;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;

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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wunderground.pws.model.Condition;
import com.wunderground.pws.model.CurrentObservation;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = PersistenceApplication.class)
public class ConditionRepositoryTest {

	private DynamoDBMapper dynamoDBMapper;

	@Autowired
	private AmazonDynamoDB amazonDynamoDB;

	@Autowired
	private ConditionRepository repository;

	@Before
	public void setup() throws Exception {
		dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

		CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(CurrentObservation.class);
		tableRequest.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));
		try {
			amazonDynamoDB.describeTable("Conditions");
		} catch(ResourceNotFoundException ex){
			amazonDynamoDB.createTable(tableRequest);
		}
	}

	@Test
	public void sampleTestCase() {
		InputStream is = getClass().getResourceAsStream("/imariano13.json");
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		Condition condition = null;
		try {
			condition = mapper.readValue(is, Condition.class);
			assertNotNull(condition);
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		repository.save(condition.getCurrentObservation());
		dynamoDBMapper.batchDelete(repository.findAll());
	}

}
