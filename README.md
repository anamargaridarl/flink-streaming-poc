# flink-streaming-poc [STILL IN PROGRESS]

Some exploratory work on Flink data streaming. 

[Flink oficial documentation](https://nightlies.apache.org/flink/flink-docs-release-1.20/)

Java17 | Flink 1.20

Testing environment:
- Kafka 
- Kafka UI
- JobManager 
- TaskManager
- Runner [TODO] find better solution 

## How to run 

```
docker compose build
docker compose up
```

- Kafka UI is at <i>localhost:8080</i>. For reference on how to use it please go to [link](https://github.com/provectus/kafka-ui).

- Flink provides an UI at <i>localhost:8081</i>. You can check the sent jobs, their flow and resource usage, etc.

### Runner

The runner container makes use of the Flink CLI to send jobs to the Flink running environment. Your job's code should be in the ``transformations/`` folder.

In the docker-compose make sure that the runner ```command``` points to the code you want to run. To send a job start the container. It will stop once it finishes.

e.g. to run a pyflink transformation
```
command: ./bin/flink run -m jobmanager:8081 -py ./transformations/transformation_with_state.py
```
e.g. to run a java jar transformation
```
command: ./bin/flink run -m jobmanager:8081 -d ./transformations/quickstart-1.0.0.jar
```

Alternatively, you could run the CLI by acessing the JobManager container and running the command. However, similarly to the runner, you would have to add the volume with the transformations to the jobmanager container.

### Kafka

This testing environment includes a Kafka stack to connect Flink to Kafka. This allows you to read data from and to Kafka topics in Flink.

[TODO]: add schema registry to docker compose for future serialization

#### Kafka Produce

A script with a kafka producer so that you can feed data to the topics in the test environment. 


### Java Flink Project 

The java_flink_project has a project set to run in **Intelij** with **Gradle** to allow you to develop and test your Java transformations. After you can also generate a jar from the project and run it in the docker environment.