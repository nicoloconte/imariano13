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

import com.wunderground.pws.batch.listener.JobCompletionNotificationListener;
import com.wunderground.pws.batch.processor.ConditionItemProcessor;
import com.wunderground.pws.batch.reader.WundergroudConditionRestReader;
import com.wunderground.pws.batch.writer.DynamoDBConditionsItemWriter;
import com.wunderground.pws.model.Condition;
import com.wunderground.pws.model.entities.CurrentObservation;
import com.wunderground.pws.persistence.repositories.ConditionRepository;
import com.wunderground.pws.persistence.repositories.LastObservationRepository;
import com.wunderground.pws.persistence.repositories.MinMaxRepository;
import com.wunderground.pws.persistence.repositories.WuResponseRepository;

@Configuration
@EnableBatchProcessing
@Import({ BatchScheduler.class })
@ComponentScan("com.wunderground.pws.batch")
public class BatchConditionsConfiguration {

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
	private WuResponseRepository wuResponseRespository;
	@Autowired
	private LastObservationRepository lastObservationRepository;
	@Value("${wu.token}")
	private String token;
	@Value("${wu.condition.url}")
	private String conditionUrl;
	
	@Bean
	public WundergroudConditionRestReader conditionReader() {
		WundergroudConditionRestReader reader = new WundergroudConditionRestReader(conditionUrl, token, wuResponseRespository);
		return reader;
	}

	@Bean
	public DynamoDBConditionsItemWriter conditionWriter() {
		DynamoDBConditionsItemWriter writer = new DynamoDBConditionsItemWriter(conditionRepository, minMaxRepository, lastObservationRepository);
		return writer;
	}

	@Bean
	public ConditionItemProcessor conditionProcessor() {
		return new ConditionItemProcessor();
	}

	@Bean
	public Job conditionJob() {
		return jobBuilderFactory.get("importImariano13ConditionJob").incrementer(new RunIdIncrementer())
				.listener(listener).flow(conditionStep()).end().build();
	}

	@Bean
	public Step conditionStep() {
		return stepBuilderFactory.get("importImariano13ConditionStep").<Condition, CurrentObservation>chunk(1)
				.reader(conditionReader()).processor(conditionProcessor()).writer(conditionWriter()).build();
	}

	@Scheduled(cron = "0 */5 * ? * *")
	public void conditionPerform() throws Exception {
		JobParameters param = new JobParametersBuilder()
				.addString("importImariano13ConditionStepJobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		jobLauncher.run(conditionJob(), param);
	}

}
