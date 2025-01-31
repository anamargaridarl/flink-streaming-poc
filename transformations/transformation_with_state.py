from pyflink.common import WatermarkStrategy, Row
from pyflink.common.serialization import Encoder
from pyflink.common.typeinfo import Types
from pyflink.datastream import StreamExecutionEnvironment
from pyflink.datastream.connectors.file_system import FileSink, OutputFileConfig
from pyflink.datastream.connectors.number_seq import NumberSequenceSource
from pyflink.datastream.functions import RuntimeContext, MapFunction
from pyflink.datastream.state import ValueStateDescriptor


class MapIsEven(MapFunction):
    cnt_state = None

    #initialize state
    def open(self, runtime_context: RuntimeContext):
        state_desc = ValueStateDescriptor('cnt', Types.LONG())
        self.cnt_state = runtime_context.get_state(state_desc)

    def map(self, value: Row):
        cnt = self.cnt_state.value()
        if value[0] % 2 == 0:
            self.cnt_state.update(1 if cnt is None else cnt + 1)
            return value[0], value[1] + 1
        else:
            return value[0], value[1]


def transformation_with_state():
    env = StreamExecutionEnvironment.get_execution_environment()

    seq_num_source = NumberSequenceSource(1, 10000)
    ds = env.from_source(
        source=seq_num_source,
        watermark_strategy=WatermarkStrategy.no_watermarks(),
        source_name='seq_num_source',
        type_info=Types.LONG())

    ds = ds.map(lambda a: Row(a, 1), output_type=Types.ROW([Types.LONG(), Types.LONG()])) \
           .key_by(lambda a: a[0]) \
           .map(MapIsEven(), output_type=Types.TUPLE([Types.LONG(), Types.LONG()]))

    output_path = './outputs'
    # File will be in the TaskManager's working directory
    file_sink = FileSink \
        .for_row_format(output_path, Encoder.simple_string_encoder()) \
        .with_output_file_config(OutputFileConfig.builder().build()) \
        .build()
    ds.sink_to(file_sink)

    env.execute('transformation_with_state')


if __name__ == '__main__':
    transformation_with_state()