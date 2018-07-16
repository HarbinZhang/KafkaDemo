# KafkaDemo

## New Updates
works in the new Mac

## Content
### kafka-examples: Producers & Consumers  
Single thread producer and multi-consumers(consumer group).

### streams-starter-project
WordCount:
1. Stream from Kafka     < null, “Kafka Kafka Streams”>
2. MapValues lowercase.  < null, “ … “ >
3. FlatMapValues split by space. <null, “kafka”>, <....>
4. SelectKey to apply a key.  <“Kafka”,”Kafka”> …
5. GroupByKey before aggregation. (<><>) ()
6. Count occurrences in each group. <“Kafka”, 2>, <“streams”, 1>
7. To in order to write the results back to Kafka.
