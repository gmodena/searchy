package io.github.gmodena.searchy;

import io.github.gmodena.searchy.bsp.JVector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class VectorTest {

    @Test
    void testSub() {
        float[] points1 = {1.0f, 2.0f, 3.0f};
        float[] points2 = {0.5f, 1.0f, 1.5f};
        JVector vector1 = new JVector(points1);
        JVector vector2 = new JVector(points2);

        JVector result = vector1.sub(vector2);

        float[] expected = {0.5f, 1.0f, 1.5f};
        JVector expectedResult = new JVector(expected);

        assertArrayEquals(expectedResult.raw(), result.raw());
    }

    @Test
    void testMean() {
        float[] points1 = {1.0f, 2.0f, 3.0f};
        float[] points2 = {0.5f, 1.0f, 1.5f};
        JVector vector1 = new JVector(points1);
        JVector vector2 = new JVector(points2);

        JVector result = vector1.mean(vector2);

        float[] expected = {0.75f, 1.5f, 2.25f};
        JVector expectedResult = new JVector(expected);

        assertArrayEquals(expectedResult.raw(), result.raw());
    }

    @Test
    void testDot() {
        float[] points1 = {1.0f, 2.0f, 3.0f};
        float[] points2 = {0.5f, 1.0f, 1.5f};
        JVector vector1 = new JVector(points1);
        JVector vector2 = new JVector(points2);

        float result = vector1.dot(vector2);

        float expected = 0.5f + 2.0f + 3.0f * 1.5f;

        assertEquals(expected, result);
    }

    @Test
    void testDistance() {
        float[] points1 = {1.0f, 2.0f, 3.0f};
        float[] points2 = {0.5f, 1.0f, 1.5f};
        JVector vector1 = new JVector(points1);
        JVector vector2 = new JVector(points2);

        float result = vector1.distance(vector2);

        float expected = (float) Math.sqrt(Math.pow(0.5f - 1.0f, 2) + Math.pow(1.0f - 2.0f, 2) + Math.pow(1.5f - 3.0f, 2));

        assertEquals(expected, result, 1e-6);
    }
}
