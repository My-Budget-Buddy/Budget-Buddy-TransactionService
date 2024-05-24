# Budget Buddy - Transaction Service

## Overview

The Transaction Service is a core component of the Budget Buddy personal finance application. This microservice is responsible for handling user transactions, providing basic CRUD operations as well as customized queries to manage financial data effectively.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [Testing](#testing)


## Architecture

The Transaction Service is built with the following technologies:
- **Backend**: Spring Boot
- **Database**: PostgreSQL
- **Testing**: JUnit
- **Communication**: RESTful APIs, using RESTClient for inter-service communication

## Installation

### Prerequisites

- JDK 17
- Maven
- PostgreSQL setup

### Steps

1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/transactionservice.git
    cd transactionservice
    ```

2. Build the project:
    ```bash
    mvn clean install
    ```

3. Run the application:
    ```bash
    mvn spring-boot:run
    ```

## Configuration

Configure your database and other settings in the `application.yml` file located in `src/main/resources`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/yourdbname
    username: yourusername
    password: yourpassword
  jpa:
    hibernate:
      ddl-auto: update
```

## Usage

The Transaction Service provides endpoints for managing user transactions. You can access these endpoints using tools like Postman or CURL.

## API Documentation

### Endpoints

#### Create Transaction
- **URL**: ```POST /transactions/user/{userId}/createTransaction\```
- **Description**: Create a new transaction.
- **Request**:
    ```json
    {
      "userId": 1,
      "accountId": 101,
      "vendorName": "Apple",
      "amount": 1200.99,
      "category": "Shopping",
      "description": "Purchase of electronics",
      "date": "2024-05-01"
    }
    ```
- **Response**:
    ```json
    {
      "transactionId": 12,
      "userId": 1,
      "accountId": 101,
      "vendorName": "Apple",
      "amount": 1200.99,
      "category": "Shopping",
      "description": "Purchase of electronics",
      "date": "2024-05-01"
    }
    ```

#### Get Transaction
- **URL**: ```GET /transactions/user/{userId}```
- **Description**: Retrieve a transaction by the userId.
- **Response**:
    ```json
    [
      {
        "transactionId": 1,
        "userId": 1,
        "accountId": 101,
        "vendorName": "Amazon",
        "amount": 59.99,
        "category": "Shopping",
        "description": "Purchase of electronics",
        "date": "2024-01-15"
      },
      ...
    ]
    ```

#### Update Transaction
- **URL**: ```PUT /transactions/user/{userId}/updateTransaction```
- **Description**: Update an existing transaction.
- **Request**:
    ```json
    {
      "transactionId": 1,
      "userId": 1,
      "accountId": 101,
      "vendorName": "Amazon",
      "amount": 89.99,
      "category": "Shopping",
      "description": "Purchase of electronics",
      "date": "2024-01-15"
    }
    ```
- **Response**:
    ```json
    {
      "transactionId": 1,
      "userId": 1,
      "accountId": 101,
      "vendorName": "Amazon",
      "amount": 89.99,
      "category": "Shopping",
      "description": "Purchase of electronics",
      "date": "2024-01-15"
    }
    ```

#### Delete Transaction
- **URL**: ```DELETE /transactions/user/{userId}/deleteTransaction/{transactionId}```
- **Description**: Delete a transaction by its ID.
- **Response**:
  204: NO CONTENT

#### Get Recent Five Transactions
- **URL**: ```GET /transactions/recentTransactions/{userId}```
- **Description**: Retrieve the most recent five transactions for a specific user.
- **Response**:
    ```json
    [
      {
        "transactionId": 8,
        "userId": 1,
        "accountId": 108,
        "vendorName": "Uber",
        "amount": 25.00,
        "category": "Transportation",
        "description": "Ride to airport",
        "date": "2024-01-22"
    },
      ...
    ]
    ```

#### Get Transactions for the Current Month
- **URL**: ```GET /transactions/currentMonthTransactions/{userId}```
- **Description**: Retrieve all transactions for the current month for a specific user.
- **Response**:
    ```json
    [
      {
        "transactionId": 1,
        "userId": 1,
        "accountId": 101,
        "vendorName": "Amazon",
        "amount": 59.99,
        "category": "Shopping",
        "description": "Purchase of electronics",
        "date": "2024-05-15"
      },
      ...
    ]
    ```

## Testing

To run the tests, use the following command:

```bash
mvn test
```







