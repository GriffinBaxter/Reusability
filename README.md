# SENG 302 Team 400 Project

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

Note that due to CORS policy and sameSite cookie policy, you will need to disable some security measures.
For example this can be achieved by doing the following for Chrome: 
- Create a shortcut to Chrome 
- In the properties add '--disable-web-security --user-data-dir="C:/ChromeDevSession"' after everything in the target property field.

### Backend / server

    cd backend
    ./gradlew bootRun

Running on: http://localhost:9499/ by default

The backend can be tested by using the examples from the [API specification](https://eng-git.canterbury.ac.nz/seng302-2021/seng302-api-spec-2021) in the [Swagger Editor](https://editor.swagger.io/).

## Todo (S2)

- Update team name into `build.gradle` and `package.json`
- Update this README title
- Update this README contributors
- Set up Gitlab CI server (refer to the student guide on learn)
- Decide on a LICENSE

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
