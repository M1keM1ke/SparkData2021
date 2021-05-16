package ru.mike.unsafe_sparkdata.transformation;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import ru.mike.unsafe_sparkdata.collection.OrderedBag;

import java.util.List;

public class SortTransformation implements SparkTransformation {
    @Override
    public Dataset<Row> transform(Dataset<Row> dataset, List<String> columnNames, OrderedBag<Object> args) {
       return dataset
               .orderBy(columnNames.get(0), columnNames.stream()
               .skip(1)
               .toArray(String[]::new));
    }
}
