package com.example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;




public class FileLoader {

    public List<OrderData> loadFile(String fileName) throws Exception {
        if (fileName.endsWith(".csv")) return loadCSV(fileName);
        if (fileName.endsWith(".xml")) return loadXML(fileName);
        if (fileName.endsWith(".json")) return loadJSON(fileName);
        throw new IllegalArgumentException("Filformatet stöds inte.");
    }
    //load csv
    private List<OrderData> loadCSV(String fileName) throws Exception {
        List<OrderData> data = new ArrayList<>();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            data = reader.lines()
                         .skip(1)
                         .map(line -> {
                             String[] fields = line.split(",");
                             
                             String orderDate = fields.length > 0 ? fields[0].replace("\"", "") : "";
                             String region = fields.length > 1 ? fields[1].replace("\"", "") : "";
                             String rep1 = fields.length > 2 ? fields[2].replace("\"", "") : "";
                             String rep2 = fields.length > 3 ? fields[3].replace("\"", "") : "";
                             String item = fields.length > 4 ? fields[4].replace("\"", "") : "";
                             int units = fields.length > 5 ? parseIntOrDefault(fields[5], 0) : 0;
                             double unitCost = fields.length > 6 ? parseDoubleOrDefault(fields[6], 0.0) : 0.0;
                             double total = fields.length > 7 ? parseDoubleOrDefault(fields[7], 0.0) : 0.0;
    
                             return new OrderData(orderDate, region, rep1, rep2, item, units, unitCost, total);
                         })
                         .collect(Collectors.toList());
        }
        return data;
    }

    //load json
    private List<OrderData> loadJSON(String fileName) throws Exception {
        List<OrderData> data = new ArrayList<>();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            
            Gson gson = new Gson();
            List<Map<String, String>> jsonObjects = gson.fromJson(reader, List.class);
    
            for (Map<String, String> jsonObject : jsonObjects) {
                try {
                    String orderDate = jsonObject.getOrDefault("A", "").replace("\"", "");
                    String region = jsonObject.getOrDefault("B", "").replace("\"", "");
                    String rep1 = jsonObject.getOrDefault("C", "").replace("\"", "");
                    String rep2 = jsonObject.getOrDefault("D", "").replace("\"", "");
                    String item = jsonObject.getOrDefault("E", "").replace("\"", "");
                    int units = parseIntOrDefault(jsonObject.getOrDefault("F", ""), 0);
                    double unitCost = parseDoubleOrDefault(jsonObject.getOrDefault("G", ""), 0.0);
                    double total = parseDoubleOrDefault(jsonObject.getOrDefault("H", ""), 0.0);  
    
                    OrderData orderData = new OrderData(orderDate, region, rep1, rep2, item, units, unitCost, total);
                    data.add(orderData);
                } catch (Exception e) {
                    System.out.println("Varning: Kunde inte läsa en rad i JSON. Fel: " + e.getMessage());
                }
            }
        }
        return data;
    }

     //load xml
     private List<OrderData> loadXML(String fileName) throws Exception {
        List<OrderData> data = new ArrayList<>();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            NodeList rows = doc.getElementsByTagName("row");
            for (int i = 0; i < rows.getLength(); i++) {
                NodeList fields = rows.item(i).getChildNodes();
                OrderData orderData = new OrderData(
                    getTextContentOrEmpty(fields, 1),
                    getTextContentOrEmpty(fields, 3),
                    getTextContentOrEmpty(fields, 5),
                    getTextContentOrEmpty(fields, 7),
                    getTextContentOrEmpty(fields, 9),
                    parseIntOrDefault(getTextContentOrEmpty(fields, 11), 0),
                    parseDoubleOrDefault(getTextContentOrEmpty(fields, 13), 0.0),
                    parseDoubleOrDefault(getTextContentOrEmpty(fields, 15), 0.0)
                );
                data.add(orderData);
            }
        }
        return data;
    }

    
    private double parseDouble(String value) {
        
        value = value.replace(",", "").replaceAll("\\.(?=.*\\.)", "");
        return Double.parseDouble(value);
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value.replace("\"", "").trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    private double parseDoubleOrDefault(String value, double defaultValue) {
    if (value == null || value.isEmpty()) {
        return defaultValue;  
    }
    try {
        return Double.parseDouble(value.replace(",", ""));
    } catch (NumberFormatException e) {
        return defaultValue;
    }
}

    private String getTextContentOrEmpty(NodeList fields, int index) {
        return fields.item(index) != null ? fields.item(index).getTextContent().trim() : "";
    }
    
}
