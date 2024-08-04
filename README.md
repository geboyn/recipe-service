# Recipe Service

The application has been built using Spring Boot in java 21. To keep things easier to set up, I have used
an in memory database (h2) and for the advanced filtering I chose Spring Jpa Specification.

## Requirements

- java 17
- maven
- Docker (optional)

## Running the application

- build:
  - ```mvn spotless:apply```
  - ```mvn clean install ```
- run:
  - ```cd target && java -jar recipe-0.0.1-SNAPSHOT.jar --spring.profiles.active=local```
    OR
  - ```mvn spring-boot:run -Dspring-boot.run.profiles=local```
- run with docker:
  - build image ```docker build . -t recipe-service```
  - run ```docker run -p 8080:8080 -e "SPRING_PROFILES_ACTIVE=local" recipe-service```

## Usage examples

### Save recipe:

```
curl  --request POST 'localhost:8080/recipes' --header 'Content-Type: application/json' \
--data-raw '[
  {
    "name": "steak",
    "type": "standard",
    "instructions": "Just Do it!",
    "servings": 4,
    "ingredients": [
      {
        "name": "potatos",
        "quantity": "500 grams"
      },
      {
        "name": "pork",
        "quantity": "300 grams"
      }
    ]
  }
]'
```

### Get recipe by id:

```curl --location --request GET 'localhost:8080/recipes/{id}'```

More examples can be found in the documentation section

## Documentation:

- Run the application and go to Swagger: http://localhost:8080/swagger-ui/index.html#/

