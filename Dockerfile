FROM flink:1.20-java17

RUN apt-get update && apt-get install -y python3 python3-pip \
    && ln -s /usr/bin/python3 /usr/bin/python

RUN wget -P /opt/flink/lib https://repo.maven.apache.org/maven2/org/apache/flink/flink-connector-kafka/3.4.0-1.20/flink-connector-kafka-3.4.0-1.20.jar
RUN wget -P /opt/flink/lib/ https://repo.maven.apache.org/maven2/org/apache/kafka/kafka-clients/3.4.0/kafka-clients-3.4.0.jar

RUN apt install -y openjdk-17-jdk
ARG JAVA_HOME=/usr/lib/jvm/java-17-openjdk-arm64
ENV FLINK_HOME=/opt/flink
ENV PATH=$FLINK_HOME/bin:$PATH

COPY ./requirements.txt requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

