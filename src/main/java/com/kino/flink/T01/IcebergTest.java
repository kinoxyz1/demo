// package com.kino.flink.T01;
//
// import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
// import org.apache.flink.table.api.EnvironmentSettings;
// import org.apache.flink.table.api.Table;
// import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
// import org.apache.flink.types.Row;
//
// /**
//  * @author kino
//  * @date 2024/6/5 15:05
//  */
// public class IcebergTest {
//     public static void main(String[] args) throws Exception {
//         StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
//         env.setParallelism(1);
//
//         System.setProperty("java.security.krb5.conf", "/Users/kino/Downloads/hive_client.keytab-jz/krb5.conf");
//
//         Configuration conf = new Configuration();
//         conf.set("hadoop.security.authentication", "kerberos");
//
//
//         EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().build();
//         StreamTableEnvironment tEnv = StreamTableEnvironment.create(env, settings);
//
//         StringBuffer sb = new StringBuffer();
//         sb.append("CREATE CATALOG hive_catalog WITH (");
//         sb.append("  'type'='iceberg',");
//         sb.append("  'uri'='thrift://jz-desktop-01:9083',");
//         sb.append("  'clients'='5',");
//         sb.append("  'property-version'='1',");
//         sb.append("  'warehouse'='hdfs://ns1/user/hive/warehouse/',");
//         sb.append("  'format-version'='2'");
//         sb.append(");");
//         tEnv.executeSql(sb.toString());
//         Table t = tEnv.sqlQuery("select * from hive_catalog.hzh.test1");
//         tEnv.toAppendStream(t, Row.class).print();
//
//         env.execute();
//     }
// }
