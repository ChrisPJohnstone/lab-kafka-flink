package batcher;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;

/**
 * Reads every record from the Kafka {@code lab} topic and prints it to stdout.
 *
 * <p>The generator produces JSON shaped like:
 *
 * <pre>{@code {"time": "...", "id": "uuid", "name": "abc", "age": 42}}</pre>
 *
 * <p>{@code time} is kept as a STRING because the generator writes RFC 3339
 * timestamps (e.g. {@code 2026-10-21T20:31:00.123+01:00}), which Flink's JSON
 * format cannot cast to TIMESTAMP directly.
 *
 * <p>Output goes to the TaskManager's stdout, so it is visible with
 * {@code podman logs -f flink-taskmanager} (or {@code make logs}).
 */
public final class Batcher {

    private static final String TOPIC = "lab";

    /** Reached from inside the compose network via Kafka's INTERNAL listener. */
    private static final String DEFAULT_BOOTSTRAP_SERVERS = "kafka:9094";

    private Batcher() {
    }

    public static void main(String[] args) {
        String bootstrapServers = args.length > 0 ? args[0] : DEFAULT_BOOTSTRAP_SERVERS;

        TableEnvironment tEnv = TableEnvironment.create(EnvironmentSettings.inStreamingMode());
        tEnv.getConfig().set("pipeline.name", "lab-kafka-printer");

        tEnv.executeSql(sourceTable(bootstrapServers));
        tEnv.sqlQuery("SELECT * FROM lab").execute().print();
    }

    static String sourceTable(String bootstrapServers) {
        return "CREATE TEMPORARY TABLE lab (\n"
            + "  `time` STRING,\n"
            + "  `id` STRING,\n"
            + "  `name` STRING,\n"
            + "  `age` INT\n"
            + ") WITH (\n"
            + "  'connector' = 'kafka',\n"
            + "  'topic' = '" + TOPIC + "',\n"
            + "  'properties.bootstrap.servers' = '" + bootstrapServers + "',\n"
            + "  'properties.group.id' = 'lab-printer',\n"
            + "  'scan.startup.mode' = 'earliest-offset',\n"
            + "  'format' = 'json',\n"
            + "  'json.ignore-parse-errors' = 'true'\n"
            + ")";
    }
}
