package com.mowitnow.step.lawn;

import com.mowitnow.model.Lawn;
import com.mowitnow.utils.TestFileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@SpringBatchTest
public class LawnPreparationStepTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void shouldPrepareLawn() throws IOException {
        var jobExecution = jobLauncherTestUtils.launchStep("lawnPreparationStep",
                new JobParametersBuilder().addString("filePath", TestFileUtils.getFileAbsolutePath("input.txt"))
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters());

        assertThat(jobExecution.getExecutionContext().get("lawn")).isEqualTo(new Lawn(5, 5));
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
    }

    @ParameterizedTest
    @ValueSource(strings = {"empty.txt", "invalid_dimensions.txt"})
    void shouldFailWithInvalidFile(String filePath) throws IOException {
        var jobExecution = jobLauncherTestUtils.launchStep("lawnPreparationStep",
                new JobParametersBuilder().addString("filePath", TestFileUtils.getFileAbsolutePath(filePath))
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters());

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("FAILED");
    }

}
