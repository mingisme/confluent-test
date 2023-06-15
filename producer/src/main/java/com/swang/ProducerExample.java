package com.swang;

import io.confluent.examples.clients.basicavro.Payment;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ProducerExample {

    private static final String TOPIC = "transactions";
    private static final Properties props = new Properties();
    private static String configFile;

    public static void main(String[] args) {
        if (args.length < 2) {
            args = new String[]{"localhost:9092", "http://localhost:8081"};
        }

        String kafkaServer = args[0];
        String schemaRegistry = args[1];

        if (args.length == 2) {
            //Non TLS
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        } else {
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
        }

        //Avro registry
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);

        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);

        try (KafkaProducer<String, Payment> producer = new KafkaProducer<String, Payment>(props)) {

            for (long i = 0; i < 10; i++) {
                final String orderId = "id" + Long.toString(i);
                final Payment payment = new Payment(orderId, 1000.00d);
                final ProducerRecord<String, Payment> record = new ProducerRecord<String, Payment>(TOPIC, payment.getId().toString(), payment);
                Future<RecordMetadata> send = producer.send(record);
                try {
                    RecordMetadata recordMetadata = send.get(5, TimeUnit.SECONDS);
                    System.out.println("ACK, timestamp:"+recordMetadata.timestamp()+", topic:" + recordMetadata.topic() + ", offset:" + recordMetadata.offset() + ", partition:" + recordMetadata.partition());
                } catch (ExecutionException | TimeoutException e) {
                    throw new RuntimeException(e);
                }
                System.out.printf("Successfully produce 1 message to topic called %s%n", TOPIC);
                Thread.sleep(1000L);
            }

            producer.flush();
            System.out.printf("Successfully produced 10 messages to a topic called %s%n", TOPIC);

        } catch (final SerializationException | InterruptedException e) {
            e.printStackTrace();
        }

    }

}
