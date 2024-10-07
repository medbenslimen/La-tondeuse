package com.mowitnow.step.mower;

import com.mowitnow.model.Instruction;
import com.mowitnow.model.Lawn;
import com.mowitnow.model.Mower;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Processor for the mower
 */
@Component
@StepScope
public class MowerProcessor implements ItemProcessor<Mower, Mower> {
    private static final String INVALID_ORIENTATION_MESSAGE = "Invalid orientation: ";

    private final Lawn lawn;

    public MowerProcessor(@Value("#{stepExecution.jobExecution}") JobExecution jobExecution) {
        this.lawn = (Lawn) jobExecution.getExecutionContext().get("lawn");
    }

    /**
     * Process the mower
     *
     * @param mower the mower to process
     * @return the processed mower
     */
    @Override
    public Mower process(Mower mower) {
        return mower.getInstructions().chars()
                .mapToObj(ch -> Instruction.fromCode((char) ch))
                .reduce(mower, this::executeInstruction, (m1, m2) -> m2);
    }

    /**
     * Execute a single instruction on the mower
     *
     * @param mower       the mower to execute the instruction on
     * @param instruction the instruction to execute
     * @return the mower after the instruction has been executed
     */
    private Mower executeInstruction(Mower mower, Instruction instruction) {
        return switch (instruction) {
            case MOVE -> move(mower);
            case TURN_LEFT -> turnLeft(mower);
            case TURN_RIGHT -> turnRight(mower);
        };
    }

    /**
     * Move the mower forward
     *
     * @param mower the mower to move
     * @return the mower after the move
     */
    private Mower move(Mower mower) {
        // Calculate the new position of the mower
        int[] newPosition = switch (mower.getOrientation()) {
            case 'N' -> new int[]{mower.getX(), mower.getY() + 1};
            case 'E' -> new int[]{mower.getX() + 1, mower.getY()};
            case 'S' -> new int[]{mower.getX(), mower.getY() - 1};
            case 'W' -> new int[]{mower.getX() - 1, mower.getY()};
            default -> throw new IllegalStateException(INVALID_ORIENTATION_MESSAGE + mower.getOrientation());
        };

        // Check if the new position is valid
        return isValidPosition(newPosition[0], newPosition[1]) ? mower.withPosition(newPosition[0], newPosition[1])
                : mower;
    }

    /**
     * Turn the mower left
     *
     * @param mower the mower to turn
     * @return the mower after the turn
     */
    private Mower turnLeft(Mower mower) {
        return mower.withOrientation(switch (mower.getOrientation()) {
            case 'N' -> 'W';
            case 'W' -> 'S';
            case 'S' -> 'E';
            case 'E' -> 'N';
            default -> throw new IllegalStateException(INVALID_ORIENTATION_MESSAGE + mower.getOrientation());
        });
    }

    /**
     * Turn the mower right
     *
     * @param mower the mower to turn
     * @return the mower after the turn
     */
    private Mower turnRight(Mower mower) {
        return mower.withOrientation(switch (mower.getOrientation()) {
            case 'N' -> 'E';
            case 'E' -> 'S';
            case 'S' -> 'W';
            case 'W' -> 'N';
            default -> throw new IllegalStateException(INVALID_ORIENTATION_MESSAGE + mower.getOrientation());
        });
    }

    /**
     * Check if the position is valid
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if the position is valid, false otherwise
     */
    private boolean isValidPosition(int x, int y) {
        // check if the position is within the lawn bounds
        return lawn != null && x >= 0 && x <= lawn.width() && y >= 0 && y <= lawn.height();
    }
}
