# searchy

No frills, zero-deps, multi-threaded, vector search in Java. Inspired by [annoy](https://github.com/spotify/annoy) and [fann](https://github.com/fennel-ai/fann/).

## Description

This is a simple implementation of [nearest neighbour search](https://en.wikipedia.org/wiki/Nearest_neighbor_search) using a [binary space partitioning algorithm](https://en.wikipedia.org/wiki/Binary_space_partitioning). It
is not optimized for speed or memory usage. 

### Example
```java
import io.github.gmodena.searchy.Index;

Index index = new Index.Builder()
        .withNumTrees(1)
        .withMaxNodeSize(2)
        .add(new float[]{1f, 2f, 3f})
        .add(new float[]{4f, 5f, 6f})
        .add(new float[]{7f, 8f, 9f})          
        .build();

var result = index.query(new float[]{6.5f, 8f, 9f}, 2);

result.stream().map(c -> Arrays.toString(c.vector().raw() ) + " " + c.id() + " " + c.distance())
        .forEach(System.out::println);
```

## Installation

Jars are published to [jitpack.io](https://jitpack.io/#gmodena/searchy/).

### Maven
Add jitpack to your repositories:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
Then add a dependency on `io.github.gmodena:searchy` with:
```xml
<dependency>
    <groupId>io.github.gmodena</groupId>
    <artifactId>searchy</artifactId>
    <version>main-SNAPSHOT</version>
</dependency>
```

Check https://jitpack.io/#gmodena/searchy/ for the latest version.

### Manually
You can build a jar locally by cloning this repo and running gradle:
```
$ git clone https://github.com/gmodena/searchy.git
$ ./gradlew :searchy-index:build
```
The jar will be available under `./index/build/libs/searchy-index-*.jar`.

## API
Build project documentation with:
```commandline
./gradlew :searchy-index:javadoc
```

HTML output will be available under `./index/build/docs/javadoc/index.html`.
