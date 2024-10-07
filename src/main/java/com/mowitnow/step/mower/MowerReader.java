package com.mowitnow.step.mower;

import com.mowitnow.exception.BatchFileException;
import com.mowitnow.exception.MowerParseException;
import com.mowitnow.model.Mower;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Component
@StepScope
public class MowerReader implements ItemReader<Mower> {
    private final BufferedReader reader;
    private boolean firstLineSkipped = false;

    public MowerReader(@Value("#{jobParameters['filePath']}") String filePath) throws BatchFileException {
        try {
            reader = new BufferedReader(new FileReader(filePath));
        } catch (IOException e) {
            throw new BatchFileException("Error opening file: " + filePath, e);
        }
    }

    @Override
    public Mower read() throws MowerParseException {
        try {
            // skip the first line only once
            if (!firstLineSkipped) {
                // skips the lawn dimensions
                reader.readLine();
                firstLineSkipped = true;
            }

            String positionLine = reader.readLine();
            if (positionLine == null) {
                // end of file
                return null;
            }

            String instructionsLine = reader.readLine();
            if (instructionsLine == null) {
                throw new MowerParseException("Missing instruction line for mower.");
            }

            String[] position = positionLine.split(" ");
            if (position.length != 3) {
                throw new MowerParseException("Invalid mower position format: " + positionLine);
            }

            int x = Integer.parseInt(position[0]);
            int y = Integer.parseInt(position[1]);
            char orientation = position[2].charAt(0);

            return new Mower(x, y, orientation, instructionsLine);
        } catch (IOException e) {
            throw new BatchFileException("Error reading file during mower parsing.", e);
        } catch (NumberFormatException e) {
            throw new MowerParseException("Invalid number format in mower position.", e);
        }
    }
}
