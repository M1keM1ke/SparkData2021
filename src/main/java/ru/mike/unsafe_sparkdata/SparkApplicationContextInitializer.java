package ru.mike.unsafe_sparkdata;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.mike.unsafe_sparkdata.extractor.DataExtractorResolver;
import ru.mike.unsafe_sparkdata.factory.SparkInvocationHandlerFactory;

import java.beans.Introspector;
import java.lang.reflect.Proxy;
import java.util.Set;

public class SparkApplicationContextInitializer implements ApplicationContextInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        AnnotationConfigApplicationContext tempContext = new AnnotationConfigApplicationContext("ru.mike.unsafe_sparkdata");
        SparkInvocationHandlerFactory factory = moveBeansToRealContext(context, tempContext);
        tempContext.close();

        factory.setRealContext(context); //сетим в фабрику реальный контекст

        registerSparkBean(context); //регистрируем бины Spark в нашем контексте
        String packagesToScan = context.getEnvironment().getProperty("spark.packages-to-scan");
        Reflections scanner = new Reflections(packagesToScan);
        Set<Class<? extends SparkRepository>> sparkRepoInterfaces = scanner.getSubTypesOf(SparkRepository.class);
        sparkRepoInterfaces.forEach(sparkRepoInterface -> {
            Object golem = Proxy.newProxyInstance(sparkRepoInterface.getClassLoader(), //создаем прокси репозиториев
                    new Class[]{sparkRepoInterface},
                    factory.create(sparkRepoInterface));
            context.getBeanFactory() //регистрируем в контексте наш прокси-репозиторий в качестве бина
                    .registerSingleton(Introspector.decapitalize(sparkRepoInterface.getSimpleName()), golem);

        });
    }

    private SparkInvocationHandlerFactory moveBeansToRealContext(ConfigurableApplicationContext context, AnnotationConfigApplicationContext tempContext) {
        SparkInvocationHandlerFactory factory = tempContext.getBean(SparkInvocationHandlerFactory.class);
        DataExtractorResolver extractorResolver = tempContext.getBean(DataExtractorResolver.class);
        context.getBeanFactory().registerSingleton("sparkDataResolver", extractorResolver);
        return factory;
    }

    /**
     * Регистрирует бины Spark в контексте Spring
     * @param context реальный контекст Spring
     */
    private void registerSparkBean(ConfigurableApplicationContext context) {
        String appName = context.getEnvironment().getProperty("spark.app-name");

        SparkSession sparkSession = SparkSession.builder().appName(appName).master("local[*]").getOrCreate();
        JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext());
        context.getBeanFactory().registerSingleton("sparkContext", sparkContext);
        context.getBeanFactory().registerSingleton("sparkSession", sparkSession);
    }
}





