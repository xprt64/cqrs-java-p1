package com.p1;

import com.cqrs.sql_event_store.SqlEventStore;
import com.p1.config.MyApplicationConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) throws Exception {

        ApplicationContext ctx = new AnnotationConfigApplicationContext(MyApplicationConfig.class);
        ctx.getBean(SqlEventStore.class).dropStore();
        ctx.getBean(SqlEventStore.class).createStore();
        ctx.getBean(MyApp.class).run();
    }

}
