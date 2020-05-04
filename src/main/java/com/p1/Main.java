package com.p1;

import com.p1.config.MyApplicationConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) throws Exception {

        ApplicationContext ctx = new AnnotationConfigApplicationContext(MyApplicationConfig.class);
        ctx.getBean(MyApp.class).run();
    }

}
