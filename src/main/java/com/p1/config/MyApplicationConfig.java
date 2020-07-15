package com.p1.config;

import com.cqrs.aggregates.EventSourcedAggregateRepository;
import com.cqrs.commands.*;
import com.cqrs.events.*;
import com.cqrs.events.ErrorReporter;
import com.cqrs.infrastructure.AbstractFactory;
import com.cqrs.questions.*;
import com.cqrs.sql_event_store.SqlEventStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
@ComponentScan("com.p1")
public class MyApplicationConfig {

    @Value("${P1_DB_URL}")
    private String EVENTSTORE_DB_URL;

    @Value("${P1_DB_PW}")
    private String EVENTSTORE_DB_PW;

    @Value("${P1_DB_USER}")
    private String EVENTSTORE_DB_USER;

    final ApplicationContext applicationContext;

    public MyApplicationConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    CommandDispatcher commandDispatcher(SqlEventStore eventStore) {
        return new CommandDispatcherWithValidator(
            new DefaultCommandDispatcher(
                new CommandSubscriberByMap(),
                new CommandApplier(),
                new EventSourcedAggregateRepository(
                    eventStore
                ),
                new DefaultSideEffectsDispatcher(
                    new CompositeEventDispatcher(
                        new EventDispatcherBySubscriber(
                            new EventSubscriberByMap(
                                new AnnotatedReadModelEventHandlersMap(),
                                applicationContext::getBean,
                                errorReporter()
                            )
                        ),
                        new EventDispatcherBySubscriber(
                            new EventSubscriberByMap(
                                new AnnotatedOnceEventHandlersMap(),
                                applicationContext::getBean,
                                errorReporter()
                            )
                        )
                    )
                )
            ),
            new CommandValidatorBySubscriber(
                new CommandValidatorSubscriberByMap(
                    applicationContext::getBean,
                    new AnnotatedCommandValidatorsMap()
                )
            )
        );
    }

    @Bean
    SqlEventStore eventStore() {
        System.out.println(String.format("Connecting to %s", EVENTSTORE_DB_URL));
        try {
            return new SqlEventStore(
                DriverManager.getConnection(
                    EVENTSTORE_DB_URL, EVENTSTORE_DB_USER, EVENTSTORE_DB_PW),
                "events_p1"
            );
        } catch (SQLException throwables) {
            System.out.println(String.format("Event store connection error: %s", throwables.getMessage()));
            System.exit(1);
            return null;
        }
    }

    private ErrorReporter errorReporter() {
        return new ErrorReporter() {
            @Override
            public void reportEventDispatchError(
                Object eventListener,
                String listenerClass,
                String methodName,
                EventWithMetaData eventWithMetaData,
                Throwable throwable
            ) {
                System.out.println(
                    "Error dispatching event " +
                    eventWithMetaData.event.getClass().getCanonicalName() + " to " +
                    listenerClass + "." + methodName + " " + throwable.getClass().toString() + " " +
                    throwable.getMessage()
                );
                throwable.printStackTrace();
            }
        };
    }

    @Bean
    Asker asker(){
        return new DefaultAsker(
            applicationContext::getBean,
            new AnswererResolverByMap(
                new AnnotatedQuestionAnswerersMap()
            ),
            new SubscriberResolverByMap(
                new AnnotatedQuestionSubscribersMap()
            )
        );
    }

    @Bean
    QuestionPublisher questionPublisher(){
        return new DefaultQuestionPublisher(
            new SubscriberResolverByMap(
                new AnnotatedQuestionSubscribersMap()
            ),
            applicationContext::getBean,
            (listenerInstance, listenerClass, methodName, question, throwable) -> {
                System.out.println(
                    "Error publishing question " +
                        question.getClass().getCanonicalName() + " to " +
                        listenerClass + "." + methodName + " " + throwable.getClass().toString() + " " +
                        throwable.getMessage()
                );
                throwable.printStackTrace();
            }
        );
    }
}
