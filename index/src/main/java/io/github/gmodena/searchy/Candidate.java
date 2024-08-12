package io.github.gmodena.searchy;

import java.util.Arrays;
import java.util.Collections;

/**
 * A result search candidate is a vector with an id and a distance from a query vector.
 *
 * @param vector
 * @param id
 * @param distance
 */

public record Candidate(float[] vector, Integer id, float distance) implements Comparable {
    @Override
    public int compareTo(Object o) {
        return -1 * Float.compare(distance, ((Candidate) o).distance);
    }

    /**
     * Candidate records should be immutable data carrying types.
     * Implement explicit defensive copying to enforce immutability.
     *
     * TODO: benchmark the tradeoff between copying the array and using a reference.
     *
     * @param vector
     * @param id
     * @param distance
     */
    public Candidate(float[] vector, Integer id, float distance) {
        this.vector = Arrays.copyOf(vector, vector.length);
        this.id = id;
        this.distance = distance;
    }

    /**
     * Get the vector.
     *
     * TODO: benchmark the tradeoff between copying the array and using a reference.
     * @return
     */
    public float[] vector() {
        return Arrays.copyOf(vector, vector.length);
    }
}
