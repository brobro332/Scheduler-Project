package kr.co.scheduler;

import kr.co.scheduler.global.config.batch.BatchJob;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableBatchProcessing
@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class SchedulerApplication {

	public static void main(String[] args) {

		SpringApplication.run(SchedulerApplication.class, args);
	}
}
