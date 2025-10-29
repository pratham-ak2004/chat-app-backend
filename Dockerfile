# Use Java 21 base image (Debian-based)
FROM maven:3.9-eclipse-temurin-21

# Update image
RUN apt-get update

# Download and extract MongoDB manually
RUN mkdir -p /opt/mongodb && \
    curl -L -o /tmp/mongodb.tgz https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-3.4.7.tgz && \
    tar -xzf /tmp/mongodb.tgz -C /opt/mongodb --strip-components=1 && \
    rm /tmp/mongodb.tgz && \
    mkdir -p /data/db

# Add MongoDB binaries to PATH
ENV PATH="$PATH:/opt/mongodb/bin"

# Set environment variables for MongoDB
ENV MONGO_DB_PATH=/data/db \
    MONGO_INITDB_ROOT_USERNAME=admin \
    MONGO_INITDB_ROOT_PASSWORD=secret \
    MONGO_DB_NAME=appdb \
    MONGO_PORT=27017 \
    MONGO_DATABASE=appdb \
    MONGO_USER=admin \
    MONGO_CLUSTER=localhost \
    MONGO_PASSWORD=secret

# Create MongoDB data directory
RUN mkdir -p ${MONGO_DB_PATH}

# Copy project source code
WORKDIR /app
COPY . .

# Build the Spring Boot JAR using Maven
RUN mvn clean package -DskipTests

# Create entrypoint script
RUN echo '#!/bin/bash\n\
mongod --dbpath ${MONGO_DB_PATH} --bind_ip 127.0.0.1 &\n\
echo "Waiting for MongoDB to start..."\n\
sleep 5\n\
java -jar target/*.jar\n' > /start.sh && chmod +x /start.sh

# Expose only app port
EXPOSE 8080

ENTRYPOINT ["/bin/bash", "/start.sh"]
