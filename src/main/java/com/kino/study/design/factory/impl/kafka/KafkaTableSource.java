package com.kino.study.design.factory.impl.kafka;

import com.kino.study.design.factory.TableSource;

/**
 * @author kino
 * @date 2024/5/11 18:32
 */
public class KafkaTableSource implements TableSource {
    @Override
    public void open() {
        System.out.println("KafkaTableSource.open");
    }

    @Override
    public void run() {
        System.out.println("KafkaTableSource.run");
    }

    @Override
    public void close() {
        System.out.println("KafkaTableSource.close");
    }
}
