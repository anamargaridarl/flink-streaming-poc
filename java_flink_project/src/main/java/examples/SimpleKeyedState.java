package examples;

import examples.transformations.CountWindowAverage;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SimpleKeyedState {
    final static String jobTitle = "examples.ExampleB";
    public void run() throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Tuple2<Long,Long>> dataStream = env.fromElements( new Tuple2<Long,Long>(1L, 3L), new Tuple2<Long,Long>(1L, 5L), new Tuple2<Long,Long>(1L, 7L), new Tuple2<>(1L, 4L), new Tuple2<Long,Long>(1L, 2L));

        dataStream.keyBy(tuple -> tuple.f0, TypeInformation.of(new TypeHint<Long>() {
                }))
                .flatMap(new CountWindowAverage())
                .print();

        env.execute(jobTitle);
    }
}

