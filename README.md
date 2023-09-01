# CQRS Example

This readme provides information on how to set up and run the **CQRS Example** project, which consists of two services: **Product Command Service** and **Product Query Service**, along with required infrastructure components like **Kafka** and **PostgreSQL** using Docker Compose.

## CQRS (Command Query Responsibility Segregation)

**CQRS**, which stands for **Command Query Responsibility Segregation**, is an architectural pattern used in microservices and distributed systems. It separates the responsibilities for handling commands (changing state) and queries (retrieving state) into distinct parts of the application. This segregation can lead to a more scalable and efficient system, as each part can be optimized for its specific task.

In CQRS:

- **Command side** handles operations that modify data. These commands are typically imperative and can include actions like creating, updating, or deleting records. The Command side is responsible for enforcing business rules and validating input data.

- **Query side** handles read operations, such as retrieving data for display or reporting purposes. The Query side is often optimized for efficient data retrieval and can use different data models or caching mechanisms to improve performance.

### Using CQRS in this Microservices Example

In the provided microservices example, CQRS is applied by separating the responsibilities of handling commands and queries into two distinct services: **Product Command Service** and **Product Query Service**.

- **Product Command Service**:
    - This service is responsible for handling commands related to products. It receives requests to create, update, or delete products.
    - It interacts with the PostgreSQL database for storing and managing product data.
    - It uses Kafka as a message broker to publish product-related events, allowing for asynchronous communication and event-driven architecture.

- **Product Query Service**:
    - This service is responsible for handling queries related to products. It receives requests to retrieve product information.
    - It also interacts with the same PostgreSQL database to fetch product data.
    - It uses Kafka as a consumer to subscribe to and process product-related events published by the Command Service, allowing it to update its read-side data store for efficient querying.

By segregating the responsibilities in this way, the microservices can be independently scaled, optimized, and maintained. For example, the Command Service can handle a high volume of write operations while the Query Service can efficiently serve read requests, all while remaining loosely coupled through the Kafka message broker.

Overall, CQRS helps in achieving better separation of concerns and scalability in microservices architectures, making it a valuable pattern for building robust and efficient systems.

![img_14.png](screenshots%2Fimg_14.png)

![img_15.png](screenshots%2Fimg_15.png)

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Getting Started](#getting-started)
    - [Setting Up Infrastructure](#setting-up-infrastructure)
    - [Running the Services](#running-the-services)
3. [Project Details](#project-details)
    - [Product Command Service](#product-command-service)
    - [Product Query Service](#product-query-service)
4. [Docker Compose Files](#docker-compose-files)
5. [Additional Notes](#additional-notes)

## Prerequisites<a name="prerequisites"></a>
Before running the project, make sure you have the following prerequisites installed on your system:

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)
- Java Development Kit (JDK) 17 or higher

## Getting Started<a name="getting-started"></a>
Follow these steps to set up and run the project:

### Setting Up Infrastructure<a name="setting-up-infrastructure"></a>
1. Clone this repository to your local machine.

2. Navigate to the project directory:

   ```bash
   cd cqrs-ex1
   ```

3. Start the required infrastructure components (Kafka and PostgreSQL) using Docker Compose:

   ```bash
   docker-compose -f docker-compose/kafka.yml up -d
   docker-compose -f docker-compose/postgres.yml up -d
   ```
   
![img_2.png](screenshots%2Fimg_2.png)

![img_3.png](screenshots%2Fimg_3.png)

   This will start Kafka and PostgreSQL containers. Ensure that these containers are up and running before proceeding.

### Running the Services<a name="running-the-services"></a>
Now, you can run the **Product Command Service** and **Product Query Service**.

#### Product Command Service<a name="product-command-service"></a>
1. Run the `product-command-service` using your IDEA or:

   ```bash
   cd product-command-service
   ```

Run the Product Command Service:

   ```bash
   ./gradlew bootRun
   ```

   The service will start on port 9001.

#### Product Query Service<a name="product-query-service"></a>
1. Navigate to the `product-query-service` directory:

   ```bash
   cd product-query-service
   ```

2. Run the Product Query Service:

   ```bash
   ./gradlew bootRun
   ```

   The service will start on port 9002.

## Project Details<a name="project-details"></a>
Here's an overview of the two services included in this project:

### Product Command Service<a name="product-command-service"></a>
- **Port**: 9001
- **Database**: PostgreSQL (configured in `application.properties`)
![img_7.png](screenshots%2Fimg_7.png)

- **Kafka Producer**:
    - Bootstrap Servers: localhost:9092
    - Key Serializer: String
    - Value Serializer: JSON
- This service handles product-related commands.

### Product Query Service<a name="product-query-service"></a>
- **Port**: 9002
- **Database**: PostgreSQL (configured in `application.properties`)
- **Kafka Consumer**:
    - Bootstrap Servers: localhost:9092
    - Key Deserializer: String
    - Value Deserializer: String
    - Trusted JSON Packages: com.example.*
- This service handles product-related queries.

## Docker Compose Files<a name="docker-compose-files"></a>
- **Kafka**: The Kafka container is defined in `docker-compose/kafka.yml`. It includes Zookeeper, Kafka brokers and kafka-ui.
- **PostgreSQL**: The PostgreSQL container is defined in `docker-compose/postgres.yml`. It sets up a PostgreSQL database and a pgAdmin web interface for database management.

## Additional Notes<a name="additional-notes"></a>
- The services are configured to use PostgreSQL as their database, with credentials and connection details specified in their respective `application.properties` files.
- If you want to build and package the services as Docker containers, you can uncomment the service definitions in the `docker-compose` files and build the Docker images accordingly.
- Make sure to stop the Docker containers and services when you're done:

  ```bash
  docker-compose -f docker-compose/kafka.yml down
  docker-compose -f docker-compose/postgres.yml down
  ```

  You can also stop the Spring Boot services by pressing `Ctrl+C` in their respective terminal windows.