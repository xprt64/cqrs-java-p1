package com.p1.readmodels;

import javax.inject.Named;
import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

@Named
public class ReadModelPersistence<EntityType> {
    private final HashMap<String, HashMap<String, EntityType>> entitiesPerTable = new HashMap<>();

    public HashMap<String, EntityType> entities(String tableName){
        return entitiesPerTable.computeIfAbsent(tableName, k -> new HashMap<>());
    }

    public void save(String tableName, String entityId, EntityType item) {
        entities(tableName).put(entityId, item);
    }

    public void delete(String tableName, String entityId, EntityType item) {
        entities(tableName).remove(entityId);
    }

    public void mutateIfExists(String tableName, String entityId, Function<EntityType, EntityType> mutator) {
        EntityType oldEntity = load(tableName, entityId);
        if (oldEntity != null) {
            EntityType newEntity = mutator.apply(oldEntity);
            save(tableName, entityId, newEntity);
        }
    }

    public void mutate(String tableName, String entityId, Function<EntityType, EntityType> mutator, Supplier<EntityType> creator) {
        EntityType oldEntity = load(tableName, entityId);
        if (oldEntity == null) {
            oldEntity = creator.get();
        }
        EntityType newEntity = mutator.apply(oldEntity);
        save(tableName, entityId, newEntity);
    }

    public EntityType load(String tableName, String entityId) {
        return entities(tableName).get(entityId);
    }

    public HashMap<String, EntityType> loadAll(String tableName) {
        return entities(tableName);
    }
}
