package io.github.gmodena.searchy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IndexSerializerTest {
    private final static int numTrees = 3;
    private final static int maxSize = 15;
    private Index index;

    private ArrayList<float[]> vectors;
    private ArrayList<Integer> ids;

    @BeforeEach
    public void setUp() {
        vectors = new ArrayList<>();
        vectors.add(new float[]{1f, 2f, 3f});
        vectors.add(new float[]{4f, 5f, 6f});
        vectors.add(new float[]{7f, 8f, 9f});

        ids = new ArrayList<>();
        ids.add(1);
        ids.add(1);
        ids.add(3);

        index = new Index.Builder()
                .withNumTrees(numTrees)
                .withMaxNodeSize(maxSize)
                .add(vectors, ids)
                .build();
    }

    @Test
    public void testDummySerDe() throws IOException, ClassNotFoundException {
        byte[] serializedIndex = IndexSerializer.serialize(index);
        Index deserializedIndex = IndexSerializer.deserialize(serializedIndex);

        assertEquals((int) deserializedIndex.numTrees(), numTrees);
        assertEquals((int) deserializedIndex.maxNodeSIze(), maxSize);
        // assertTrue(deserializedIndex.getVectorIds() == null);
    }
}
