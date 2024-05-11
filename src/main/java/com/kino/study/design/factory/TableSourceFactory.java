package com.kino.study.design.factory;

/**
 * @author kino
 * @date 2024/5/11 18:27
 */
public interface TableSourceFactory extends TableFactory {
    TableSource createTableSource();
}
