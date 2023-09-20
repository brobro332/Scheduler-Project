package kr.co.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing // BaseEntity 생성으로 인한 Auditing 설정
@SpringBootApplication
@EnableScheduling
public class SchedulerApplication {

	public static void main(String[] args) {

		SpringApplication.run(SchedulerApplication.class, args);
	}
}
