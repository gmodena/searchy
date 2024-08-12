# searchy

No frills, zero-deps, multi-threaded, vector search in Java. Inspired by [annoy](https://github.com/spotify/annoy) and [fann](https://github.com/fennel-ai/fann/).

## Description

This is a simple implementation of [nearest neighbour search](https://en.wikipedia.org/wiki/Nearest_neighbor_search) using a [binary space partitioning algorithm](https://en.wikipedia.org/wiki/Binary_space_partitioning). It
is not optimized for speed or memory usage. 

### Example
```java
import io.github.gmodena.searchy.Index;

Index index = new Index.Builder()
        .setNumTrees(1)
        .setMaxSize(2)
        .add(new float[]{1f, 2f, 3f})
        .add(new float[]{4f, 5f, 6f})
        .add(new float[]{7f, 8f, 9f})          
        .build();

var result = index.query(new float[]{6.5f, 8f, 9f}, 2);

result.stream().map(c -> Arrays.toString(c.vector().raw() ) + " " + c.id() + " " + c.distance())
        .forEach(System.out::println);
```

## API
Build project documentation with:
```commandline
./gradlew :index:javadoc
```

HTML output will be available under `./index/build/docs/javadoc/index.html`.
