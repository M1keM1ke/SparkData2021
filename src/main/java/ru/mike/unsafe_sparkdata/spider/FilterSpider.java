package ru.mike.unsafe_sparkdata.spider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mike.unsafe_sparkdata.transformation.FilterTransformation;
import ru.mike.unsafe_sparkdata.transformation.SparkTransformation;
import ru.mike.unsafe_sparkdata.util.WordsMatcher;
import scala.Tuple2;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component("findBy")
@RequiredArgsConstructor
public class FilterSpider implements TransformationSpider {
    private final Map<String, FilterTransformation> filterTransformationMap;

    @Override
    public Tuple2<SparkTransformation,List<String>> getTransformation(List<String> methodWords, Set<String> fieldNames) {
        List<String> columnNames = List.of(WordsMatcher.findAndRemoveMatchingPiecesIfExists(fieldNames, methodWords));
        String filterName = WordsMatcher.findAndRemoveMatchingPiecesIfExists(filterTransformationMap.keySet(), methodWords);
        return new Tuple2<>(filterTransformationMap.get(filterName), columnNames);
    }
}







