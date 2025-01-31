package examples;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SimpleKafkaPipeline {

    final static String inputTopic = "input-topic";
    final static String outputTopic = "output-topic";
    final static String jobTitle = "KafkaTransform";

    public void transformStringKafka(String[] args) throws Exception {
        final String bootstrapServers = args.length > 0 ? args[0] : "kafka1:29092";

        // Set up the streaming execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers(bootstrapServers)
                .setTopics(inputTopic)
                .setGroupId("test_group")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        KafkaRecordSerializationSchema<String> serializer = KafkaRecordSerializationSchema.builder()
                .setValueSerializationSchema(new SimpleStringSchema())
                .setTopic(outputTopic)
                .build();

        KafkaSink<String> sink = KafkaSink.<String>builder()
                .setBootstrapServers(bootstrapServers)
                .setRecordSerializer(serializer)
                .build();

        DataStream<String> text = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");


        // Split up the lines in pairs (2-tuples) containing: (word,1)
        DataStream<String> counts = text.map((value -> value + "!!"));

        // Add the sink to so results
        // are written to the outputTopic
        counts.sinkTo(sink);

        // Execute program
        env.execute(jobTitle);

    }


}