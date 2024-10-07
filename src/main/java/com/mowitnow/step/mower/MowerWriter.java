package com.mowitnow.step.mower;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.mowitnow.model.Mower;

/**
 * Writer for the mower result
 */
@Component
public class MowerWriter implements ItemWriter<Mower> {

    /**
     * Write the mowers to the output
     *
     * @param chunk the chunk of mowers to write
     * @throws Exception if an error occurs during the writing of the mowers
     */
    @Override
    public void write(Chunk<? extends Mower> chunk) throws Exception {
        chunk.getItems()
                .forEach(mower -> System.out.printf("%d %d %c%n", mower.getX(), mower.getY(), mower.getOrientation()));
    }
}
