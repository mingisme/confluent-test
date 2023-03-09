package org.example;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerExample {

    private static final String TOPIC = "transactions";
    private static final Properties props = new Properties();
    private static String configFile;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(final String[] args) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        //non TLS
//          props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        //TLS
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");
        props.put("security.protocol", "SSL");
        props.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "/Users/ming.wang4/RD/confluent/ssl-ca/keystore-client/kafka.keystore.jks");
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "test12345");
        props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "/Users/ming.wang4/RD/confluent/ssl-ca/keystore-client/kafka.keystore.jks");
        props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "test12345");
        props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, "test12345");

        //Avro registry
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-payments");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, false);

        try (final KafkaConsumer<String, Object> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList(TOPIC));

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
