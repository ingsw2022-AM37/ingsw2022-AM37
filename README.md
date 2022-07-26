# ERIANTYS

This is the repository of main project for "_Progetto di Ingegneria del Software 2022_".

Game rules are provided [here](https://www.craniocreations.it/wp-content/uploads/2021/11/Eriantys_ITA_bassa.pdf)

Tests line coverage: client 4%, controller 17%, message 54%, model 82%, network 0%

**Final vote: 30 cum laude**

### Starting game

1) In order to launch the **server** you have to go with terminal in its jar folder and
   write: `java -jar nameJar.jar --port port`
    - `--port` is an optional field.

2) In order to launch the **client** you have two options:
    - Repeat the same procedure and write `java -jar jarName.jar --graphics gui --address address --port port`
    - Double-click on the file for playing with graphics.
        - `--address` and `--port` are optional fields.
        - You can write `cli` instead of `gui`.

#### Group components:

- ##### 10697104    Baggetto Matteo ([@Matteo-Baggetto](https://github.com/Matteo-Baggetto)) [✉ Contact me](mailto:matteo.baggetto@mail.polimi.it)
- ##### 10675392    Brambilla Matteo ([@bramba2000](https://github.com/bramba2000)) [✉ Contact me](mailto:matteo15.brambilla@mail.polimi.it)
- ##### 10723902    Cerutti Paolo ([@PolCeru](https://github.com/PolCeru)) [✉ Contact me](mailto:paolo2.cerutti@mail.polimi.it)

### Functionalities

| Functionality                         | State |
|:--------------------------------------|:-----:|
| Basic rules                           |  🟢   |
| Complete rules                        |  🟢   |
| Socket                                |  🟢   |
| GUI                                   |  🟢   |
| CLI                                   |  🟢   |
| Advanced characters                   |  🟢   |
| Resilience to disconnections          |  🟢   |
| Multiple games                        |  🟢   |

### Libraries and Plugins

| Libraries/Plugin | Description                                                   |
|------------------|---------------------------------------------------------------|
| __Maven__        | Compilation automation tool used primarily for Java projects. |
| __JavaFx__       | Graphic library for making user interfaces.                   |
| __JUnit__        | Unit Testing Framework.                                       |
| __Mockito__      | Mocking framework for unit tests.                             |
| __Log4j2__       | Logging framework.                                            |
| __Jansi__        | ANSI CLI utility library for console output.                  |
