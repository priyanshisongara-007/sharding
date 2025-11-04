# User Data Sharding Example in Java with MySQL

This repository demonstrates a simple example of database sharding using Java and MySQL. The code shows how to partition user data across two different database shards based on the user ID.

---

## What is Sharding?

Sharding is a database architecture pattern that horizontally partitions data across multiple database instances (shards). Each shard contains a subset of the total data, usually based on a shard key. This improves performance, scalability, and availability by distributing load and data.

---

## Overview of This Example

- The project contains a simple Java class `Sharding` that manages user data (`id`, `name`).
- Two MySQL databases (shards) are used:
  - Shard 1: `sys` database at `10.65.134.76:3310`
  - Shard 2: `myapp` database at `10.65.134.76:3310`
- The user ID is the shard key.
- Users with **odd** IDs are stored in **Shard 1**.
- Users with **even** IDs are stored in **Shard 2**.

---

## How It Works

1. **Data Insertion**

   When inserting a user, the program checks the user's ID:

   ```java
   if (userId % 2 == 0) {
       // Connect to Shard 2
   } else {
       // Connect to Shard 1
   }



Prerequisites
Before running this example, ensure you have the following:
Java Development Kit (JDK) version 8 or higher
MySQL server running on 10.65.134.76 and listening on port 3310
Two databases created: sys and myapp
A users table in both databases with the schema:

Copy sql
CREATE TABLE users (
    id INT PRIMARY KEY,
    name VARCHAR(50)
);
A MySQL user (e.g., root) with access to both databases
Configuration
Update the database connection details in the Sharding class to match your MySQL environment:


private static final String URL_SHARD1 = "jdbc:mysql://10.65.134.76:3310/sys";
private static final String URL_SHARD2 = "jdbc:mysql://10.65.134.76:3310/myapp";
private static final String USER = "root";
private static final String PASSWORD = "*****";

Running the Example
Compile the Sharding Java class.

Run the program.
The example workflow:
Inserts four users with IDs 1 to 4.
Even IDs (2, 4) are inserted into myapp database.
Odd IDs (1, 3) are inserted into sys database.
Retrieves and prints each inserted user from the correct shard.
