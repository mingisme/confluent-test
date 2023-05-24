package com.swang;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.util.Properties;

public class ProducerGenericDataExample2 {

    private static final String TOPIC = "transactions";
    private static final Properties props = new Properties();
    private static String configFile;

    public static void main(String[] args) throws IOException {
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
            props.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");
            props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "./kafka.keystore.jks");
            props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "test12345");
            props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "./kafka.keystore.jks");
            props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "test12345");
            props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, "test12345");
        }

        //Avro registry
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);

        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);

        //option1: read schema from classpath
//        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("avro/io/confluent/examples/clients/basicavro/Payment.avsc");
//        Schema schema = new Schema.Parser().parse(inputStream);

        //option2: read schema from avro registry
        //schema is constructed by topic name and suffix
        String schemaName = TOPIC + "-value";
        //version can be a number or 'latest'
        String version = "1"; //"latest";
        //get schema with RESTful API invoke
        String schemaUrl = schemaRegistry + "/subjects/" + schemaName + "/versions/" + version;
        HttpGet request = new HttpGet(schemaUrl);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        //create Avro Schema with json string
        Gson gson = new Gson();
        JsonObject responseBodyAsJson = gson.fromJson(responseBody, JsonObject.class);
        String schemaAsString = gson.toJson(new JsonParser().parse(responseBodyAsJson.getAsJsonPrimitive("schema").getAsString()).getAsJsonObject());
        Schema schema = new Schema.Parser().parse(schemaAsString);

        try (KafkaProducer<String, GenericRecord> producer = new KafkaProducer<>(props)) {

            for (long i = 0; i < 10; i++) {

                String transactionAsString = "{\"id\": \"id" + i + "\", \"amount\": 1000.0}";
                GenericRecord data = null;
                //generate GenericRecord with schema and transactionAsString

                final ProducerRecord<String, GenericRecord> record = new ProducerRecord<>(TOPIC, data.get("id").toString(), data);
                producer.send(record);
                System.out.printf("Successfully produce 1 message to topic called %s%n", TOPIC);
                Thread.sleep(1000L);
            }

            producer.flush();
            System.out.printf("Successfully produced 10 messages to a topic called %s%n", TOPIC);

        } catch (final SerializationException e) {
            e.printStackTrace();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }


}
