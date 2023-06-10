package com.swang.lab;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;

import java.io.IOException;

public class LogEventExample0 {
    public static void main(String[] args) throws IOException {
        String json = "{\n" +
                "  \"dt\": 1623165600,\n" +
                "  \"timestamp\": 1623165600,\n" +
                "  \"source\": {\n" +
                "    \"name\": \"MyApplication\",\n" +
                "    \"version\": null,\n" +
                "    \"owner\": {\"string\":\"John Doe\"}\n" +
                "  },\n" +
                "  \"context\": {\n" +
                "    \"key1\": \"value1\",\n" +
                "    \"key2\": \"value2\"\n" +
                "  },\n" +
                "  \"level\": \"INFO\",\n" +
                "  \"message\": \"Application started\"\n" +
                "}\n";
        Schema schema = new Parser().parse("{\n" +
                "  \"doc\": \"Generic application log event\",\n" +
                "  \"fields\": [\n" +
                "    {\n" +
                "      \"doc\": \"Timestamp of the event\",\n" +
                "      \"name\": \"timestamp\",\n" +
                "      \"type\": \"long\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"doc\": \"The application that sent the event\",\n" +
                "      \"name\": \"source\",\n" +
                "      \"type\": {\n" +
                "        \"doc\": \"Identification of an application\",\n" +
                "        \"fields\": [\n" +
                "          {\n" +
                "            \"doc\": \"The name of the application\",\n" +
                "            \"name\": \"name\",\n" +
                "            \"type\": \"string\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"default\": null,\n" +
                "            \"doc\": \"(Optional) The application version\",\n" +
                "            \"name\": \"version\",\n" +
                "            \"type\": [\n" +
                "              \"null\",\n" +
                "              \"string\"\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"default\": null,\n" +
                "            \"doc\": \"The owner of the application\",\n" +
                "            \"name\": \"owner\",\n" +
                "            \"type\": [\n" +
                "              \"null\",\n" +
                "              \"string\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        \"name\": \"Application\",\n" +
                "        \"type\": \"record\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"doc\": \"The application context, contains application-specific key-value pairs\",\n" +
                "      \"name\": \"context\",\n" +
                "      \"type\": {\n" +
                "        \"type\": \"map\",\n" +
                "        \"values\": \"string\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"doc\": \"The log level, being either DEBUG, INFO, WARN or ERROR\",\n" +
                "      \"name\": \"level\",\n" +
                "      \"type\": {\n" +
                "        \"doc\": \"The level of the log message\",\n" +
                "        \"name\": \"ApplicationLogLevel\",\n" +
                "        \"symbols\": [\n" +
                "          \"DEBUG\",\n" +
                "          \"INFO\",\n" +
                "          \"WARN\",\n" +
                "          \"ERROR\",\n" +
                "          \"FATAL\"\n" +
                "        ],\n" +
                "        \"type\": \"enum\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"doc\": \"The log message\",\n" +
                "      \"name\": \"message\",\n" +
                "      \"type\": \"string\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"name\": \"ApplicationLogEvent\",\n" +
                "  \"namespace\": \"io.axual.client.example.schema\",\n" +
                "  \"type\": \"record\"\n" +
                "}");

        GenericRecord data = getGenericRecord(schema, json);

        System.out.println(data);
    }

    private static GenericRecord getGenericRecord(Schema schema, String jsonString) throws IOException {
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
        Decoder decoder = DecoderFactory.get().jsonDecoder(schema, jsonString);
        return datumReader.read(null, decoder);
    }
}

