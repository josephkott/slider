package com.lyadov.slider;

import com.lyadov.slider.model.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PositionTest {
    @Test
    public void testPositionStringRepresentation() {
        Position position = new Position(1, 2);
        Assertions.assertEquals("1 / 2", position.toString());
    }
}
