FROM openjdk:8

RUN java -version

RUN apt-get update -y && apt-get install maven -y

RUN apt-get update -y && apt-get install git

WORKDIR /usr/src

RUN git clone https://github.com/xprt64/cqrs-java.git &&\
	cd cqrs-java && \
	mvn clean install && \
	cd ..

RUN git clone https://github.com/xprt64/cqrs-java-sql-event-store.git &&\
	cd cqrs-java-sql-event-store && \
	mvn clean install && \
	cd ..

RUN git clone https://github.com/xprt64/cqrs-java-p1.git &&\
	cd cqrs-java-p1 && \
	mvn clean package

# RUN java -jar /usr/src/cqrs-java-p1/target/p1-1.0-SNAPSHOT.jar

CMD ["java", "-jar", "/usr/src/cqrs-java-p1/target/p1-1.0-SNAPSHOT.jar"]