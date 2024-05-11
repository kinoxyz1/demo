package com.kino.flink.T01;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

/**
 * @author kino
 * @date 2024/5/9 23:35
 */
public class KafkaDynamicTableTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        env.setParallelism(1);

        EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().build();
        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env, settings);

        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE KafkaSourceTable (");
        sb.append("  `id` STRING,");
        sb.append("  `name` STRING");
        sb.append(") WITH (");
        sb.append("  'connector' = 'kafka',");
        sb.append("  'topic' = 'test',");
        sb.append("  'properties.bootstrap.servers' = '127.0.0.1:9092',");
        sb.append("  'properties.group.id' = 'testGroup',");
        sb.append("  'scan.startup.mode' = 'earliest-offset', ");
        sb.append("  'format' = 'json'");
        sb.append(")");
        tEnv.executeSql(sb.toString());
        Table t = tEnv.sqlQuery("SELECT * FROM KafkaSourceTable");
        tEnv.toAppendStream(t, Row.class).print();

        env.execute();
    }
}
