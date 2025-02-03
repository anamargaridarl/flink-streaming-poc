# flink-streaming-poc [STILL IN PROGRESS]

This repository serves as a tutorial for first-time users of Apache Flink, providing a step-by-step guide on how to use Flink effectively. The focus is on real-time data streaming and its integration with Apache Kafka. Through practical examples, users will learn how to process, transform, and analyze streaming data using Flink.

[Flink oficial documentation](https://nightlies.apache.org/flink/flink-docs-release-1.20/)

Java17 | Flink 1.20

Testing environment:
- Kafka 
- Kafka UI
- JobManager 
- TaskManager
- Runner

## How to run 

```
docker compose build
docker compose up
```

- Kafka UI is at <i>localhost:8080</i>. For reference on how to use it please go to [link](https://github.com/provectus/kafka-ui).

- Flink provides an UI at <i>localhost:8081</i>. You can check the sent jobs, their flow and resource usage, etc.

### Runner

We define a separate runner container to submit jobs without interfering with the Flink cluster. The Flink cluster consists of a JobManager, responsible for job scheduling and coordination, and TaskManagers, which handle execution. This separation allows for greater flexibility in configuring job submission strategies, retry logic, and dependencies within the runner container.

The runner container uses the Flink CLI to submit jobs. To execute a job, ensure that your code is placed inside the transformations/ directory. In your Docker Compose configuration, set the command field to the path of the job you want to run. The runner container will automatically stop once the job completes.

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

[TODO]: add schema registry to docker compose for future serialization

### Kafka Producer

A script with a kafka producer so that you can feed data to the topics in the test environment. 


### Java Flink Project 

The java_flink_project has a project set to run in **Intelij** with **Gradle** to allow you to develop and test your Java transformations. 

To run the code in the docker environment you can generate a jar from the project. To do this build the project with Gradle and copy the jar file at ```build/libs``` to the ```transformations``` folder. Don't forget to point the runner to the file. For more details on this check the Annex section.



## Annex [TODO]

### How to configure Intelij to run the project

