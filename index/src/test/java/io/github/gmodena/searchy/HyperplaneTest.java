package io.github.gmodena.searchy;

import io.github.gmodena.searchy.bsp.Hyperplane;
import io.github.gmodena.searchy.bsp.JVector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HyperplaneTest {

    @Test
    void testIsAbove() {
        float[] coeffsArray = {1.0f, 2.0f, 3.0f};
        JVector coeffs = new JVector(coeffsArray);
        float epsilon = 0.0001f;
        Hyperplane hyperplane = new Hyperplane(coeffs, epsilon);

        // Test when the dot product + epsilon is greater than or equal to 0
        float[] vectorArray1 = {1.0f, 1.0f, 1.0f};
        JVector vector1 = new JVector(vectorArray1);
        assertTrue(hyperplane.isAbove(vector1));

        // Test when the dot product + epsilon is less than 0
        float[] vectorArray2 = {-1.0f, -1.0f, -1.0f};
        JVector vector2 = new JVector(vectorArray2);
        assertFalse(hyperplane.isAbove(vector2));
    }
}