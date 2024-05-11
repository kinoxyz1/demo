package com.kino.study.design.factory.impl.kafka;

import com.kino.study.design.factory.TableSink;
import com.kino.study.design.factory.TableSinkFactory;
import com.kino.study.design.factory.TableSource;
import com.kino.study.design.factory.TableSourceFactory;

/**
 * @author kino
 * @date 2024/5/11 18:31
 */
public class KafkaTableFactory implements TableSinkFactory, TableSourceFactory {

    private static final String IDENTIFIER = "Kafka";

    @Override
    public TableSink createTableSink() {
        return new KafkaTableSink();
    }

    @Override
    public TableSource createTableSource() {
        return new KafkaTableSource();
    }

    @Override
    public String factoryIdentifier() {
        return IDENTIFIER;
    }
}
