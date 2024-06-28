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
        env.enableCheckpointing(1000L);
        env.setParallelism(1);

        EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().build();
        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env, settings);

        /**
         * kafka-topics.sh --create --zookeeper 10.74.175.109:2181 --topic test1 --partitions 5 --replication-factor 1
         *
         * kafka-console-producer.sh --broker-list 127.0.0.1:9092 --topic t1
         * {"id": 1, "name": "kino1"}
         * {"id": 1, "name": "kino2"}
         * {"id": 1, "name": "kino3"}
         * {"id": 1, "name": "kino4"}
         * {"id": 1, "name": "kino5"}
         * {"id": 1, "name": "kino6"}
         * {"id": 1, "name": "kino7"}
         * {"id": 1, "name": "kino8"}
         * {"id": 1, "name": "kino9"}
         *
         * kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic test1 --from-beginning
         * kafka-topics.sh --delete --topic test1 --zookeeper 10.74.175.109:2181
         *
         * CREATE TABLE `score_board` (
         *     `id` INT,
         *     `name` STRING,
         *     PRIMARY KEY (id) NOT ENFORCED
         * ) WITH (
         *     'connector' = 'starrocks',
         *     'jdbc-url' = 'jdbc:mysql://192.168.1.122:9932',
         *     'load-url' = '192.168.1.140:9930',
         *     'database-name' = 'test',
         *     'table-name' = 'score_board',
         *     'username' = 'root',
         *     'password' = 'JzData@2022'
         * );
         *
         */

        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE KafkaSourceTable (");
        sb.append("  `id` int,");
        sb.append("  `name` STRING");
        sb.append(") WITH (");
        sb.append("  'connector' = 'kafka',");
        sb.append("  'topic' = 't1',");
        sb.append("  'properties.bootstrap.servers' = '192.168.1.249:30757',");
        sb.append("  'properties.group.id' = 'testGroup',");
        sb.append("  'scan.startup.mode' = 'earliest-offset', ");
        sb.append("  'format' = 'json'");
        sb.append(")");


        StringBuffer sink = new StringBuffer();
        sink.append("CREATE TABLE `sink` (");
        sink.append("     `id` INT,");
        sink.append("     `name` STRING,");
        sink.append("     PRIMARY KEY (id) NOT ENFORCED");
        sink.append(" ) WITH (");
        sink.append("     'connector' = 'starrocks',");
        sink.append("     'jdbc-url' = 'jdbc:mysql://192.168.1.122:9932',");
        sink.append("     'load-url' = '192.168.1.140:9930',");
        sink.append("     'database-name' = 'kinodb',");
        sink.append("     'table-name' = 't1',");
        sink.append("     'username' = 'root',");
        sink.append("     'password' = 'JzData@2022'");
        sink.append(") ");


        tEnv.executeSql(sb.toString());
        // Table t = tEnv.sqlQuery("SELECT * FROM KafkaSourceTable");
        // tEnv.toAppendStream(t, Row.class).print();
        tEnv.executeSql(sink.toString());
        tEnv.executeSql("insert into sink SELECT * FROM KafkaSourceTable");

        env.execute();
    }
}
