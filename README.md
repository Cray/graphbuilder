# Intel(R) Graph Builder 2.0 (alpha) 

Intel Graph Builder 2.0 (alpha) is a library of user defined functions (UDFs) and
macros in Pig Latin used to construct graphs in Hadoop. The 2.0 (alpha) version
supports multi-relational graphs, or property graphs, in which both objects
and relationships may be labeled with multiple properties and property values.

Graphs can be constructed from structured, semi-structured,
or unstructured data. In the case of structured data, columns of HBase tables or
fields in CSV/TSV files for example can be annotated as objects,
relationships, or their properties.

To do the same from nested XML and JSON we
have provided an improved `XMLLoader` function (available in the Apache Piggy Bank
repository) to parse XML files, an `ExtractJSONField` UDF to extract JSON Path
matches from a JSON string and a `RegexExtractAllMatches` utility which extracts all
text matches in a string. Once a graph is constructed, use the de-duplication macro to merge duplicate elements.
These capabilities can easily be extended by writing your own custom user defined function.

Of course, there's no point in building a graph if you can't query,
analyze or visualize it. So, we have introduced new bulk load and export
methods. The `LOAD_TITAN` macro bulk loads the open source [Titan][1] distributed
graph database through the Blueprints API so that you can explore the graphs
using the Gremlin query language.

In addition, we have extended Graph Builder to support the Resource Description Framework 
(RDF) export format. We use the [Apache Jena][2] library to form RDF triples for property 
graph elements, RDF graphs are exported in the [NTriples][3] format.

Last, but not least this version of the Graph Builder library can also export simple edge
(object) lists and vertex (relationship) lists. You can use graph visualization tools such as Gephi with
the edge list exports.

[1]: http://thinkaurelius.github.io/titan/
[2]: http://jena.apache.org
[3]: http://www.w3.org/TR/n-triples/

## License

Graph Builder is licensed under the Apache License 2.0

## Disclaimer

This repository represents an experimental fork of the original Intel code developed internally at Cray, while we hope
to feed out changes back into the upstream repository we make no guarantees that this will happen.  Cray also makes no
guarantees that this code will be supported in any way, shape or form moving forwards.

## Building

Intel Graph Builder is Java based and uses Apache Maven 3 as the build manager and targets Java 7. Please ensure Maven 3 
and Java 7 are installed io your system.

To build the library and run the unit tests:

    mvn clean package
    
To build the library without running the unit tests:

    mvn clean package -DskipTests

To install Graph Builder:

    mvn clean install

### Build Status

GraphBuilder uses continuous integration provided by [Travis CI](http://travis-ci.org)

[![Build Status](https://travis-ci.org/Cray/graphbuilder.png?branch=2.0.alpha)](https://travis-ci.org/Cray/graphbuilder)

### Dependencies

Please use Titan version 0.4.1 to execute Gremlin queries.

## Using Graph Builder

Please refer to the Pig scripts provided in the *examples/* directory to run
the different use cases of Graph Builder.

### Wikipedia Example

The **wikipedia_example.pig** script
constructs a bipartite Link-Page graph from Wikipedia dataset (XML format).
You can download the Wiki page dump containing pages in English language
from the following location:

[http://download.wikimedia.org/enwiki/latest/enwiki-latest-pages-articles.xml.bz2](http://download.wikimedia.org/enwiki/latest/enwiki-latest-pages-articles.xml.bz2)

This size of the data is 9.5GB compressed and 44GB uncompressed. A smaller
version of the Wiki dump containing a subset of the pages can be downloaded
from:

[http://dumps.wikimedia.org/enwiki/latest/enwiki-latest-pages-articles1.xml-p000000010p000010000.bz2](http://dumps.wikimedia.org/enwiki/latest/enwiki-latest-pages-articles1.xml-p000000010p000010000.bz2)

We have also provided some toy data to demonstrate the functionality
of the UDF and macros provided in this distribution. They are in the
examples/data directory. Please see the Known Issues if you hit any problems.

### RDF Example

The **rdf_example.pig** script constructs a simple RDF graph out of an example employee database, it demonstrates the flexible declarative nature of the RDF mapping.

#### RDF generation in Cray vs Intel

The declarative mappings for RDF and Property Graphs are the major difference between Cray's fork of GraphBuilder and Intel's code.  We believe that our declarative mapping approach 
is significantly easier to write and understand as well as exposing substantially more control over how property graphs and RDF are generated.  It also has the benefit of decoupling the
generation of property graphs from the generation of RDF graphs to some degree.  While our RDF generation still requires a property graph as input the property graph does not need to care
about RDF namespaces as in the Intel version.

## Documentation

You can find the HTML documentation under *docs/html* directory.

## Release Notes

Added the following UDFs and macros:

- Added the following UDFs and macros:
    - CreatePropGraphElements
    - ExtractJSONField
    - GetPropGraphElementID
    - MergeDuplicateGraphElements
    - RDF
    - VertexList
    - EdgeList
    - RegexExtractAllMatches  
    - CreateRowKey
    - `MERGE_DUPLICATE_ELEMENTS`
    - `LOAD_TITAN`
- Added the `TableToTitanGraph` MapReduce application to bulk load property graphs from HBase Tables to the open source Titan graph database
- Removed ID normalization and partitioning
- Removed the wordpage graph and the linkgraph tokenizer from the demoapps

## Configuration & Tuning

### Setting the Hadoop classpath 

To use the Intel Graph Builder library,
please set the `HADOOP_CLASSPATH` as follows:

    export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:$GRAPHBUILDER_HOME/target/graphbuilder-2.0-alpha-with-deps.jar

### Tuning

Hadoop, HBase, and Titan must be tuned carefully to successfully create large
graphs (in the order of hundreds of million vertices/edges such as the Wikipedia dataset). Please refer to
[https://github.com/thinkaurelius/titan/wiki/Bulk-Loading](https://github.com/thinkaurelius/titan/wiki/Bulk-Loading) for tuning Titan. In particular,
we recommend tuning the following parameters:

- `graphbuilder.titan.ids.block-size`
- `graphbuilder.titan.ids.partition`
- `graphbuilder.titan.ids.num-partitions`
- `graphbuilder.titan.storage.idauthority-wait-time`
- `graphbuilder.titan.ids.renew-timeout`
- `graphbuilder.titan.ids.idauthority-retries`
- `storage.buffer-size`
- `graphbuilder.titan.storage.attempt-wait`
- `graphbuilder.titan.storage.write-attempts`
- `graphbuilder.titan.storage.batch-loading`

During bulk loading Titan we recommend to disable the tx cache by setting `graphbuilder.titan.tx-cache-size` to `0`
and we recommend setting Hadoop's `mapred.reduce.tasks` and `mapred.task.timeout` parameters.

For the HBase configuration, we recommend tuning the following parameters:

- `hbase.zookeeper.property.maxClientCnxns`
- `zookeeper.session.timeout`
- `hbase.hregion.max.filesize`
- `hbase.regionserver.handler.count`
- `hbase.rpc.timeout`
- `hbase.client.write.buffer`