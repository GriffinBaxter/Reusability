# SENG 302 Team 400 Project - Reusability: Share & Save

Project uses `gradle`, `npm`, `Spring Boot`, `Vue.js` and `Gitlab CI`.

## Project Structure

A frontend sub-project (web GUI):

- `frontend/src` Frontend source code (Vue.js)
- `frontend/public` publicly accessible web assets (e.g., icons, images, style sheets)
- `frontend/dist` Frontend production build

A backend sub-project (business logic and persistence server):

- `backend/src` Backend source code (Java - Spring)
- `backend/out` Backend production build

## How to run

The following is assuming you already cloned the repository.

### Frontend / GUI

    $ cd frontend
    $ npm install
    $ npm run serve

Running on: http://localhost:9500/ by default

### Backend / server (using an in-memory H2 database)

For running locally using an in-memory H2 database, the following commands should be run:

    cd backend
    ./gradlew bootRun

Running on: http://localhost:9499/ by default

To access the in-memory H2 database, route to http://localhost:9499/h2 in the browser.

Use the Setting Name of `Generic H2 (Embedded)`.

The driver class should be set to `org.h2.Driver`, JDBC URL should be `jdbc:h2:mem:testdb`, the username should be set to `sa` and there is no password.

### Backend / server (using a MariaDB database)

For running using a MariaDB database, the following environment variables are **required** to be set:
* DB_URL
* DB_USERNAME
* DB_PASSWORD
* DGAA_EMAIL
* DGAA_PASSWORD

To run it on port 9499, the following commands should be run:

    cd backend
    ./gradlew bootRun -Dspring.profiles.active=mariadb

Running on: http://localhost:9499/ by default

To run it on port 8999 (as it is in production), the following commands should be run:

    cd backend
    ./gradlew bootRun -Dspring.profiles.active=production

Running on: http://localhost:8999/ by default

The backend can be tested by using the examples from the [API specification](https://eng-git.canterbury.ac.nz/seng302-2021/seng302-api-spec-2021) in the [Swagger Editor](https://editor.swagger.io/).

## Demo User Accounts

#### Standard User with business
- Email: everblanxart@gmail.com
- Password: Password123!

#### Standard User without business
- Email: emailUSER@email.com
- Password: Password123!

#### Admin
- Email: DGAA@example.com
- Password: Password123!


## Contributors

- SENG302 teaching team
- Griffin Baxter
- Zhedong Cao
- Billie Johnson
- Zachary Kaye
- Hayley Krippner
- Jack Patterson
- Dan Ronen
- Troy Tomlins

## References

- [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring JPA docs](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Vue docs](https://vuejs.org/v2/guide/)
- [Learn resources](https://learn.canterbury.ac.nz/course/view.php?id=10577&section=11)
