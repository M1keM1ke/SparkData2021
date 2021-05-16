package ru.mike.starter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.mike.unsafe_sparkdata.collection.LazySparkList;
import ru.mike.unsafe_sparkdata.collection.lazy.FirstLevelCacheService;
import ru.mike.unsafe_sparkdata.collection.lazy.LazyCollectionAspectHandler;

@Configuration
public class StartConf {
    @Bean
    @Scope("prototype")
    public LazySparkList lazySparkList(){
        return new LazySparkList();
    }

    @Bean
    public FirstLevelCacheService firstLevelCacheService(){
        return new FirstLevelCacheService();
    }

    @Bean
    public LazyCollectionAspectHandler lazyCollectionAspectHandler(){
        return new LazyCollectionAspectHandler();
    }
}
