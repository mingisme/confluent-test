package example;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerExample {

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        if (args.length < 4) {
            throw new RuntimeException("Please provide Kafka server, schema registry, topic, and group id");
        }
        String kafkaServer = args[0];
        String schemaRegistry = args[1];
        String topic = args[2];
        String groupId = args[3];
        final Properties props = new Properties();
        //TLS
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put("security.protocol", "SSL");
        props.put("ssl.keystore.type", "pkcs12");
        props.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "./kafka.keystore.pfx");
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "test12345");
        props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "./kafka.keystore.pfx");
        props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "test12345");
        props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, "test12345");

        //Avro registry
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);

        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, false);

        try (final KafkaConsumer<String, Object> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList(topic));

            while (true) {
                final ConsumerRecords<String, Object> records = consumer.poll(Duration.ofMillis(100));
                for (final ConsumerRecord<String, Object> record : records) {
                    final String key = record.key();
                    final Object value = record.value();
                    System.out.printf("key = %s, value = %s%n", key, value.toString());
                }
            }
        }
    }
}
