package examples;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.formats.avro.AvroDeserializationSchema;
import org.apache.flink.formats.avro.AvroSerializationSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class AvroSerialization {

  static final String inputTopic = "input-topic-2";
  static final String outputTopic = "output-topic-2";
  static final String jobTitle = "examples.ExampleD";

  public void transformStringKafka(String[] args) throws Exception {
    final String bootstrapServers = args.length > 0 ? args[0] : "localhost:29092";

    // Set up the streaming execution environment
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    KafkaSource<Transaction> source =
        KafkaSource.<Transaction>builder()
            .setBootstrapServers(bootstrapServers)
            .setTopics(inputTopic)
            .setGroupId("test_group")
            .setStartingOffsets(OffsetsInitializer.earliest())
            .setValueOnlyDeserializer(AvroDeserializationSchema.forSpecific(Transaction.class))
            .build();

    SerializationSchema<TransactionWithStatus> serializationSchema =
        AvroSerializationSchema.forSpecific(TransactionWithStatus.class);

    KafkaRecordSerializationSchema<TransactionWithStatus> serializer =
        KafkaRecordSerializationSchema.builder()
            .setValueSerializationSchema(serializationSchema)
            .setTopic(outputTopic)
            .build();

    KafkaSink<TransactionWithStatus> sink =
        KafkaSink.<TransactionWithStatus>builder()
            .setBootstrapServers(bootstrapServers)
            .setRecordSerializer(serializer)
            .build();

    DataStream<Transaction> transactions =
        env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");

    DataStream<TransactionWithStatus> processedTransactions =
        transactions
            .keyBy(value -> value)
            .map(
                (transaction) -> {
                  return new TransactionWithStatus(
                      transaction.getValue(), transaction.getMerchantID(), true);
                });

    processedTransactions.sinkTo(sink);

    // Execute program
    env.execute(jobTitle);
  }
}
