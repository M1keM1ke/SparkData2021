package ru.mike.unsafe_sparkdata.collection.lazy;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import ru.mike.unsafe_sparkdata.collection.LazySparkList;

import java.util.List;

@Aspect
public class LazyCollectionAspectHandler {
    @Autowired
    private FirstLevelCacheService cacheService;
    @Autowired
    private ConfigurableApplicationContext context;

    /**
     * Срабатывает перед вызовом коллекции
     * Если коллекция не инициализирована, то берет закешированные значения и сетит в коллекцию
     * @param jp содержимое коллекции
     */
    @Before("execution(* ru.mike.unsafe_sparkdata.collection.LazySparkList.*(..)) && execution(* java.util.List.*(..)) ")
    public void setLazyCollections(JoinPoint jp) {
        LazySparkList lazyList = (LazySparkList) jp.getTarget();
        if (!lazyList.initialized()) {
            List<Object> content = cacheService.readDataFor(lazyList.getOwnerId(), lazyList.getModelClass(), lazyList.getPathToSource(),lazyList.getForeignKeyName(), context);
            lazyList.setContent(content);
        }
    }
}





