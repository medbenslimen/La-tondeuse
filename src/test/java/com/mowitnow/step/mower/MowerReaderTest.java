package com.mowitnow.step.mower;

import com.mowitnow.exception.BatchFileException;
import com.mowitnow.exception.MowerParseException;
import com.mowitnow.model.Mower;
import com.mowitnow.utils.TestFileUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MowerReaderTest {

    @Test
    public void shouldReadValidMowerData() throws Exception {
        String filePath = TestFileUtils.getFileAbsolutePath("input.txt");
        MowerReader reader = new MowerReader(filePath);

        Mower mower1 = reader.read();
        assertThat(mower1).isNotNull();
        assertThat(mower1.getX()).isEqualTo(1);
        assertThat(mower1.getY()).isEqualTo(2);
        assertThat(mower1.getOrientation()).isEqualTo('N');
        assertThat(mower1.getInstructions()).isEqualTo("GAGAGAGAA");

        Mower mower2 = reader.read();
        assertThat(mower2).isNotNull();
        assertThat(mower2.getX()).isEqualTo(3);
        assertThat(mower2.getY()).isEqualTo(3);
        assertThat(mower2.getOrientation()).isEqualTo('E');
        assertThat(mower2.getInstructions()).isEqualTo("AADAADADDA");

        assertThat(reader.read()).isNull(); // End of file
    }

    @Test
    public void shouldThrowExceptionWithInvalidInstruction() throws Exception {
        String filePath = TestFileUtils.getFileAbsolutePath("invalid_instructions.txt");
        MowerReader reader = new MowerReader(filePath);

        assertThrows(MowerParseException.class, reader::read);
    }

    @Test
    public void shouldThrowBatchFileExceptionWhenFileNotFound() {
        assertThrows(BatchFileException.class, () -> new MowerReader("non_existent_file.txt"));
    }
}
