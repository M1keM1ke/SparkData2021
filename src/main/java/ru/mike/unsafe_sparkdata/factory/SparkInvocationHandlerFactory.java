package ru.mike.unsafe_sparkdata.factory;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import ru.mike.unsafe_sparkdata.SparkRepository;
import ru.mike.unsafe_sparkdata.annotation.Source;
import ru.mike.unsafe_sparkdata.annotation.Transient;
import ru.mike.unsafe_sparkdata.collection.lazy.LazyCollectionInjectorPostFinalizer;
import ru.mike.unsafe_sparkdata.extractor.DataExtractor;
import ru.mike.unsafe_sparkdata.extractor.DataExtractorResolver;
import ru.mike.unsafe_sparkdata.finalizer.Finalizer;
import ru.mike.unsafe_sparkdata.proxy.SparkInvocationHandler;
import ru.mike.unsafe_sparkdata.spider.TransformationSpider;
import ru.mike.unsafe_sparkdata.transformation.SparkTransformation;
import ru.mike.unsafe_sparkdata.util.WordsMatcher;
import scala.Tuple2;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SparkInvocationHandlerFactory {
    private final DataExtractorResolver resolver;
    private final Map<String, TransformationSpider> spiderMap;
    private final Map<String, Finalizer> finalizerMap;

    @Setter
    private ConfigurableApplicationContext realContext;

    /**
     * Формирует invocationHandler для прокси
     * @param sparkRepoInterface репозиторий
     * @return сформированный {@link SparkInvocationHandler}
     */
    public SparkInvocationHandler create(Class<? extends SparkRepository> sparkRepoInterface) {
        Class<?> modelClass = getModelClass(sparkRepoInterface);
        String pathToData = modelClass.getAnnotation(Source.class).value();
        Set<String> fieldNames = getFieldNames(modelClass);
        DataExtractor dataExtractor = resolver.resolve(pathToData); //определяем подходящий DataExtractor для файла с данными
        Map<Method, List<Tuple2<SparkTransformation,List<String>>>> transformationChain = new HashMap<>();
        Map<Method, Finalizer> method2Finalizer = new HashMap<>();

        Method[] methods = sparkRepoInterface.getMethods();

        for (Method method : methods) {
            TransformationSpider currentSpider = null;
            List<String> methodWords = WordsMatcher.toWordsByJavaConvention(method.getName()); //парсим название метода репозитория по словам
            List<Tuple2<SparkTransformation,List<String>>> transformations = new ArrayList<>();

            while (methodWords.size() > 1) {
                String spiderName = WordsMatcher.findAndRemoveMatchingPiecesIfExists(spiderMap.keySet(), methodWords);

                if (!spiderName.isEmpty()) {
                    currentSpider = spiderMap.get(spiderName);
                }

                transformations.add(currentSpider.getTransformation(methodWords,fieldNames));
            }

            transformationChain.put(method, transformations);
            String finalizerName = "collect";

            if (methodWords.size() == 1) {
                finalizerName = Introspector.decapitalize(methodWords.get(0));
            }

            method2Finalizer.put(method, finalizerMap.get(finalizerName));
        }

        return SparkInvocationHandler.builder()
                .modelClass(modelClass)
                .pathToData(pathToData)
                .dataExtractor(dataExtractor)
                .transformationChain(transformationChain)
                .finalizerMap(method2Finalizer)
                .postFinalizer(new LazyCollectionInjectorPostFinalizer(realContext))
                .context(realContext)
                .build();
    }

    /**
     * Определяет модель параметризированного репозитория
     * @param repoInterface репозиторий
     * @return класс-модель репозитория
     */
    private Class<?> getModelClass(Class<? extends SparkRepository> repoInterface) {
        ParameterizedType genericInterface = (ParameterizedType) repoInterface.getGenericInterfaces()[0];
        Class<?> modelClass = (Class<?>) genericInterface.getActualTypeArguments()[0];
        return modelClass;
    }

    /**
     * Возвращает поля энтити репозитория, не являющиеся транзиентными или коллекциями
     * @param modelClass Класс-энтити
     * @return коллекция полей
     */
    private Set<String> getFieldNames(Class<?> modelClass) {
        return Arrays.stream(modelClass.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Transient.class))
                .filter(field -> !Collection.class.isAssignableFrom(field.getType()))
                .map(Field::getName)
                .collect(Collectors.toSet());

    }
}
