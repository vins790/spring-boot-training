# Pre-requirements
Before you run the application, the following prerequisites must be met:
- run all docker containers
- run liquibase to update the database schema

### How to run Word Frequency Service
In order to start the Word Frequency Service, you can run the following command in your terminal:

```bash
docker-compose up --build
```

## How to run Liquibase
Before running the liquibase, make sure your liquibase.properties file is properly configured with the correct database connection details. Once you have verified that, you can run the following command in your terminal to update the database schema:
```bash
liquibase update
```
or
```bash
.\mvnw.cmd liquibase:update
```

If you are adding a new migration, remember to:
- add the new migration file to the db.changelog-master.xml file,
- rebuild the project with:
```bash
.\mvnw.cmd clean compile
```

### Migration rollback
```bash
.\mvnw.cmd liquibase:rollback "-Dliquibase.rollbackCount=1"
```