package ru.mike.unsafe_sparkdata.transformation;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import ru.mike.unsafe_sparkdata.collection.OrderedBag;

import java.util.List;

public interface SparkTransformation {
    /**
     * @param dataset
     * @param columnNames
     * @param args
     * @return
     */
    Dataset<Row> transform(Dataset<Row> dataset, List<String> columnNames, OrderedBag<Object> args);
}
