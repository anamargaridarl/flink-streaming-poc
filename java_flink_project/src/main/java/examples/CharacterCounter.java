package examples;

import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class CharacterCounter {
  static final String jobTitle = "examples.ExampleC";

  public void run() throws Exception {

    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    DataStream<String> dataStream = env.fromElements("a", "b", "a", "b", "c");

    dataStream
        .map(
            value -> new Tuple2<String, Integer>(value, 1),
            TypeInformation.of(new TypeHint<Tuple2<String, Integer>>() {}))
        .keyBy(value -> value.f0, TypeInformation.of(new TypeHint<String>() {}))
        .reduce(
            (value1, value2) -> {
              return new Tuple2<String, Integer>(value1.f0, value1.f1 + value2.f1);
            })
        .print();

    env.execute(jobTitle);
  }
}
