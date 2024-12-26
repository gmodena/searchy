package io.github.gmodena.searchy.bsp;

import java.util.*;

/**
 * A binary space partitioning tree for vector spaces.
 * The implementation is light on defensive programming. References
 * to mutable objects are passed around and stored in the constructors. This is a potential source of bugs, but a tradeoff
 * to avoid unnecessary object creation and copying.
 */
public final class Plane {
    public final static long MAX_SIZE = 15;
    private final List<JVector> vectors;
    private final List<Integer> ids;
    private final Random random;

    /**
     * Creates a new plane with the given vectors and vector IDs, using a new Random instance.
     *
     * @param vectors The list of vectors to initialize the plane with
     * @param ids The list of IDs corresponding to each vector
     * @throws NullPointerException if either vectors or ids is null
     * @throws IllegalArgumentException if vectors and ids have different sizes
     */
    public Plane(List<JVector> vectors, List<Integer> ids) {
        this(vectors, ids, new Random());
    }

    /**
     * Creates a new plane with the given vectors, vector IDs, and random number generator.
     *
     * @param vectors The list of vectors to initialize the plane with
     * @param ids The list of IDs corresponding to each vector
     * @param random The random number generator to use
     * @throws NullPointerException if any parameter is null
     * @throws IllegalArgumentException if vectors and ids have different sizes
     */
    public Plane(List<JVector> vectors, List<Integer> ids, Random random) {
        Objects.requireNonNull(vectors, "vectors must not be null");
        Objects.requireNonNull(ids, "ids must not be null");
        Objects.requireNonNull(random, "random must not be null");

        this.random = Random.from(random);
        this.vectors = List.copyOf(vectors);
        this.ids = List.copyOf(ids);
    }

    /**
     * Partition the vectors into a binary space partitioning tree.
     *
     * @return
     */
    public Node partition() {
        return partition(MAX_SIZE);
    }

    /**
     * Partition the vectors into a binary space partitioning tree with the given maximum size.
     *
     * @param maxSize
     * @return
     */
    public Node partition(long maxSize) {
        return partition(maxSize, ids);
    }

    private Node partition(long maxSize, List<Integer> vectorIds) {
        if (vectorIds.size() <= maxSize) {
            return new Node.LeafNode(vectorIds);
        }
        var hyperplane = binaryPartition(vectors, vectorIds);

        var leftNode = partition(maxSize, hyperplane.below());
        var rightNode = partition(maxSize, hyperplane.above());

        return new Node.InnerNode(hyperplane.plane(), leftNode, rightNode);
    }

    /**
     * Generates a random hyperplane in a vector space
     * and uses it to partition a set of vectors into two groups: those that are
     * above the hyperplane and those that are below it.
     * <p>
     * The result is encapsulated in a Partition object, which is
     * a record representing a partition of a vector space.
     *
     * @param ids
     * @return
     */
    private Partition binaryPartition(List<JVector> vectors, List<Integer> ids) {
        List<Integer> sample = new ArrayList<>();

        sample.add(ids.get(random.nextInt(ids.size())));
        sample.add(ids.get(random.nextInt(ids.size())));

        var a = vectors.get(sample.get(0));
        var b = vectors.get(sample.get(1));

        var coefficients = a.sub(b);
        var pointOnPlane = a.mean(b);
        var constant = -coefficients.dot(pointOnPlane);

        var hyperplane = new Hyperplane(coefficients, constant);

        List<Integer> above = new ArrayList<>();
        List<Integer> below = new ArrayList<>();

        for (int id : ids) {
            if (hyperplane.isAbove(vectors.get(id))) {
                above.add(id);
            } else {
                below.add(id);
            }
        }
        return new Partition(hyperplane, above, below);
    }
}
