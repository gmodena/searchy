package io.github.gmodena.wikinews;

import io.github.gmodena.searchy.Index;

import org.apache.commons.cli.*;

import java.util.*;

import static java.lang.StringTemplate.STR;

/**
 * A simple command line runner for the BSP index.
 * Used for demo and benchmarking purposes.
 * Indexes a dataset, queries it with random vectors, and prints the results.
 *
 * Developed and tested with the WikiNews dataset.
 */
public class Runner {
    static Index mkIndex(Dataset dataset, Integer numTrees, Integer maxSize) {
        return mkIndex(dataset, numTrees, maxSize, true);
    }

    static Index mkIndex(Dataset dataset, Integer numTrees, Integer maxSize, boolean deduplicate) {
        var startTime = System.currentTimeMillis();
        Index index = new Index.Builder()
                .setNumTrees(numTrees)
                .setMaxSize(maxSize)
                .add(dataset.vectors(), dataset.ids())
                .deduplicate(deduplicate)
                .build();
        var endTime = System.currentTimeMillis();
        System.out.println("Built BSP index with "
                + numTrees + " trees, and " + maxSize + "max node size in "
                + (endTime - startTime) + "ms");

        return index;
    }

    private static CommandLine cli(String[] args) {
        Options options = new Options();
        Option input = new Option("i", "input", true, "Path to the dataset");
        Option numTrees = new Option("n", "num-trees", true, "Number of trees");
        Option maxNodeSize = new Option("m", "max-node-size", true, "Maximum node size");
        Option deduplicate = new Option("d", "deduplicate", false, "Deduplicate vectors");
        Option maxRecords = new Option("r", "max-records", true, "Maximum number of records to read");
        Option numQueryVectors = new Option("q", "num-query-vectors", true, "Number of query vectors");
        Option numResults = new Option("k", "num-results", true, "Number of results to return");

        input.setRequired(true);
        numTrees.setType(Integer.class);
        maxNodeSize.setType(Integer.class);
        deduplicate.setType(Boolean.class);
        maxRecords.setType(Integer.class);
        numQueryVectors.setType(Integer.class);
        numResults.setType(Integer.class);

        options.addOption(input);
        options.addOption(numTrees);
        options.addOption(maxNodeSize);
        options.addOption(deduplicate);
        options.addOption(maxRecords);
        options.addOption(numQueryVectors);
        options.addOption(numResults);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("bsp-search-runner", options);

            System.exit(1);
        } finally {
            return cmd;
        }
    }

    public static void main(String[] args) {
        CommandLine cmd = cli(args);

        var input = cmd.getOptionValue("input");
        int numTrees = Integer.parseInt(cmd.getOptionValue("num-trees", "10"));
        int maxNodeSize = Integer.parseInt(cmd.getOptionValue("max-node-size", "3"));
        int numQueryVectors = Integer.parseInt(cmd.getOptionValue("num-query-vectors", "3"));
        int numResults = Integer.parseInt(cmd.getOptionValue("num-results", "3"));
        Optional<Integer> maxRecords = Optional.ofNullable(cmd.getOptionValue("max-records")).map(Integer::parseInt);
        boolean deduplicate = cmd.hasOption("deduplicate");


        var dataset = WikiNewsSource.read(input, maxRecords);

        Index index = Runner.mkIndex(dataset, numTrees, maxNodeSize, deduplicate);

        Random random = new Random();

        ArrayList<float[]> queryVectors = new ArrayList<>(); // dataset.vectors().get(3); // query vector (id=3)

        // uniform random sampling with repetition.
        // Callers might dedup at indexing time, but not at query time.
        for (int i = 0; i < numQueryVectors; i++) {
            int randomIndex = random.nextInt(dataset.vectors().size());
            queryVectors.add(dataset.vectors().get(randomIndex));
        }

        var startTime = System.currentTimeMillis();
        var result = index.query(queryVectors, numResults);
        var endTime = System.currentTimeMillis();
        System.out.println("Querying BSP index in " + (endTime - startTime) + "ms");
        result.stream()
                .map(c -> STR."\{Arrays.toString(c.vector())} \{c.id()} \{c.distance()}").forEach(System.out::println);
    }
}