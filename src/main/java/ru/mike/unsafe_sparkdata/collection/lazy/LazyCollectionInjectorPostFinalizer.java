package ru.mike.unsafe_sparkdata.collection.lazy;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ConfigurableApplicationContext;
import ru.mike.unsafe_sparkdata.annotation.ForeignKey;
import ru.mike.unsafe_sparkdata.annotation.Source;
import ru.mike.unsafe_sparkdata.collection.LazySparkList;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class LazyCollectionInjectorPostFinalizer implements PostFinalizer {
    private final ConfigurableApplicationContext realContext;

    /**
     * Сохраняет значения
     * @param retVal коллекция энтити
     * @return коллекция энтити
     */
    @SneakyThrows
    @Override
    public Object postFinalize(Object retVal) {
        if (Collection.class.isAssignableFrom(retVal.getClass())) { //если на вход пришла коллекция
            for (Object model : (List) retVal) { //пробегаемся по ней
                Field idField = model.getClass().getDeclaredField("id"); //берем поле "id"
                idField.setAccessible(true);
                long ownerId = idField.getLong(model); //берем значение поля "id" ("id" энтити)
                Field[] fields = model.getClass().getDeclaredFields(); //получаем все поля
                for (Field field : fields) { //бежим по ним
                    if (List.class.isAssignableFrom(field.getType())) { //если поле - это коллекция
                        LazySparkList sparkList = realContext.getBean(LazySparkList.class);
                        sparkList.setOwnerId(ownerId); //сетим в наш лист-бин id энтити
                        String columnName = field.getAnnotation(ForeignKey.class).value(); //получаем название колонки в зависимой сущности
                        sparkList.setForeignKeyName(columnName); //сетим название колонки в наш лист-бин
                        Class<?> embeddedModel = getEmbeddedModel(field); //определяем класс зависимой сущности
                        sparkList.setModelClass(embeddedModel); //сетим ее в наш лист-бин
                        String pathToData = embeddedModel.getAnnotation(Source.class).value(); //определяем путь к дата-файлу зависимой сущности
                        sparkList.setPathToSource(pathToData); //сетим путь в наш лист-бин

                        field.setAccessible(true);
                        field.set(model,sparkList); //устанавливаем в лист энтити наш заполненный lazySparkList
                    }
                }
            }
        }
        return retVal;
    }

    /**
     * Определяет тип модели-энтити у зависимой сущности
     * @param field поле-коллекция (фк на зааисимую сущность)
     * @return класс модели
     */
    private Class<?> getEmbeddedModel(Field field) {
        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        Class<?> embeddedModel = (Class<?>) genericType.getActualTypeArguments()[0];
        return embeddedModel;
    }
}
