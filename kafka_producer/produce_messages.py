from confluent_kafka import Producer
import time

TOPIC_NAME = "input-topic"  

config = {
    "bootstrap.servers": "localhost:9092",  
}

producer = Producer(config)

# Small script to send messages to Kafka
def send_data():
    try:
        print("Starting to send messages to Kafka...")
        for i in range(1000000000):  
            message = {"value": f"This is message {i}"}
            producer.produce(topic=TOPIC_NAME, value=message["value"])
            producer.flush()
            print(f"Message {i} sent: {message}")
            time.sleep(0.1)  
        print("Finished sending messages.")

    except Exception as e:
        print(f"Error sending message: {e}")

    finally:
        producer.close()
        print("Kafka producer closed.")


if __name__ == "__main__":
    send_data()
