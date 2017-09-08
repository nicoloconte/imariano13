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
import com.wunderground.pws.batch.processor.AstronomyItemProcessor;
import com.wunderground.pws.batch.reader.WundergroudAstronomyRestReader;
import com.wunderground.pws.batch.writer.DynamoDBAstronomyItemWriter;
import com.wunderground.pws.model.WuAstronomy;
import com.wunderground.pws.model.entities.MoonPhase;
import com.wunderground.pws.service.AstronomyService;

@Configuration
@EnableBatchProcessing
@Import({ BatchAstronomyScheduler.class })
@ComponentScan("com.wunderground.pws.batch")
public class BatchAstronomyConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private JobCompletionNotificationListener listener;
	@Autowired
	private AstronomyService astronomyService;
	@Value("${wu.token}")
	private String token;
	@Value("${wu.astronomy.url}")
	private String astronomyUrl;
	
	@Bean
	public WundergroudAstronomyRestReader astronomyReader() {
		WundergroudAstronomyRestReader reader = new WundergroudAstronomyRestReader(astronomyUrl, token);
		return reader;
	}

	@Bean
	public DynamoDBAstronomyItemWriter astronomyWriter() {
		DynamoDBAstronomyItemWriter writer = new DynamoDBAstronomyItemWriter(astronomyService);
		return writer;
	}

	@Bean
	public AstronomyItemProcessor astronomyProcessor() {
		return new AstronomyItemProcessor();
	}

	@Bean
	public Job astronomyJob() {
		return jobBuilderFactory.get("importImariano13AstronomyJob").incrementer(new RunIdIncrementer())
				.listener(listener).flow(astronomyStep()).end().build();
	}

	@Bean
	public Step astronomyStep() {
		return stepBuilderFactory.get("importImariano13AstronomyStep").<WuAstronomy, MoonPhase>chunk(1)
				.reader(astronomyReader()).processor(astronomyProcessor()).writer(astronomyWriter()).build();
	}

	@Scheduled(cron = "0 0 1 * * ?")
	public void astronomyPerform() throws Exception {
		JobParameters param = new JobParametersBuilder()
				.addString("importImariano13AstronomyStepJobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		jobLauncher.run(astronomyJob(), param);
	}

}
