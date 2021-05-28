# Wolt - Coding Task v2.2 Opening Hours

### Project Goal
The goal of the project is to write an endpoint that accepts JSON-formatted opening hours of a
restaurant as an input and returns the rendered human readable format as a text output

###Input
Input JSON consists of keys indicating days of a week and corresponding opening hours
as values.

Part I
---
### Deliverable
Build a HTTP API that accepts opening hours data as an input (JSON) and returns a more
human readable version of the data formatted using a 12-hour clock.
Output example in 12-hour clock format:
```Monday: 8 AM - 10 AM, 11 AM - 6 PM
Tuesday: Closed
Wednesday: 11 AM - 6 PM
Thursday: 11 AM - 6 PM
Friday: 11 AM - 9 PM
Saturday: 11 AM - 9 PM
Sunday: Closed
```
### Building and Running
To build this project, execute the following command in a repository's root directory (designates assembling all outputs and running all checks):
>$ gradle build

To run tests only, execute following:
>$ gradle test

It is common for all verification tasks, including tests and linting, to be executed using the check task.
>$ gradle check

For additional information and options, refer:
https://docs.gradle.org/current/userguide/command_line_interface.html

#### Running the project
To run the project, execute the following command in a repository's root directory (designates assembling all outputs and running all checks):
>$ gradle run

API Endpont:
>http://localhost:8080/api/v1/merchants/hours

The input for the input is JSON data and response is with plain text format.

Project can be running with `curl` or any other tool such as `Postman`
Here's a sample of a curl requst:

```curl -X PUT -H "Content-Type:application/json" -d "{}" http://localhost:8080/api/v1/merchants/hours```

### Implementation Details

#### Technologies
I used Kotlin as programming language and Ktor framework for bulding API endpoint.
Ktor is an asynchronous framework for creating microservices, web applications, and more. It’s fun, free, and open source.
I choose Ktor becuase it's very lightweight, asynchronous and extensible with great community support!

Ktor github: https://github.com/ktorio
Ktor documentation: https://ktor.io/docs/welcome.html

#### Assumptions (additional to the project description)
Client request contains all days of the week. If one of the days is missing, the error returns

#### Handling incorrect input or errors
`HttpStatusCode.BadRequest` (Status: 400 Bad Request) was used for any invalid input situations 

#### Test cases covered
1. With correct json input -> ok response
2. With missing day -> error response
3. With incorrect hours sequence (eg open -> close -> close -> open) -> error response
4. With incorrect hour sequence (eg open -> close -> close -> open) -> error response
5. With hours out of sequence (starts with closed) -> error response
6. With correct overnight shift -> ok response
7. With overnight and closed restaurant next day -> ok response
8. With overnight and next day empty (no close for overnight) -> error response
9. With overnight and next day starting with open hours  -> error response
10. With overnight with next day starting with closed status -> ok response
11. With multiple open/closed hours a day -> ok response
12. With out of range time (< 0 or > max) -> error response

### Things to consier improving
- Error and information messages needs to be stored in conf file

Part II
---
Is the current JSON structure the best way to store that kind of data or can you come up with a better version?

Things I would consider improving with proposed JSON structure:
- Consider use week days as collection as this way it would be easier for schema validation, eg [{}, {}] instead of dictionary style.
- Instead of `"type" : "open"`, consider using something like `"open": true` which will help validating the property
- Usually, if a property is optional or has an empty or null value (`monday`), it's worith to consider dropping the property from the JSON, unless there's a strong semantic reason for its existence.
- Consider dates be strings formatted as recommended by [RFC 3339](https://www.ietf.org/rfc/rfc3339.txt)

Contributor
---
[Sergey Kochetkov](https://www.linkedin.com/in/sergey-kochetkov-4501313/)
