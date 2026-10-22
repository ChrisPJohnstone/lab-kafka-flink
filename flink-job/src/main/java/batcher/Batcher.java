package batcher;

import java.time.Duration;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.util.CloseableIterator;

public final class Batcher {
    private static final String BOOTSTRAP_SERVERS = "kafka:9094";
    private static final String TOPIC = "lab";
    private static final String GROUP_ID = "lab-printer";
    private static final String OUTPUT_PATH = "file:///output";

    public static void main(String[] args) throws Exception {
        KafkaSource<String> source = KafkaSource.<String>builder()
            .setBootstrapServers(BOOTSTRAP_SERVERS)
            .setTopics(TOPIC)
            .setGroupId(GROUP_ID)
            .setStartingOffsets(OffsetsInitializer.earliest())
            .setValueOnlyDeserializer(new SimpleStringSchema())
            .build();
        FileSink<String> sink = FileSink
            .forRowFormat(new Path(OUTPUT_PATH), new SimpleStringEncoder<String>("UTF-8"))
            .withRollingPolicy(
                DefaultRollingPolicy.builder()
                    .withRolloverInterval(Duration.ofMinutes(15))
                    .withInactivityInterval(Duration.ofMinutes(5))
                    .withMaxPartSize(MemorySize.ofMebiBytes(128))
                    .build()
            )
            .build();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(Duration.ofSeconds(30).toMillis());
        env.fromSource(source, WatermarkStrategy.noWatermarks(), "lab-kafka").sinkTo(sink);
        env.execute("batcher");
    }
}
