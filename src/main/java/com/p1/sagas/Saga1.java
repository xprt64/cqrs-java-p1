package com.p1.sagas;

import com.cqrs.annotations.OnceEventHandler;
import com.cqrs.events.MetaData;
import com.p1.myaggregate1.events.MyEvent1;
import org.springframework.stereotype.Component;

@Component
public class Saga1 {


    @OnceEventHandler
    void once(MyEvent1 event, MetaData metaData) throws Exception {
        System.out.println(this.getClass().getCanonicalName() + ".once(" + event +")");
    }
}
