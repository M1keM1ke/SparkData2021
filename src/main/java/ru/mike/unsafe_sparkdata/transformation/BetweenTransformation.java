package ru.mike.unsafe_sparkdata.transformation;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.springframework.stereotype.Component;
import ru.mike.unsafe_sparkdata.collection.OrderedBag;

import java.util.List;

@Component("between")
public class BetweenTransformation implements FilterTransformation {

    @Override
    public Dataset<Row> transform(Dataset<Row> dataset, List<String> columnNames, OrderedBag<Object> args) {
        return dataset.filter(functions.col(columnNames.get(0)).between(args.takeAndRemove(), args.takeAndRemove()));
    }
}
