# AuctionHouse
A simple action house with TCP/UDP communication between a server and several clients


Importing project with Intellij
1. Launch Intellij

2. Import Existent project

Database

Require

server: https://dev.mysql.com/downloads/mysql/
Install server

import database schema in Dump
from intellij database console

1. CREATE DATABASE auctionhouse

2. USE auctionhouse

3. paste the content of the dump


download connector for jar: https://dev.mysql.com/downloads/connector/j/5.1.html

file: mysql-connector-java-5.1.47.jar

Add jar to project in intellij

1. project structure
2. modules
3. dependencies
4. add jar (lower section)
5. find mysql-connector-java-5.1.47.jar in folder

run executable jar

java -Dlog4j.configurationFile=file:<LOG4J2 PROPERTIES PATH> -jar <NAME>.jar

