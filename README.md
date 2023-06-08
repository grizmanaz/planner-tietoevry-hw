# Tietoevry homework task

## About task
This project is a simple (back-end) study planner made with Java and Spring/Spring Boot which helps you estimate the time needed to finish your thesis considering your job schedule and other busy time off the day.

I considered that student writing a thesis may be also working in an official job with a stable schedule, so I added workdays hours constants for everyday. Holidays, sleep and personal time was also considered.

otherBusyHours is optional, but student can use it if he is going to have some extra things to do that day.

### Running the project
1. Open project (intellij)
2. Run 'PlannerApplication' or mvn spring-boot:run into terminal
3. Open Postman or other API testing alternative
4. Enter URL http://localhost:8080/api/planner
5. Choose POST
6. Press Body->RAW
7. Enter sample JSON request data

#### Request parameters
1. startDate (date)
2. endDate (date)
3. thesisHoursNeeded (int)
4. date (date) (optional)
5. otherBusyHours (int) (optional)

#### Sample Json requests:
1.
```json
{
  "thesisHoursNeeded": 38,
  "dueDate": "2023-06-19",
  "startDate": "2023-06-09",
  "busyDays": [
    {
      "date": "2023-06-12",
      "otherBusyHours": 3
    },
    {
      "date": "2023-06-15",
      "otherBusyHours": 1
    }
  ]
}
```
 2.
 ```jso
{
  "thesisHoursNeeded": 25,
  "startDate": "2023-06-09",
  "dueDate": "2023-06-16"
}
```

