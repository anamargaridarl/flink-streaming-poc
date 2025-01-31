package examples;

import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SimpleKeyBy {
    final static String jobTitle = "examples.ExampleA";
    public void run() throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Tuple2<Long,Long>> dataStream = env.fromElements( new Tuple2<Long,Long>(2L, 3L), new Tuple2<Long,Long>(1L, 5L), new Tuple2<Long,Long>(1L, 7L), new Tuple2<>(1L, 4L), new Tuple2<Long,Long>(1L, 2L));

        dataStream.keyBy(tuple -> tuple.f0, TypeInformation.of(new TypeHint<Long>() {
                }))
                .map(tuple -> new Tuple2<Long,Long>(tuple.f0 + 1, tuple.f1+1),TypeInformation.of(new TypeHint<Tuple2<Long,Long>>() {
                })).print();

        env.execute(jobTitle);
    }
}

