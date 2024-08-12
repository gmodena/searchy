package io.github.gmodena.wikinews;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class WikiNewsSource {
    public static Dataset read(String fileName, Optional<Integer> maxRecords) {
        // Read a CSV file from a local directory.
        // Format:
        // The first line of the file contains the number of words
        // in the vocabulary and the size of the vectors.
        // Each line contains a word followed by its vectors,
        // like in the default fastText text format.
        // Each value is space separated.
        // Words are ordered by descending frequency.
        String line;
        String tabSplitBy = " ";
        ArrayList<float[]> vectors = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();
        float[] vector;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            // skip header
            br.readLine();

            int counter = 0;
            while ((line = br.readLine()) != null) {
                if ((maxRecords.isPresent()) && (counter >= maxRecords.get())) {
                    break;
                }
                vector = new float[300];
                // first columns is a token. Skip it.
                String[] vectorStr = line.split(tabSplitBy);
                for (int i = 1; i < vectorStr.length - 1; i++) {
                    vector[i] = Float.parseFloat(vectorStr[i]);
                }
                vectors.add(vector);
                ids.add(counter);
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Read " + vectors.size() + " vectors");
        return new Dataset(vectors, ids);
    }
}
