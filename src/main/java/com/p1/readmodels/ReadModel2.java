package com.p1.readmodels;

import com.cqrs.annotations.EventHandler;
import com.cqrs.events.MetaData;
import com.p1.myaggregate1.events.MyEvent1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReadModel2 {

    public static final String TABLE_NAME = "ReadModel2";

    private final ReadModelPersistence persistence;

    @Autowired
    public ReadModel2(ReadModelPersistence persistence) {
        this.persistence = persistence;
    }

    @EventHandler
    void on2_1_v6(MyEvent1 event, MetaData metaData){
        System.out.println(this.getClass().getCanonicalName() + ".on(" + event +")");
        persistence.insert(TABLE_NAME, event.aggregateId);
    }

    public List<String> loadAll(){
        return persistence.load(TABLE_NAME);
    }
}
