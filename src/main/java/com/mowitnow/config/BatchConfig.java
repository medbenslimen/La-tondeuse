package com.mowitnow.config;

import com.mowitnow.exception.BatchFileException;
import com.mowitnow.model.Mower;
import com.mowitnow.step.lawn.LawnPreparationTasklet;
import com.mowitnow.step.mower.MowerReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    @Bean
    public Job lawnMowerJob(JobRepository jobRepository, Step lawnPreparationStep, Step mowerProcessingStep) {
        return new JobBuilder("lawnMowerJob", jobRepository)
                .start(lawnPreparationStep)
                .next(mowerProcessingStep)
                .build();
    }

    @Bean
    public Step lawnPreparationStep(JobRepository jobRepository,
                                    PlatformTransactionManager transactionManager,
                                    LawnPreparationTasklet lawnPreparationTasklet) throws BatchFileException {
        return new StepBuilder("lawnPreparationStep", jobRepository)
                .tasklet(lawnPreparationTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step mowerProcessingStep(JobRepository jobRepository,
                                    PlatformTransactionManager transactionManager,
                                    MowerReader mowerReader,
                                    ItemProcessor<Mower, Mower> processor,
                                    ItemWriter<Mower> writer) {
        return new StepBuilder("mowerProcessingStep", jobRepository)
                .<Mower, Mower>chunk(1, transactionManager)
                .reader(mowerReader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
