package ru.mike.unsafe_sparkdata.extractor;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component("json")
public class JsonDataExtractor implements DataExtractor {
    /**
     * Spark extractor данных для json файлов
     * @param pathToData путь до файла с данными
     * @param context    контекст приложения
     * @return датасет из json файла
     */
    @Override
    public Dataset<Row> load(String pathToData, ConfigurableApplicationContext context) {
       return context.getBean(SparkSession.class).read().json(pathToData);
    }
}
