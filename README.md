# Recipe Service

## Requirements

- java 17
- maven
- Docker (optional)

## Running the application

- build: ```mvn clean install ```
- run:
  - cd target && java -jar recipe-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
  - mvn spring-boot:run -Dspring-boot.run.profiles=local
- run with docker:
  - build image ``` docker build . -t recipe-service```

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

Swagger: http://localhost:8080/swagger-ui/index.html#/

