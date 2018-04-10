import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Arrays;
import java.util.Properties;

public class StreamStarterApp {
    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-starter-app");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());


        StreamsBuilder builder = new StreamsBuilder();
//    1. Stream from Kafka     < null, “Kafka Kafka Streams”>
         KStream<String, String> wordCountInput = builder.stream("word-count-input");
//    2. MapValues lowercase.  < null, “ … “ >
        KTable<String, Long> wordCounts = wordCountInput.mapValues(value -> value.toLowerCase())
//    3. FlatMapValues split by space. <null, “kafka”>, <....>
        .flatMapValues(value -> Arrays.asList(value.split(" ")))
//    4. SelectKey to apply a key.  <“Kafka”,”Kafka”> …
        .selectKey(((key, value) -> value))
//    5. GroupByKey before aggregation. (<><>) ()
        .groupByKey()
//    6. Count occurrences in each group. <“Kafka”, 2>, <“streams”, 1>
        .count();
//    7. To in order to write the results back to Kafka.

        wordCounts.to(Serdes.String(), Serdes.Long(), "word-count-output");

        KafkaStreams streams = new KafkaStreams(builder.build(), properties);
        streams.start();

        System.out.println(streams.localThreadsMetadata());


        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
