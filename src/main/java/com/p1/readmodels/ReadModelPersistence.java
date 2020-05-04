package com.p1.readmodels;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class ReadModelPersistence {
    private final HashMap<String, List<String>> tables = new HashMap<>();

    public void insert(String tableName, String item){
        List<String> table = tables.getOrDefault(tableName, new ArrayList<String>());
        table.add(item);
        tables.put(tableName, table);
    }

    public List<String> load(String tableName){
        return tables.getOrDefault(tableName, new ArrayList<>());
    }
}
