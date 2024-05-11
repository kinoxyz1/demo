package com.kino.study.design.factory.impl.kafka;

import com.kino.study.design.factory.TableSink;

/**
 * @author kino
 * @date 2024/5/11 18:33
 */
public class KafkaTableSink implements TableSink {

    @Override
    public void open() {
        System.out.println("KafkaTableSink.open");
    }

    @Override
    public void run() {
        System.out.println("KafkaTableSink.run");
    }

    @Override
    public void close() {
        System.out.println("KafkaTableSink.close");
    }
}
