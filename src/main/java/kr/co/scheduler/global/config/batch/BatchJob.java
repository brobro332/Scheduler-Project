package kr.co.scheduler.global.config.batch;

import kr.co.scheduler.community.entity.Post;
import kr.co.scheduler.community.service.PostService;
import kr.co.scheduler.global.config.mail.AlertFiredMail;
import kr.co.scheduler.global.config.mail.AlertInactiveMail;
import kr.co.scheduler.scheduler.entity.Project;
import kr.co.scheduler.scheduler.service.ProjectService;
import kr.co.scheduler.user.entity.User;
import kr.co.scheduler.user.repository.UserRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Configuration
public class BatchJob{

    @Autowired
    private PostService postService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlertInactiveMail alertInactiveMail;

    @Autowired
    private AlertFiredMail alertFiredMail;

    /**
     * processNotLoggedUsersJob: 미접속 사용자에 대한 데이터를 처리하는 배치
     * Step 1. 10일간 미접속 사용자는 안내성 메일을 전송
     * Step 2. 30일간 미접속 사용자는 모든 게시글을 삭제(이미지 파일 제거를 위함) 및 회원 탈퇴 처리
     */
    @Bean
    public Job processNotLoggedUsersJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {

        return new JobBuilder("processNotLoggedUsersJob", jobRepository)
                .start(processNotLoggedFor10DaysUsersStep(jobRepository, platformTransactionManager))
                .next(processNotLoggedFor30DaysUsersStep(jobRepository, platformTransactionManager))
                .build();
    }

    // ================================== 구분 ================================== //

    @Bean
    public Step processNotLoggedFor10DaysUsersStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        LocalDate daysAgo = LocalDate.now().minusDays(15);

        return new StepBuilder("processNotLoggedFor10DaysUsersStep", jobRepository)
                .<User, User>chunk(10, transactionManager)
                .reader(notLoggedUsersFor10DaysItemReader(daysAgo))
                .processor(notLoggedUsersFor10DaysItemProcessor())
                .faultTolerant() // Skip 기능 활성화
                .skip(Exception.class)
                .writer(noOpItemWriter())
                .build();
    }

    @Bean
    public ItemReader<User> notLoggedUsersFor10DaysItemReader(LocalDate daysAgo) {

        return new RepositoryItemReaderBuilder<User>()
                .repository(userRepository)
                .methodName("findByLastLoggedDay")
                .arguments(daysAgo)
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .name("notLoggedUsersItemReader")
                .build();
    }

    @Bean
    public ItemProcessor<User, User> notLoggedUsersFor10DaysItemProcessor() {
        return user -> {

            try {
                alertInactiveMail.sendMessage(user.getEmail());

                return null;
            } catch (Exception e) {

                throw new IllegalArgumentException("수신 이메일 주소가 존재하지 않거나 휴면상태입니다.");
            }
        };
    }

    @Bean
    public ItemWriter<User> noOpItemWriter() {

        return new ListItemWriter<>();
    }

    // ================================== 구분 ================================== //

    @Bean
    public Step processNotLoggedFor30DaysUsersStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        LocalDate daysAgo = LocalDate.now().minusDays(30);

        return new StepBuilder("processNotLoggedFor30DaysUsersStep", jobRepository)
                .<User, User>chunk(10, transactionManager)
                .reader(notLoggedUsersFor30DaysItemReader(daysAgo))
                .processor(notLoggedUsersFor30DaysItemProcessor())
                .faultTolerant() // Skip 기능 활성화
                .skip(Exception.class)
                .writer(notLoggedUsersFor30DaysItemWriter())
                .build();
    }

    @Bean
    public ItemReader<User> notLoggedUsersFor30DaysItemReader(LocalDate daysAgo) {

        return new RepositoryItemReaderBuilder<User>()
                .repository(userRepository)
                .methodName("findByLastLoggedDay")
                .arguments(daysAgo)
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .name("notLoggedUsersItemReader")
                .build();
    }

    @Bean
    public ItemProcessor<User, User> notLoggedUsersFor30DaysItemProcessor() {

        return user -> {

            try {
                alertFiredMail.sendMessage(user.getEmail());

                return user;
            } catch (Exception e) {

                throw new IllegalArgumentException("수신 이메일 주소가 존재하지 않거나 휴면상태입니다.");
            }
        };
    }

    @Bean
    public ItemWriter<User> notLoggedUsersFor30DaysItemWriter() {

        return users -> {

            for (User user : users) {

                List<Post> posts = user.getPosts();
                String email = user.getEmail();
                List<Project> projects = user.getProjects();

                for (Post post : posts) {

                    postService.deletePost(post, email);
                }

                for (Project project : projects) {

                    projectService.deleteProject(project.getId(), email);
                }

                userRepository.delete(user);
            }
        };
    }
}
