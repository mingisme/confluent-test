package com.swang.lab;

import com.google.gson.Gson;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonToRecordExample2 {
    public static void main(String[] args) {
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

        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(json, Map.class);

        GenericRecord record = new GenericData.Record(schema);
        record.put("name", map.get("name"));
        record.put("age", map.get("age"));

        List<Map<String, Object>> addressMaps = (ArrayList<Map<String, Object>>) map.get("addresses");
        List<GenericRecord> addresses = new ArrayList<>();
        for (Map<String, Object> addressMap : addressMaps) {
            GenericRecord addressRecord = new GenericData.Record(schema.getField("addresses").schema().getElementType());
            addressRecord.put("street", addressMap.get("street"));
            addressRecord.put("city", addressMap.get("city"));
            addressRecord.put("state", addressMap.get("state"));
            addresses.add(addressRecord);
        }
        record.put("addresses", addresses);

        System.out.println(record);
    }
}


