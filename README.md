# Auction App ##
Auction site for ENI school project, using Java Spring Boot

## Initialization ###

### application.properties file ###
[Application properties](src/main/resources/application.properties) must be configured to run

#### <ins>app.hostname</ins> ####
Server hostname (address and port) to access site
<br>
Used for hyperlinks in mails

#### spring.datasource.* ####
Database access configuration
<br>
- <ins>spring.datasource.url
- <ins>spring.datasource.username
- <ins>spring.datasource.password

#### spring.mail.* ####
Mail sending configuration
<br>
Configured for Gmail address by default
- <ins>spring.mail.username</ins>
- <ins>spring.mail.password</ins>

### Database ###
Project has been developped with MariaDB (gradle depedency still present in [build.gradle](build.gradle))
<br>
There is a SQL script to create tables and procedures in your database
<br>
[create_database.sql](create_database.sql) file in root folder
