package com.kino.study.design.factory;

/**
 * @author kino
 * @date 2024/5/11 18:29
 */
public interface TableSource extends Table {
    void open();

    void run();

    void close();
}
