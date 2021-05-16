package ru.mike.unsafe_sparkdata.collection.lazy;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import ru.mike.unsafe_sparkdata.extractor.DataExtractor;
import ru.mike.unsafe_sparkdata.extractor.DataExtractorResolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirstLevelCacheService {
    private Map<Class<?>, Dataset<Row>> model2Dataset = new HashMap<>();

    @Autowired
    private DataExtractorResolver extractorResolver;

    public List readDataFor(long ownerId, Class<?> modelClass, String pathToSource, String foreignKey, ConfigurableApplicationContext context) {
        if (!model2Dataset.containsKey(modelClass)) {
            DataExtractor extractor = extractorResolver.resolve(pathToSource);
            Dataset<Row> dataset = extractor.load(pathToSource, context);
            dataset.persist();
            model2Dataset.put(modelClass, dataset);

        }

        Encoder<?> encoder = Encoders.bean(modelClass);
        return model2Dataset.get(modelClass)
                .filter(functions.col(foreignKey).equalTo(ownerId))
                .as(encoder)
                .collectAsList();
    }
}







