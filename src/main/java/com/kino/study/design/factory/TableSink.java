package com.kino.study.design.factory;

/**
 * @author kino
 * @date 2024/5/11 18:30
 */
public interface TableSink {
    void open();

    void run();

    void close();
}
