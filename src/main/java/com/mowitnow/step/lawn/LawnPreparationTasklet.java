package com.mowitnow.step.lawn;

import com.mowitnow.exception.BatchFileException;
import com.mowitnow.exception.LawnParseException;
import com.mowitnow.model.Lawn;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Component
@StepScope
public class LawnPreparationTasklet implements Tasklet {

    private final String filePath;

    public LawnPreparationTasklet(@Value("#{jobParameters['filePath']}") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws BatchFileException, LawnParseException {
        if (filePath == null) {
            throw new BatchFileException("filePath is mandatory");
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String firstLine = bufferedReader.readLine();
            if (firstLine == null) {
                throw new LawnParseException("Input file is empty");
            }
            String[] lawnDimensions = firstLine.split(" ");
            if (lawnDimensions.length != 2) {
                throw new LawnParseException("Invalid lawn dimensions");
            }
            int width = Integer.parseInt(lawnDimensions[0]);
            int height = Integer.parseInt(lawnDimensions[1]);
            chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("lawn", new Lawn(width, height));
        } catch (IOException e) {
            throw new BatchFileException("Error reading file", e);
        } catch (NumberFormatException e) {
            throw new LawnParseException("Invalid number format in lawn dimensions.", e);
        }
        return RepeatStatus.FINISHED;
    }
}
