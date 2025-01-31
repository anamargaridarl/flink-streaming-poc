from pyflink.datastream import StreamExecutionEnvironment
from pyflink.datastream.connectors.kafka import (
    KafkaSource,
    KafkaSink,
    KafkaOffsetsInitializer,
    KafkaRecordSerializationSchema,DeliveryGuarantee
)
from pyflink.common.serialization import SimpleStringSchema
from pyflink.common import WatermarkStrategy

def kafka_simple_pipeline():

    env = StreamExecutionEnvironment.get_execution_environment()

    kafka_source = (
        KafkaSource.builder()
        .set_bootstrap_servers("localhost:9092")
        .set_group_id("test_group")
        .set_topics("input-topic")
        .set_value_only_deserializer(SimpleStringSchema())
        .set_starting_offsets(KafkaOffsetsInitializer.earliest())
        .build()
    )

    data_stream = env.from_source(kafka_source, watermark_strategy=WatermarkStrategy.no_watermarks(), source_name="KafkaSource")
    
    # Uppercase the messages
    processed_stream = data_stream.map(lambda x: x.upper())

    kafka_sink = (
        KafkaSink.builder()
        .set_bootstrap_servers("localhost:9092")
        .set_record_serializer(
            KafkaRecordSerializationSchema.builder()
            .set_topic("output-topic")
            .set_value_serialization_schema(SimpleStringSchema())
            .build()
        )
        .build()
    )

    processed_stream.sink_to(kafka_sink)

    env.execute("kafka_simple_pipeline")


if __name__ == "__main__":
    kafka_simple_pipeline()
