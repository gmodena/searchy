package io.github.gmodena.searchy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryTest {
    private ArrayList<float[]> vectors;
    private ArrayList<Integer> ids;
    private Index index;
    private final float[] queryVector = new float[]{6.5f, 8f, 9f};
    private final List<float[]> queriesVector = List.of(queryVector, queryVector);

    @BeforeEach
    void init() {
        vectors = new ArrayList<>();
        vectors.add(new float[]{1f, 2f, 3f});
        vectors.add(new float[]{4f, 5f, 6f});
        vectors.add(new float[]{7f, 8f, 9f});
        vectors.add(new float[]{10f, 11f, 12f});
        vectors.add(new float[]{13f, 14f, 15f});
        vectors.add(new float[]{16f, 17f, 18f});
        vectors.add(new float[]{19f, 20f, 21f});
        vectors.add(new float[]{22f, 23f, 24f});
        vectors.add(new float[]{25f, 26f, 27f});
        vectors.add(new float[]{28f, 29f, 30f});


        ids = new ArrayList<>();


        for (int i = 1; i <= vectors.size(); i++) {
            ids.add(i);
        }

        assertEquals(vectors.size(), ids.size());

        index = new Index.Builder()
                .withNumTrees(3)
                .withMaxNodeSize(10)
                .add(vectors, ids)
                .build();
    }

    @Test
    void testSearch() {
        var topK = 3;
        Query q = new Query(index, queryVector, topK);
        var result = q.search();
        assertEquals(topK, result.size());
    }

    @Test
    void testMultiQuerySearch() {
        var topK = 2;
        Query q = new Query(index, queriesVector, topK);
        var result = q.search();
        assertEquals(topK, result.size());
    }

    @Test
    public void testThreadSafety() throws Exception {
        int numThreads = 2;
        int topK = 1;

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        List<Future<List<Candidate>>> futures = IntStream.range(0, numThreads)
                .mapToObj(i -> executorService.submit(() -> {
                    Query query = new Query(index, queryVector, topK);
                    return query.search();
                }))
                .collect(Collectors.toList());

        for (Future<List<Candidate>> future : futures) {
            List<Candidate> candidates = future.get();
            assertEquals(topK, candidates.size());
        }

        executorService.shutdown();
    }
}
