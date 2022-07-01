# INGSW2022-AM37

This is the repository of main project for "_Progetto di Ingegneria del Software 2022_".

Gamerules provided [here](https://www.craniocreations.it/wp-content/uploads/2021/11/Eriantys_ITA_bassa.pdf)

Tests line coverage: client 4%, controller 17%, message 54%, model 82%, network 0%

### Starting game
1) In order to launch the server you have to go with terminal in its jar folder and write: java -jar nameJar.jar --port port
   --port port is an optional field
   
2) You have two option for launching the game: you can repeat the same procedure and write java -jar nameJar.jar --graphics cli --address address --port port 
   or you can double click on the file. 
   --address address and --port port are otpional fields. 
Group components:

- ##### 10697104    Baggetto Matteo ([@Matteo-Baggetto](https://github.com/Matteo-Baggetto)) [✉ Contact Me](mailto:matteo.baggetto@mail.polimi.it)
- ##### 10675392    Brambilla Matteo ([@bramba2000](https://github.com/bramba2000)) [✉ Contact Me](mailto:matteo15.brambilla@mail.polimi.it)
- ##### 10723902    Cerutti Paolo ([@PolCeru](https://github.com/PolCeru)) [✉ Contact Me](mailto:paolo2.cerutti@mail.polimi.it)

### Functionalities

| Functionality                         | State |
|:--------------------------------------|:-----:|
| Basic rules                           |  🟢   |
| Complete rules                        |  🟢   |
| Socket                                |  🟢   |
| GUI                                   |  🟢   |
| CLI                                   |  🟢   |
| Advanced characters                   |  🟢   |
| Resilience to disconnections          |  🟡   |
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
