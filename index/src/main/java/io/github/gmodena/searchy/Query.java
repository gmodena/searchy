package io.github.gmodena.searchy;

import io.github.gmodena.searchy.bsp.JVector;
import io.github.gmodena.searchy.bsp.Node;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Query the id for the top k nearest neighbors.
 */
public class Query {
    private final JVector[] queries;

    private final Integer topK;

    private final ConcurrentHashMap.KeySetView<Object, Boolean> candidates;
    private final Index index;

    /**
     * Query the id for the top k nearest neighbors.
     *
     * @param index the id to query
     * @param query the query vector
     * @param topK  the number of nearest neighbors to return
     */
    public Query(Index index, float[] query, Integer topK) {
        this.index = index;
        this.queries = new JVector[]{new JVector(query)};
        this.topK = topK;
        this.candidates = ConcurrentHashMap.newKeySet();
    }

    /**
     * Query the id for the top k nearest neighbors.
     *
     * @param index   the id to query
     * @param queries the query vectors
     * @param topK    the number of nearest neighbors to return
     */
    public Query(Index index, List<float[]> queries, Integer topK) {
        this.index = index;
        this.queries = queries.stream()
                .map(JVector::new)
                .toArray(JVector[]::new);
        this.topK = topK;
        this.candidates = ConcurrentHashMap.newKeySet();
    }

    /**
     * Search the id for the top k nearest neighbors.
     * Results are approximate.
     */
    public List<Candidate> search() {
        // neighbours will contain at most topk+1 elements before polling.
        PriorityQueue<Candidate> neighbours = new PriorityQueue<>(topK + 1, Comparator.comparingDouble(Candidate::distance).reversed());

        Arrays.stream(queries).parallel().forEach(query -> {
            index.getTrees().forEach(tree -> searchTree((Node) tree, query, topK));

            candidates.stream()
                    .map(idx -> {
                        JVector vector = index.getVector((Integer) idx);
                        return new Candidate(vector.raw(), (Integer) idx, vector.distance(query));
                    })
                    .forEach(candidate -> {
                        synchronized (neighbours) {
                            if (neighbours.size() < topK || candidate.distance() < neighbours.peek().distance()) {
                                neighbours.offer(candidate);
                                if (neighbours.size() > topK) {
                                    neighbours.poll();
                                }
                            }
                        }
                    });
        });

        return new ArrayList<>(neighbours);
    }

    private int searchTree(Node tree, JVector query, int n) {
        // we reached a leaf node.
        // Take all elements in the leaf node and add them to candidates.
        if (tree instanceof Node.LeafNode leaf) {
            var leafValues = leaf.getIds();
            int numCandidatesFound = Math.min(n, leafValues.size());

            for (int i = 0; i < numCandidatesFound; i++) {
                candidates.add(leafValues.get(i));
            }

            return numCandidatesFound;
        } else {
            // We are at an inner node.
            // Search recursively the left and right nodes. Backtrack if
            // the candidates pool size is smaller than k.
            var inner = (Node.InnerNode) tree;
            var above = inner.vectorSpace().isAbove(query);
            Node main;
            Node backup;
            if (above) {
                main = inner.rightNode();
                backup = inner.leftNode();
            } else {
                main = inner.leftNode();
                backup = inner.rightNode();
            }
            var k = searchTree(main, query, n);
            if (k < n) {
                return k + searchTree(backup, query, n - k);
            }
            return k;
        }
    }
}