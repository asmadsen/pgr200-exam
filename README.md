## Hvordan teste løsningen
```
Naviger til target mappen:
cd server/target

Endre innlevering.properties med riktig kobling til din postgresql database.

Start serveren:
java -jar conference-api-server-0.1-SNAPSHOT.jar

Start dette bin/bash skriptet som tester noen få endepunkter på vårt API:
./api_cURL_commands

eller importer alle endpunkter til postman og naviger manuelt der:
ConferenceServer.postman_collection.json
```

## Sjekkliste for innleveringen

- [ ] Kodekvalitet
    - [ ] Produserer `mvn package` en executable jar? (tips: Bruk `maven-shade-plugin`)
    - [ ] Bruker koden Java 8 og UTF-8
    - [ ] Bygger prosjektet på https://travis-ci.com?
    - [ ] Har du god test-dekning? (tips: Sett opp jacoco-maven-plugin til å kreve at minst 65% av linjene har testdekning)
    - [ ] Kobler main-klassen seg opp mot PostgreSQL ved hjelp av en properties-fil?
    - [ ] Kan main-klassen resette databasen? (tips: Bruk Flyway#clear)
- [ ] Funksjonalitet
    - [ ] Kan man legge inn et nytt foredrag?
    - [ ] Kan man liste foredrag i databasen?
    - [ ] Valgfritt: Oppretter main-klassen et konferanseprogram med flere dager og tracks?
    - [ ] Valgfritt: Kan main-klassen vise konferanseprogrammet?
- [ ] Dokumentasjon i form av README.md
    - [ ] Navn og Feide-ID på dere de som var på teamet
    - [ ] Inkluderer dokumentasjonen hvordan man tester ut funksjonaliteten programmet manuelt? (Inkludert eventuell ekstra funksjonalitet dere har tatt med)
    - [ ] Inkluderer dokumentasjonen en evaluering av hvordan man jobbet sammen?
    - [ ] Inkluderer dokumentasjonen en screencast av en parprogrammeringsesjon?
    - [ ] Inkluderer dokumentasjonen en evaluering _fra_ en annen gruppe og en evaluering _til_ en annen gruppe?
    - [ ] Inkluderer dokumentasjonen et databasediagram
    - [ ] Inkluderer dokumentasjonen en link til screencast av programmeringsesjon?

# Architecture overview [![Build Status](https://travis-ci.com/Westerdals/command-line-parser-exercise-asmadsen.svg?token=CtziNfWQqQxtrfWutyqx&branch=master)](https://travis-ci.com/Westerdals/command-line-parser-exercise-asmadsen)

![Architecture Overview](doc/conference-server.png)

## Database assignment
[Video where we solve a bug](https://youtu.be/KL9j5lVmp0c)


## Running the Conference CLI client

1. `mvn package`
2. `java -classpath target/conference-server-0.1-SNAPSHOT.jar no.kristiania.prg200.conference.server.ConferenceServer`
3. `java -jar target/conference-server.jar help`
4. `java -jar target/conference-server.jar add --title "My Talk" --description "You should listen to my talk"`
5. `java -jar target/conference-server.jar list`
	* Will list the title and ids of talks
6. `java -jar target/conference-server.jar show --id xxxx-xx-xxxx-xxxxx`
	* Will show the title and description of a talk
7. `java -jar target/conference-server.jar update --id xxxx-xx-xxxx-xxxxx -title "New title"`
	* Will update the title (description, topic) of the talk in the database

![Usage flow](doc/conference-server-flow.png)

## Testing the server with curl

1. `java -classpath target/conference-server.jar no.kristiania.prg200.conference.server.ConferenceServer`
2. `curl -X POST --data-urlencode title="My Talk" --data-urlencode description="Come and see" http://localhost:8090/api/talks`
3. `curl http://localhost:8090/api/talks`
	* Will list all talks
4. `curl http://localhost:8090/api/talks/<id>`
	* Will show the title and description of a talk

	
## Example

```
$ mvn package
[INFO] Scanning for projects...
[INFO] ------------------------------------------------------------------------
[INFO] Building conference-server 0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ conference-server ---
[INFO] Compiling 25 source files to ...\conference-server\target\classes
[INFO]
[INFO] --- maven-compiler-plugin:3.1:testCompile (default-testCompile) @ conference-server ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 9 source files to ..\conference-server\target\test-classes
[INFO]
[INFO] --- maven-surefire-plugin:2.12.4:test (default-test) @ conference-server ---

-------------------------------------------------------
 T E S T S
-------------------------------------------------------
....

Results :

Tests run: 27, Failures: 0, Errors: 0, Skipped: 2

[INFO]
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ conference-server ---
[INFO] Building jar: ...conference-server\target/conference-server-0.1-SNAPSHOT.jar
[INFO]
[INFO] --- maven-shade-plugin:3.1.1:shade (default) @ conference-server ---
[INFO] Including org.postgresql:postgresql:jar:42.2.2 in the shaded jar.
[INFO] Replacing original artifact with shaded artifact.
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 12.565 s
[INFO] Finished at: 2018-07-08T17:18:12+02:00
[INFO] Final Memory: 21M/211M
[INFO] ------------------------------------------------------------------------

$ java -jar target/conference-server-0.1-SNAPSHOT.jar add -title "My Talk" -description "You should listen to my talk"
200


$ java -jar target/conference-server-0.1-SNAPSHOT.jar add -title "A Talk About Java" -description "A very interesting talk about Java" -topic Java
200


$ java -jar target/conference-server-0.1-SNAPSHOT.jar add -title "Another Talk About Java" -description "More interesting information about Java" -topic Java
200


$ curl -X "POST" --data-urlencode title="My Talk on CURL" --data-urlencode description="Something else" --data-urlencode topic=script http://localhost:8090/api/talks

$ java -jar target/conference-server-0.1-SNAPSHOT.jar list
200
4ece8579-452b-4d35-875e-67c5ca374081    My Talk
fd4d2022-fead-4003-ac21-a0595a360137    A Talk About Java
aeebffda-f3e9-4d55-a80e-3398345d62a3    Another Talk About Java
9508e602-5988-46c6-a79c-b5606d78cde6    My Talk on CURL

$ java -jar target/conference-server-0.1-SNAPSHOT.jar list -topic Java
200
fd4d2022-fead-4003-ac21-a0595a360137    A Talk About Java
aeebffda-f3e9-4d55-a80e-3398345d62a3    Another Talk About Java

$ java -jar target/conference-server-0.1-SNAPSHOT.jar show -id 9508e602-5988-46c6-a79c-b5606d78cde6
200
ID
9508e602-5988-46c6-a79c-b5606d78cde6

Title
My Talk on CURL

Topic
script

Description
Something else

$ curl http://localhost:8090/api/talks
4ece8579-452b-4d35-875e-67c5ca374081    My Talk
fd4d2022-fead-4003-ac21-a0595a360137    A Talk About Java
aeebffda-f3e9-4d55-a80e-3398345d62a3    Another Talk About Java
9508e602-5988-46c6-a79c-b5606d78cde6    My Talk on CURL

$ curl http://localhost:8090/api/talks/9508e602-5988-46c6-a79c-b5606d78cde6
ID
9508e602-5988-46c6-a79c-b5606d78cde6

Title
My Talk on CURL

Topic
script

Description
Something else
$ curl http://localhost:8090/api/talks?topic=Java
fd4d2022-fead-4003-ac21-a0595a360137    A Talk About Java
aeebffda-f3e9-4d55-a80e-3398345d62a3    Another Talk About Java
```


## Adding a schedule

1. `java -jar conference.jar schedule -tracks 3 -timeslots 10 -days 2`
2. `java -jar conference.jar schedule-talk -id <id> -track 3 -timeslot 10 -day 2`
3. `java -jar conference.jar show-schedule`
4. Update schedule
	* `java -jar conference.jar update-schedule -day 1 -date '2018-11-01'`
	* `java -jar conference.jar update-schedule -track 2 -name Database`
	* `java -jar conference.jar update-schedule -timeslot 4 -start 10:00`


	