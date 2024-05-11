package com.kino.study.design.factory;

import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author kino
 * @date 2024/5/11 18:43
 */
public class FactoryUtils {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        TableSourceFactory factory = createTableSource("kafka");
        TableSource tableSource = factory.createTableSource();
        tableSource.open();
    }

    public static TableSourceFactory createTableSource(String factoryName) throws InstantiationException, IllegalAccessException {
        Class<?> interfaceClass = TableFactory.class;
        Reflections reflections = new Reflections("");
        Set<Class<?>> subTypesOf = ((Set<Class<?>>) reflections.getSubTypesOf(interfaceClass)).stream().filter(x -> !x.isInterface()).collect(Collectors.toSet());
        for (Class<?> aClass : subTypesOf) {
            if (aClass.getSimpleName().toLowerCase().contains(factoryName)) {
                return (TableSourceFactory) aClass.newInstance();
            }
        }
        return null;
    }
}
