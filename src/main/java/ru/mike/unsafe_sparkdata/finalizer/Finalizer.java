package ru.mike.unsafe_sparkdata.finalizer;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public interface Finalizer {
    /**
     * Производит финальную обработку данных, полученных в результате трансформаций метода репозитория
     * @param dataset датасет с полученными данными
     * @param model класс энтити-модели
     * @return возвращаемый объект репозитория
     */
    Object doAction(Dataset<Row> dataset, Class<?> model);
}
