package ru.mike.unsafe_sparkdata.extractor;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.context.ConfigurableApplicationContext;

public interface DataExtractor {
    /**
     * Spark extractor данных (json, csv, jdbc)
     * @param pathToData путь до файла с данными
     * @param context контекст приложения
     * @return датасет с данными
     */
    Dataset<Row> load(String pathToData, ConfigurableApplicationContext context);
}
