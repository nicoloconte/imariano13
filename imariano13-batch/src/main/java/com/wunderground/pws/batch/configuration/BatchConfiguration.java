package com.wunderground.pws.batch.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wunderground.pws.batch.listener.JobCompletionNotificationListener;
import com.wunderground.pws.batch.processor.ConditionItemProcessor;
import com.wunderground.pws.batch.reader.WundergroudRestReader;
import com.wunderground.pws.batch.writer.DynamoDBItemWriter;
import com.wunderground.pws.model.Condition;
import com.wunderground.pws.model.CurrentObservation;
import com.wunderground.pws.persistence.repositories.ConditionRepository;
import com.wunderground.pws.persistence.repositories.LastObservationRepository;
import com.wunderground.pws.persistence.repositories.MinMaxRepository;

@Configuration
@EnableBatchProcessing
@Import({ BatchScheduler.class })
@ComponentScan("com.wunderground.pws.batch")
public class BatchConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private JobCompletionNotificationListener listener;
	@Autowired
	private ConditionRepository conditionRepository;
	@Autowired
	private MinMaxRepository minMaxRepository;
	@Autowired
	private LastObservationRepository lastObservationRepository;
	@Value("${wu.token}")
	private String token;
	@Value("${wu.condition.url}")
	private String conditionUrl;
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper;
	}

	@Bean
	public WundergroudRestReader reader() {
		WundergroudRestReader reader = new WundergroudRestReader(conditionUrl, token);
		return reader;
	}

	@Bean
	public DynamoDBItemWriter writer() {
		DynamoDBItemWriter writer = new DynamoDBItemWriter(conditionRepository, minMaxRepository, lastObservationRepository);
		return writer;
	}

	@Bean
	public ConditionItemProcessor processor() {
		return new ConditionItemProcessor();
	}

	@Bean
	public Job job() {
		return jobBuilderFactory.get("importImariano13ConditionJob").incrementer(new RunIdIncrementer())
				.listener(listener).flow(step()).end().build();
	}

	@Bean
	public Step step() {
		return stepBuilderFactory.get("importImariano13ConditionStep").<Condition, CurrentObservation>chunk(1)
				.reader(reader()).processor(processor()).writer(writer()).build();
	}

	@Scheduled(cron = "0 */5 * ? * *")
	public void perform() throws Exception {
		JobParameters param = new JobParametersBuilder()
				.addString("importImariano13ConditionStepJobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();

		jobLauncher.run(job(), param);
	}

}
