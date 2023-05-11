package com.swang.lab;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map;

public class GsonTraversalExample {
    public static void main(String[] args) {
        String jsonString = "{\n" +
                "  \"name\": \"John\",\n" +
                "  \"age\": 30,\n" +
                "  \"city\": \"New York\",\n" +
                "  \"address\": {\n" +
                "    \"street\": \"123 Main St\",\n" +
                "    \"zip\": \"12345\"\n" +
                "  },\n" +
                "  \"phone\": [\n" +
                "    {\n" +
                "      \"type\": \"home\",\n" +
                "      \"number\": \"555-1234\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"work\",\n" +
                "      \"number\": \"555-5678\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();

        for (Map.Entry<String, com.google.gson.JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            com.google.gson.JsonElement value = entry.getValue();

            System.out.print(key + " : ");

            if (value.isJsonPrimitive()) {
                System.out.println(value.getAsString());
            } else if (value.isJsonObject()) {
                System.out.println("{");
                for (Map.Entry<String, com.google.gson.JsonElement> nestedEntry : value.getAsJsonObject().entrySet()) {
                    String nestedKey = nestedEntry.getKey();
                    com.google.gson.JsonElement nestedValue = nestedEntry.getValue();

                    System.out.println("  " + nestedKey + " : " + nestedValue);
                }
                System.out.println("}");
            } else if (value.isJsonArray()) {
                System.out.println("[");
                for (com.google.gson.JsonElement arrayElement : value.getAsJsonArray()) {
                    System.out.println("  {");
                    for (Map.Entry<String, com.google.gson.JsonElement> nestedEntry : arrayElement.getAsJsonObject().entrySet()) {
                        String nestedKey = nestedEntry.getKey();
                        com.google.gson.JsonElement nestedValue = nestedEntry.getValue();

                        System.out.println("    " + nestedKey + " : " + nestedValue);
                    }
                    System.out.println("  }");
                }
                System.out.println("]");
            }
        }
    }
}

