package com.swang.lab;

import com.google.gson.Gson;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.Schema.Parser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonToRecordExample1 {
    public static void main(String[] args) {
        String json = "{\"name\":\"John Doe\",\"age\":30,\"address\":{\"street\":\"123 Main St\",\"city\":\"New York\",\"state\":\"NY\"},\"phone_numbers\":[\"555-1234\",\"555-5678\"]}";
        Schema schema = new Parser().parse("{\"type\":\"record\",\"name\":\"Person\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"age\",\"type\":\"int\"},{\"name\":\"address\",\"type\":{\"type\":\"record\",\"name\":\"Address\",\"fields\":[{\"name\":\"street\",\"type\":\"string\"},{\"name\":\"city\",\"type\":\"string\"},{\"name\":\"state\",\"type\":\"string\"}]}},{\"name\":\"phone_numbers\",\"type\":{\"type\":\"array\",\"items\":\"string\"}}]}");

        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(json, Map.class);

        GenericRecord record = new GenericData.Record(schema);
        record.put("name", map.get("name"));
        record.put("age", map.get("age"));

        Map<String, Object> addressMap = (Map<String, Object>) map.get("address");
        GenericRecord addressRecord = new GenericData.Record(schema.getField("address").schema());
        addressRecord.put("street", addressMap.get("street"));
        addressRecord.put("city", addressMap.get("city"));
        addressRecord.put("state", addressMap.get("state"));
        record.put("address", addressRecord);

        List<String> phoneNumbers = (ArrayList<String>) map.get("phone_numbers");
        record.put("phone_numbers", phoneNumbers);

        System.out.println(record);
    }
}

