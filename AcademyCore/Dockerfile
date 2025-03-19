# Step 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 as build

RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

COPY --chown=spring:spring pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -B

COPY --chown=spring:spring src ./src
RUN mvn package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Step 2: Run the application
FROM eclipse-temurin:21-jre-alpine as runtime

RUN addgroup -S spring && adduser -S spring -G spring

VOLUME /tmp

COPY --chown=spring:spring --from=build /app/target/dependency/BOOT-INF/lib /app/lib
COPY --chown=spring:spring --from=build /app/target/dependency/META-INF /app/META-INF
COPY --chown=spring:spring --from=build /app/target/dependency/BOOT-INF/classes /app

EXPOSE 8080

USER spring:spring

ENV JVM_OPTS=""

CMD ["sh", "-c", "java $JVM_OPTS -cp app:app/lib/* io.github.wesleyosantos91.AcademyCoreApplication"]

