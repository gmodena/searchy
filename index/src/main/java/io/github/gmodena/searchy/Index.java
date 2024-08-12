package io.github.gmodena.searchy;

import io.github.gmodena.searchy.bsp.JVector;
import io.github.gmodena.searchy.bsp.Node;
import io.github.gmodena.searchy.bsp.Plane;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;


/**
 * A id is a collection of trees. Each tree is a binary space partitioning tree.
 */
public class Index implements Serializable {
    private final Random random;
    private final Integer maxSize;
    private final Integer numTrees;
    private final boolean deduplicate;
    private List<JVector> vectors;
    private List<Integer> vectorIds;
    private ArrayList<Node> trees;

    private Index(Builder builder) {
        this.numTrees = builder.numTrees;
        this.maxSize = builder.maxSize;
        this.random = builder.random;
        this.deduplicate = builder.deduplicate;
        this.vectors = builder.vectors;
        this.vectorIds = builder.vectorIds;
        this.trees = builder.trees;

        this.buildIndex();
    }

    /**
     * Query the index with the given vectors and return the top k candidates.
     *
     * @param vectors
     * @param k
     * @return
     */
    public List<Candidate> query(List<float[]> vectors, Integer k) {
        var query = new Query(this, vectors, k);
        return query.search();
    }

    /**
     * Query the index with the given vector and return the top k candidates.
     *
     * @param vector
     * @param k
     * @return
     */
    public List<Candidate> query(float[] vector, Integer k) {
        var query = new Query(this, vector, k);
        return query.search();
    }

    /**
     * Save the index to the given file.
     *
     * @param fileName
     * @throws IOException
     */
    public void save(String fileName) throws IOException {
        IndexSerializer.serialize(this, fileName);
    }

    /**
     * Save the index to a byte array.
     *
     * @return
     * @throws IOException
     */
    public byte[] save() throws IOException {
        return IndexSerializer.serialize(this);
    }

    /**
     * Eagerly build a id of trees.
     * Trees hold an id into the unique vector list which is not
     * necessarily its id, if duplicates existed.
     * <p>
     * Iterate from 0..numTrees and build a tree.
     */
    private void buildIndex() {
        var space = new Plane(vectors, vectorIds);
        trees = (ArrayList<Node>) java.util.stream.IntStream.range(0, numTrees)
                .parallel()
                .mapToObj(i -> space.partition(maxSize))
                .collect(java.util.stream.Collectors.toList());
    }

    protected JVector getVector(Integer i) {
        return vectors.get(i);
    }

    /**
     * Get the trees in the id.
     */
    protected List<Node> getTrees() {
        return trees;
    }

    protected List<JVector> getVectors() {
        return vectors;
    }

    protected List<Integer> getVectorIds() {
        return vectorIds;
    }

    protected Integer maxSize() {
        return maxSize;
    }

    protected Integer numTrees() {
        return numTrees;
    }

    /**
     * Create a new builder.
     *
     * @return
     */
    public static class Builder {
        private final ArrayList<Node> trees = new ArrayList<>();
        private Random random = new Random();
        private Integer maxSize;
        private Integer numTrees;
        private boolean deduplicate = true;
        private List<JVector> vectors = new ArrayList<>();
        private List<Integer> vectorIds = new ArrayList<>();

        /**
         * @param maxSize
         * @return
         */
        public Builder setMaxSize(Integer maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        /**
         * @param numTrees
         * @return
         */
        public Builder setNumTrees(Integer numTrees) {
            this.numTrees = numTrees;
            return this;
        }

        /**
         * @param seed
         * @return
         */
        public Builder setRandomSeed(Long seed) {
            this.random = new Random(seed);
            return this;
        }

        /**
         * @param deduplicate
         * @return
         */
        public Builder deduplicate(boolean deduplicate) {
            this.deduplicate = deduplicate;
            return this;
        }

        /**
         * @param vectors
         * @param ids
         * @return
         */
        public Builder add(List<float[]> vectors, List<Integer> ids) {
            if (vectors.size() != ids.size()) {
                throw new IllegalArgumentException("Vectors and IDs must be the same size.");
            }
            for (float[] vector : vectors) {
                this.vectors.add(new JVector(vector));
            }
            this.vectorIds.addAll(ids);
            return this;
        }

        /**
         * @param vectors
         * @return
         */
        public Builder add(List<float[]> vectors) {
            for (float[] vector : vectors) {
                this.vectors.add(new JVector(vector));
            }
            for (var i = this.vectorIds.size(); i < vectors.size(); i++) {
                this.vectorIds.add(i);
            }
            return this;
        }

        /**
         * @param vector
         * @param id
         * @return
         */
        public Builder add(float[] vector, Integer id) {
            this.vectors.add(new JVector(vector));
            this.vectorIds.add(id);
            return this;
        }

        /**
         * @param vector
         * @return
         */
        public Builder add(float[] vector) {
            add(vector, this.vectorIds.size() + 1);
            return this;
        }

        /**
         * @return
         */
        public Index build() {
            if (deduplicate) {
                deduplicate();
            }
            return new Index(this);
        }

        private void deduplicate() {
            HashSet<Integer> visitedHashCodes = new HashSet<>();
            List<Integer> dedupIds = new ArrayList<>();
            for (int i = 0; i < vectors.size(); i++) {
                var vector = vectors.get(i);
                var hash = vector.hashCode();
                if (!visitedHashCodes.contains(hash)) {
                    visitedHashCodes.add(hash);
                    dedupIds.add(vectorIds.get(i));
                }
            }
            this.vectors = Collections.unmodifiableList(this.vectors);
            this.vectorIds = dedupIds;
        }
    }
}