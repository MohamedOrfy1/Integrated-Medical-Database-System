# Integrated-Medical-Database-System
## Backend Code Manual
---
### Most Popular Commands

```ssh
mvn spring-boot:run
```
**Runs the Project from the Root of the Project**
```ssh
mvn clean
```
**Cleans up the project by deleting the target/ directory**
```ssh
mvn install
```
**Compiles, runs tests, packages, and then installs the JAR into your local Maven repository**
```ssh
mvn build
```
**Compiles the Java source code of your project**
---
### Project Structure

demo/  
├── .gitignore  
├── pom.xml  
├── src/  
│ ├── main/  
│ │ ├── java/  
│ │ │ └── com.example.demo/  
│ │ │ ├── controller/  
│ │ │ ├── filter/  
│ │ │ ├── model/  
│ │ │ ├── repository/  
│ │ │ ├── service/  
│ │ │ └── DemoApplication.java  
│ │ └── resources/  
│ │ ├── env/  
│ │ └── application.properties  
└── target/ (generated during build)  


## Key Files Explanation:

1. **Root Level**:
   - `.gitignore` - Specifies files to exclude from version control
   - `pom.xml` - Maven build configuration file (extra libraries are added here as dependencies and the installed using the install CMD) 

2. **Source Code (`src/main/java`)**:
   - `com.example.demo` - Base package
     - `controller/` - Spring MVC controllers
     - `filter/` - Servlet filters (Middleware)
     - `model/` - Entity classes
     - `repository/` - Data access layer (Spring Data JPA)
     - `service/` - Business logic layer
     - `DemoApplication.java` - Main Spring Boot application class

3. **Resources (`src/main/resources`)**:
   - `env/` - Environment-specific configuration files **(Not pushed into the Repo)**
   - `application.properties` - Main Spring configuration

4. **Generated**:
   - `target/` - Created by Maven during build (contains compiled classes and JAR)

The `target` directory is automatically generated when you run `mvn package`.

---
### Complete endpoints
#### Base URL
`http://localhost:8080`
| Method | Endpoint                        | Description                                                | Request                     | Response                                          |
|--------|---------------------------------|------------------------------------------------------------|-----------------------------|---------------------------------------------------|
| GET    | `/patients/getPatients`         | get all patients registered in the database                | empty body                  | Json object of other Json objects of type patients|
| POST   | `/patients/add`                 | insert a patient into the database                         | Json object of type Patient | true or false depends if insertion is done or not |
| GET    | `employees/download/output.pdf` | generate and download randomized CBC test result report    | empty body                  | PDF File                                          |
| POST   | `doctors/getPatDiagnosis`       |                                                            |                             |                                                   |
| GET    | `doctors/getPatDiagnosis`       |                                                            |                             |                                                   |
| GET    | `/doctors/getDiagnosis`         |return all diagnoses names and codes in diagnosis table                                                            |   empty body                          |  list of json objects of type diagnosis                                                 |
| POST   | `employee/getPatDate`           |                                                            |                             |                                                   |
| POST   | `doctors/addDiagnosis`           |   add a new diagnosis                                                         |   json object of type diagnosis                          |                                                   |
| Delete   | `doctors/diagnosis/{diagnosisCode}`           |    delete diagnosis by code                                                        |                             |                                                   |
 ---


