package io.github.gmodena.searchy.bsp;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Implement a binary space partitioning tree as a collection of nodes.
 * <p>
 * Leaf nodes contain a list of ids, by default indices in an array
 * of vectors.
 *
 * The implementation is light on defensive programming. References
 *  * to mutable objects are passed around and stored in the constructors. This is a potential source of bugs, but a tradeoff
 *  * to avoid unnecessary object creation and copying
 */
public class Node implements Serializable {
    private InnerNode inner;
    private LeafNode leaf;

    /**
     * Create a new node.
     *
     * @param inner
     */
    public Node(InnerNode inner, LeafNode leaf) {
        this.inner = inner;
        this.leaf = leaf;
    }

    /**
     * Create a new node.
     *
     * @param inner
     */
    public Node(InnerNode inner) {
        this.inner = inner;
        leaf = null;
    }

    /**
     * Create a new leaf node.
     *
     * @param leaf
     */
    public Node(LeafNode leaf) {
        this.leaf = leaf;
        this.inner = null;
    }

    public Node() {

    }

    /**
     * Get the inner node.
     *
     * @return
     */
    public InnerNode innerNode() {
        return inner;
    }

    /**
     * Get the leaf node.
     *
     * @return
     */
    public LeafNode leafNode() {
        return leaf;
    }

    public static class InnerNode extends Node {
        private final Hyperplane space;
        private final Node left;
        private final Node right;

        /**
         * Create a new inner node.
         *
         * @param space
         * @param left
         * @param right
         */
        public InnerNode(Hyperplane space, Node left, Node right) {
            super();
            this.space = space;
            this.left = left;
            this.right = right;
        }

        /**
         * Get the vector space of the hyperplane.
         *
         * @return
         */
        public Hyperplane vectorSpace() {
            return space;
        }

        /**
         * Get the left nodes.
         *
         * @return
         */
        public Node leftNode() {
            return left;
        }

        /**
         * Get the right nodes.
         *
         * @return
         */
        public Node rightNode() {
            return right;
        }
    }

    /**
     * A leaf node is a node that contains a list of ids.
     */
    public static class LeafNode extends Node {
        private final List<Integer> ids;

        /**
         * Create a new leaf node.
         *
         * @param ids
         */
        public LeafNode(List<Integer> ids) {
            super();
            this.ids = Collections.unmodifiableList(ids);
        }

        /**
         * Get the ids of the leaf node.
         *
         * @return
         */
        public List<Integer> getIds() {
            return ids;
        }
    }
}
