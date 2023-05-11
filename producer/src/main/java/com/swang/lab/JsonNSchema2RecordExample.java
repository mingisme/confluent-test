package com.swang.lab;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;

import java.io.IOException;

public class JsonNSchema2RecordExample {
    public static void main(String[] args) throws IOException {
        test1();
        test2();
        test3();
    }

    private static void test3() throws IOException {
        // Define Avro schema as a string
        String json = "{\"name\":\"John Doe\",\"age\":30,\"address\":{\"street\":\"123 Main St\",\"city\":\"New York\",\"state\":\"NY\"},\"phone_numbers\":[\"555-1234\",\"555-5678\"]}";
        String schema = "{\"type\":\"record\",\"name\":\"Person\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"age\",\"type\":\"int\"},{\"name\":\"address\",\"type\":{\"type\":\"record\",\"name\":\"Address\",\"fields\":[{\"name\":\"street\",\"type\":\"string\"},{\"name\":\"city\",\"type\":\"string\"},{\"name\":\"state\",\"type\":\"string\"}]}},{\"name\":\"phone_numbers\",\"type\":{\"type\":\"array\",\"items\":\"string\"}}]}";
        GenericRecord record = getGenericRecord(schema, json);
        System.out.println(record);
    }

    private static void test2() throws IOException {
        // Define Avro schema as a string
        String json = "{\"name\":\"John Doe\",\"age\":30,\"addresses\":[{\"street\":\"123 Main St\",\"city\":\"New York\",\"state\":\"NY\"},{\"street\":\"456 Elm St\",\"city\":\"San Francisco\",\"state\":\"CA\"}]}";
        String schema = "{\n" +
                "  \"type\": \"record\",\n" +
                "  \"name\": \"Person\",\n" +
                "  \"fields\": [\n" +
                "    {\n" +
                "      \"name\": \"name\",\n" +
                "      \"type\": \"string\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"age\",\n" +
                "      \"type\": \"int\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"addresses\",\n" +
                "      \"type\": {\n" +
                "        \"type\": \"array\",\n" +
                "        \"items\": {\n" +
                "          \"type\": \"record\",\n" +
                "          \"name\": \"Address\",\n" +
                "          \"fields\": [\n" +
                "            {\n" +
                "              \"name\": \"street\",\n" +
                "              \"type\": \"string\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"city\",\n" +
                "              \"type\": \"string\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \"state\",\n" +
                "              \"type\": \"string\"\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        GenericRecord record = getGenericRecord(schema, json);
        System.out.println(record);
    }

    private static void test1() throws IOException {
        // Define Avro schema as a string
        String schema = "{\n" +
                "  \"type\": \"record\",\n" +
                "  \"name\": \"Person\",\n" +
                "  \"fields\": [\n" +
                "    {\"name\": \"name\", \"type\": \"string\"},\n" +
                "    {\"name\": \"age\", \"type\": \"int\"}\n" +
                "  ]\n" +
                "}";

        // Create a JSON string
        String json = "{\"name\":\"John Doe\",\"age\":30}";

        GenericRecord record = getGenericRecord(schema, json);
        System.out.println(record);
    }

    private static GenericRecord getGenericRecord(String schemaString, String jsonString) throws IOException {
        // Create Avro schema from schema string
        Schema schema = new Schema.Parser().parse(schemaString);

        // Decode the JSON string into a GenericRecord using the Avro library
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
        Decoder decoder = DecoderFactory.get().jsonDecoder(schema, jsonString);
        GenericRecord record;
        record = datumReader.read(null, decoder);
        return record;
    }
}

