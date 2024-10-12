package io.github.gmodena.searchy;

import io.github.gmodena.searchy.bsp.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class IndexTest {
    private static final int numTrees = 3;
    private static final int maxSize = 10;

    private ArrayList<float[]> vectors;
    private ArrayList<Integer> ids;

    @BeforeEach
    void setUp() {
        vectors = new ArrayList<>();
        vectors.add(new float[]{1f, 2f, 3f});
        vectors.add(new float[]{4f, 5f, 6f});
        vectors.add(new float[]{7f, 8f, 9f});

        ids = new ArrayList<>();
        ids.add(1);
        ids.add(1);
        ids.add(3);
    }

    private void assertTreeIsValid(Node tree) {
        if (tree instanceof Node.InnerNode innerNode) {
            assertNotNull(innerNode.vectorSpace());
            assertNotNull(innerNode.leftNode());
            assertNotNull(innerNode.rightNode());

            assertTreeIsValid(innerNode.leftNode());
            assertTreeIsValid(innerNode.rightNode());
        } else if (tree instanceof Node.LeafNode leafNode) {
            assertNotNull(leafNode.getIds());
            assertTrue(leafNode.getIds().size() <= maxSize);
        } else {
            fail("Unexpected node type");
        }
    }

    @Test
    void buildVectorSpace_shouldPartitionVectorsCorrectly() {
        Index index = new Index.Builder()
                .withNumTrees(numTrees)
                .withMaxNodeSize(maxSize)
                .add(vectors, ids)
                .build();

        assertNotNull(index.getTrees());

        for (Node tree : index.getTrees()) {
            assertTreeIsValid(tree);
        }
    }

    @Test
    void deduplicate_shouldRemoveDuplicates() {
        float[] duplicateVector = new float[]{1f, 2f, 3f};
        Integer duplicateVectorId = 4; // duplicates id 1
        vectors.add(duplicateVector);
        ids.add(duplicateVectorId);

        Index index = new Index.Builder()
                .withNumTrees(1)
                .withMaxNodeSize(10)
                .add(vectors, ids)
                .build();


        Integer expectedVectorsSize = 4;
        Integer expectedIndexSize = 3;

        // Discard duplicate vector ID.
        assertEquals(expectedIndexSize, index.getVectorIds().size());
        assertFalse(index.getVectorIds().contains(duplicateVectorId));

        // But keep the vector in the dataset.
        assertEquals(expectedVectorsSize, index.getVectors().size());
    }
}
