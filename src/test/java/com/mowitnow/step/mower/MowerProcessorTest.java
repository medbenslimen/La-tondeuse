package com.mowitnow.step.mower;

import com.mowitnow.model.Lawn;
import com.mowitnow.model.Mower;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ExecutionContext;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MowerProcessorTest {
    @Mock
    private JobExecution jobExecution;

    private MowerProcessor mowerProcessor;

    @BeforeEach
    void setUp() {
        given(jobExecution.getExecutionContext()).willReturn(new ExecutionContext(Map.of("lawn", new Lawn(5, 5))));
        mowerProcessor = new MowerProcessor(jobExecution);
    }

    @Test
    void shouldProcessMowerWithValidInstructions() {
        Mower mower = new Mower(1, 2, 'N', "GAGAGAGAA");
        Mower result = mowerProcessor.process(mower);
        assertEquals(1, result.getX());
        assertEquals(3, result.getY());
        assertEquals('N', result.getOrientation());
    }

    @Test
    void shouldFailWithInvalidInstructions() {
        Mower mower = new Mower(1, 2, 'N', "GAGAGAGAX");
        assertThrows(IllegalArgumentException.class, () -> mowerProcessor.process(mower));
    }

    @Test
    void shouldMoveMowerWithinBounds() {
        Mower mower = new Mower(1, 2, 'N', "A");
        Mower result = mowerProcessor.process(mower);
        assertEquals(1, result.getX());
        assertEquals(3, result.getY());
    }

    @Test
    void shouldNotMoveMowerOutOfBounds() {
        Mower mower = new Mower(0, 0, 'S', "A");
        Mower result = mowerProcessor.process(mower);
        assertEquals(0, result.getX());
        assertEquals(0, result.getY());
    }

    @Test
    void shouldTurnMowerLeft() {
        Mower mower = new Mower(1, 2, 'N', "G");
        Mower result = mowerProcessor.process(mower);
        assertEquals('W', result.getOrientation());
    }

    @Test
    void shouldTurnMowerRight() {
        Mower mower = new Mower(1, 2, 'N', "D"); // Updated instruction
        Mower result = mowerProcessor.process(mower);
        assertEquals('E', result.getOrientation());
    }
}
