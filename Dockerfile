FROM maven:3.8.2-jdk-8
WORKDIR /app
COPY . .
RUN ["mvn", "install", "-Dmaven.test.skip=true"]
CMD mvn spring-boot:run