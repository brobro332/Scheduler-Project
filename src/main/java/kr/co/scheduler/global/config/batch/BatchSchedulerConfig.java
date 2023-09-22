package kr.co.scheduler.global.config.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;

@Configuration
@EnableScheduling
public class BatchSchedulerConfig {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job processNotLoggedUsersJob;

    /**
     * runBatchJob: 스프링 배치에 스케줄러 적용
     * 1. 로그인한지 10일 지난 사용자에게 안내 메일 전달
     * 2. 로그인한지 30일 지난 사용자의 모든 게시물 삭제(이미지 파일을 지우기 위함) 및 유저 탈퇴 처리 
     */
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void runBatchJob() throws JobExecutionException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDate("jobStartTime", LocalDate.now())
                .toJobParameters();

        jobLauncher.run(processNotLoggedUsersJob, jobParameters);
    }
}
