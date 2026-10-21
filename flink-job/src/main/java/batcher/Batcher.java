package batcher;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;

public final class Batcher {
    private static final String TOPIC = "lab";
    private static final String BOOTSTRAP_SERVERS = "kafka:9094";

    public static void main(String[] args) {
        TableEnvironment tEnv = TableEnvironment.create(EnvironmentSettings.inStreamingMode());
        tEnv.executeSql(sourceTable());
        tEnv.sqlQuery("SELECT * FROM lab").execute().print();
    }

    static String sourceTable() {
        return "CREATE TEMPORARY TABLE lab (\n"
            + "  `time` STRING,\n"
            + "  `id` STRING,\n"
            + "  `name` STRING,\n"
            + "  `age` INT\n"
            + ") WITH (\n"
            + "  'connector' = 'kafka',\n"
            + "  'topic' = '" + TOPIC + "',\n"
            + "  'properties.bootstrap.servers' = '" + BOOTSTRAP_SERVERS + "',\n"
            + "  'properties.group.id' = 'lab-printer',\n"
            + "  'scan.startup.mode' = 'earliest-offset',\n"
            + "  'format' = 'json',\n"
            + "  'json.ignore-parse-errors' = 'true'\n"
            + ")";
    }
}
