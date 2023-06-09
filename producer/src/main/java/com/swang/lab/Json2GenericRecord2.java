package com.swang.lab;

import com.google.gson.Gson;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Json2GenericRecord2 {
    public static void main(String[] args) throws IOException {
        String json = "{\"name\":\"John Doe\",\"age\":30,\"addresses\":[{\"street\":\"123 Main St\",\"city\":\"New York\",\"state\":\"NY\"},{\"street\":\"456 Elm St\",\"city\":\"San Francisco\",\"state\":\"CA\"}]}";
        Schema schema = new Parser().parse("{\n" +
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


