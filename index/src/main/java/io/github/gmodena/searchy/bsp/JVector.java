package io.github.gmodena.searchy.bsp;


import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.IntStream;

public class JVector implements IVector<JVector>, Serializable {
    public int length = 0;
    private final float[] points;

    /**
     * Create a new vector with the given points.
     *
     * @param points
     */
    public JVector(float[] points) {
        // TODO: need to benchmark the tradeoff between copying the array and using a reference.
        this.points = Arrays.copyOf(points, points.length);
        this.length = points.length;
    }

    /**
     * Subtract vector that vector from this vector.
     */
    public JVector sub(JVector that) {
        if (this.length != that.length) {
            return null;
        }
        float[] res = new float[length];
        for (var i = 0; i < length; i++) {
            res[i] = this.points[i] - that.points[i];
        }
        return new JVector(res);
    }

    /**
     * Mean of two vectors
     *
     * @param that
     * @return
     */
    public JVector mean(JVector that) {
        if (this.length != that.length) {
            return null;
        }
        float[] res = new float[length];
        for (var i = 0; i < length; i++) {
            res[i] = (this.points[i] + that.points[i]) / 2.0f;
        }
        return new JVector(res);
    }

    /**
     * Dot product of two vectors
     *
     * @param that
     * @return
     */
    public float dot(JVector that) {
        return (float) IntStream.range(0, length)
                .mapToDouble(i -> this.points[i] * that.points[i])
                .reduce(0.0f, (acc, val) -> acc + (float) val);
    }


    /**
     * Distance between two vectors
     *
     * @param that
     * @return
     */
    public float distance(JVector that) {
        var sum = 0.0f;
        for (var i = 0; i < length; i++) {
            var c = this.points[i] - that.points[i];
            sum += c * c;
        }
        return sum;
    }

    /**
     * Raw points of the vector
     * TODO: measure the overhead of returning a copy instead of a reference.
     *
     * @return
     */
    public float[] raw() {
        return java.util.Arrays.copyOf(points, points.length);
    }

    /**
     * Hash code of the vector
     *
     * @return
     */
    @Override
    public int hashCode() {
        return java.util.Arrays.hashCode(points);
    }
}