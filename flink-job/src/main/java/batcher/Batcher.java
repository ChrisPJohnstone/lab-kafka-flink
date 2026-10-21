package batcher;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.CloseableIterator;

public final class Batcher {
    private static final String BOOTSTRAP_SERVERS = "kafka:9094";
    private static final String TOPIC = "lab";
    private static final String GROUP_ID = "lab-printer";

    public static void main(String[] args) throws Exception {
        KafkaSource<String> source = KafkaSource.<String>builder()
            .setBootstrapServers(BOOTSTRAP_SERVERS)
            .setTopics(TOPIC)
            .setGroupId(GROUP_ID)
            .setStartingOffsets(OffsetsInitializer.earliest())
            .setValueOnlyDeserializer(new SimpleStringSchema())
            .build();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        try (CloseableIterator<String> records =
                 env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source")
                    .executeAndCollect()) {
            records.forEachRemaining(System.out::println);
        }
        // TODO: Batch & output to file
    }
}
