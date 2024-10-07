package com.mowitnow.step.mower;

import com.mowitnow.model.Lawn;
import com.mowitnow.utils.TestFileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
public class MowerProcessingStepTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void shouldExecuteMowerProcessingStep() throws IOException {
        var jobExecution = jobLauncherTestUtils.launchStep("mowerProcessingStep",
                new JobParametersBuilder().addString("filePath", TestFileUtils.getFileAbsolutePath("input.txt"))
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters(),
                new ExecutionContext(Map.of("lawn", new Lawn(5, 5)))
        );

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
    }

    @Test
    public void shouldFailProcessingWithInvalidInstructions() throws IOException {
        var jobExecution = jobLauncherTestUtils.launchStep("mowerProcessingStep",
                new JobParametersBuilder().addString("filePath", TestFileUtils.getFileAbsolutePath("invalid_instructions.txt"))
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters(),
                new ExecutionContext(Map.of("lawn", new Lawn(5, 5)))
        );

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("FAILED");
    }
}
