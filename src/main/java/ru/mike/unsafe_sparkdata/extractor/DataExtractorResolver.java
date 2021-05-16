package ru.mike.unsafe_sparkdata.extractor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DataExtractorResolver {
    @Autowired
    private Map<String, DataExtractor> extractorMap;

    /**
     * Определяет и возвращает DataExtractor, соответствующий дата-файлу
     * @param pathToData путь до файла
     * @return подходящий DataExtractor
     */
    public DataExtractor resolve(String pathToData) {
        String fileExtension = pathToData.split("\\.")[1]; //определяем расширение файла
        return extractorMap.get(fileExtension);
    }
}
