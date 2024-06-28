package com.kino.flink.T01;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import static org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer.timestamp;

/**
 * @author kino
 * @date 2024/5/30 00:01
 */
public class BatchLookupJoinTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        env.setParallelism(1);

        EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().build();
        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env, settings);
        tEnv.getConfig().getConfiguration().setBoolean("is.dim.batch.mode", true);

        /**
         * {"sheet_id": "643e1912-a3f7-4703-bf05-6cbe9ee0ac9f", "sale_date": "2012-04-08 12:10:00", "_table_name_": "t1", "meta_ts": "2012-04-08 12:10:00"}
         * {"sheet_id": "0af6a2a0-319d-47fa-b5a2-1f5af079f4f6", "sale_date": "2012-04-08 12:10:30", "_table_name_": "t1", "meta_ts": "2012-04-08 12:10:30"}
         * {"sheet_id": "8b9c65b8-d77b-442a-893f-c79d874c2e3a", "sale_date": "2012-04-08 12:11:00", "_table_name_": "t1", "meta_ts": "2012-04-08 12:11:00"}
         * {"sheet_id": "643e1912-a3f7-4703-bf05-6cbe9ee0ac9f", "sale_date": "2012-04-08 12:11:30", "_table_name_": "t1", "meta_ts": "2012-04-08 12:11:30"}
         * {"sheet_id": "f59f666d-0183-462e-95f7-ef28e070a4de", "sale_date": "2012-04-08 12:12:00", "_table_name_": "t1", "meta_ts": "2012-04-08 12:12:00"}
         */
        StringBuffer kafkaTable = new StringBuffer();
        kafkaTable.append("CREATE temporary TABLE kafkatable (");
        kafkaTable.append("  sheet_id  varchar(50), ");
        kafkaTable.append("  sale_date varchar(50), ");
        kafkaTable.append("  _table_name_   varchar(200), ");
        kafkaTable.append("  meta_ts        timestamp(3) metadata from 'timestamp', ");
        kafkaTable.append("  proc_time      as proctime(), ");
        kafkaTable.append("  watermark      for meta_ts as meta_ts - interval '1' MINUTE ");
        kafkaTable.append(") WITH (");
        kafkaTable.append("  'connector' = 'kafka',");
        kafkaTable.append("  'topic' = 'test',");
        kafkaTable.append("  'properties.bootstrap.servers' = '127.0.0.1:9092',");
        kafkaTable.append("  'properties.group.id' = 'testGroup',");
        kafkaTable.append("  'scan.startup.mode' = 'latest-offset', ");
        kafkaTable.append("  'format' = 'json'");
        kafkaTable.append(")");

        StringBuffer mysqlTable = new StringBuffer("CREATE temporary TABLE mysqltable (");
        mysqlTable.append("id varchar(50),");
        mysqlTable.append("name STRING,");
        mysqlTable.append("age INT,");
        mysqlTable.append("birthday DATE,");
        mysqlTable.append("classname STRING,");
        mysqlTable.append("PRIMARY KEY (id) NOT ENFORCED");
        mysqlTable.append(") WITH (");
        mysqlTable.append("'connector' = 'jdbc',");
        mysqlTable.append("'url' = 'jdbc:mysql://localhost:12345/kinodb',");
        mysqlTable.append("'table-name' = 't1',");
        mysqlTable.append("'username' = 'kino',");
        mysqlTable.append("'password' = '123456',");
        mysqlTable.append("'lookup.batch.join' = 'true',");
        mysqlTable.append("'lookup.batch.size' = '200',");
        mysqlTable.append("'lookup.batch.linger' = '3s'");
        mysqlTable.append(")");


        StringBuffer mysqlTableSink = new StringBuffer("CREATE temporary TABLE mysqltablesink (");
        mysqlTableSink.append("id varchar(50),");
        mysqlTableSink.append("name STRING,");
        mysqlTableSink.append("age INT,");
        mysqlTableSink.append("birthday DATE,");
        mysqlTableSink.append("classname STRING,");
        mysqlTableSink.append("  sheet_id  varchar(200), ");
        mysqlTableSink.append("  sale_date varchar(200), ");
        mysqlTableSink.append("  _table_name_   varchar(200) ");
        mysqlTableSink.append(") WITH (");
        mysqlTableSink.append("'connector' = 'jdbc',");
        mysqlTableSink.append("'url' = 'jdbc:mysql://localhost:12345/kinodb',");
        mysqlTableSink.append("'table-name' = 't2',");
        mysqlTableSink.append("'username' = 'kino',");
        mysqlTableSink.append("'password' = '123456'");
        mysqlTableSink.append(")");


        StringBuffer sinkTable = new StringBuffer(" ");
        // sinkTable.append(" insert into mysqltablesink ");
        sinkTable.append(" select u.id,u.name,u.age,u.birthday,u.classname,s.sheet_id,s.sale_date,s._table_name_ as _table_name_ from kafkatable AS s ");
        sinkTable.append(" inner join mysqltable FOR system_time AS OF s.proc_time AS u");
        sinkTable.append(" on s.sheet_id = u.id");

        tEnv.executeSql(mysqlTable.toString());
        tEnv.executeSql(kafkaTable.toString());
        tEnv.executeSql(mysqlTableSink.toString());
        tEnv.executeSql(sinkTable.toString());

        tEnv.toAppendStream(tEnv.sqlQuery(sinkTable.toString()), Row.class).print();

        env.execute();
    }
}
