package com.mowitnow;

import com.mowitnow.utils.TestFileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
public class LawnMowerJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void shouldExecuteJobWithValidFiles() throws Exception {

        var jobExecution = jobLauncherTestUtils.launchJob(
                new JobParametersBuilder().addString("filePath", TestFileUtils.getFileAbsolutePath("input.txt"))
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters());

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
    }

    @ParameterizedTest
    @ValueSource(strings = {"empty.txt", "invalid_dimensions.txt", "invalid_instructions.txt"})
    public void shouldFailJobWithInvalidFiles(String filePath) throws Exception {
        var jobExecution = jobLauncherTestUtils.launchJob(
                new JobParametersBuilder().addString("filePath", TestFileUtils.getFileAbsolutePath(filePath))
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters());

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("FAILED");
    }

    @Test
    public void shouldFailJobWithUndefinedFilePath() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(
                new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters());

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("FAILED");
    }

}
