package io.github.gmodena.searchy.bsp;

import java.io.Serializable;
import java.util.List;

/**
 * A hyperplane is a subspace of one dimension less than the space it divides.
 *
 * @param plane hyperplane boundary
 * @param above ids of nodes above the hyperplane boundary
 * @param below ids of nodes below the hyperplane boundary
 */
record Partition(Hyperplane plane, List<Integer> above, List<Integer> below) {
}

/**
 * A hyperplane is a subspace of one dimension less than the space it divides.
 */
public class Hyperplane implements Serializable {
    public JVector coeffs;
    public Float epsilon;

    /**
     * Create a new hyperplane with the given coefficients and epsilon.
     *
     * @param coeffs  coefficients
     * @param epsilon classification tolerance threshold
     */
    public Hyperplane(JVector coeffs, float epsilon) {
        this.coeffs = coeffs;
        this.epsilon = epsilon;
    }

    /**
     * Check if the given vector is above the hyperplane.
     *
     * @param vector
     * @return
     */
    public Boolean isAbove(JVector vector) {
        return (coeffs.dot(vector) + epsilon) >= 0.0;
    }
}