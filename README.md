# flink-streaming-poc [STILL IN PROGRESS]

Some exploratory work on Flink data streaming.

Testing environment:
- Kafka 
- Kafka UI
- JobManager 
- TaskManager
- Runner - allows us to send jobs to Flink 

## How to run 

```
docker compose build
docker compose up
```

### Runner

The runner container allows you to send jobs to Flink with the transformations you defined. Your transformations should be in the transformations/ folder.

In the docker-compose make sure that the runner command points to the transformation you want to run. 

e.g. to run a pyflink transformation
```
command: ./bin/flink run -m jobmanager:8081 -py ./transformations/transformation_with_state.py
```
e.g. to run a java jar transformation
```
command: ./bin/flink run -m jobmanager:8081 -d ./transformations/quickstart-1.0.0.jar
```


### Kafka

This testing environment includes a Kafka stack to connect Flink to Kafka. This allows you to read data from and to Kafka topics in Flink.

[TODO]:add schema registry to docker compose

#### Kafka Produce

A script with a kafka producer so that you can feed data to the topics in the test environment. 


### Java Flink Project 

This project is set to run in Intelij to allow you to develop and test your Java transformations. After you can also generate a jar from the project and run it in the docker environment.