[![Build Status](https://travis-ci.com/Westerdals/pgr200-eksamen-asmadsen.svg?token=mnDCXxqQBvvezU6uAPtS&branch=master)](https://travis-ci.com/Westerdals/pgr200-eksamen-asmadsen)

# Gruppe: 
* Kjell-Olaf Slagnes: slakje17
* Andreas Storesund Madsen: madand18

## Testing the server and client

The server runs on port 8080, so make sure nothing else uses this port.
```
Compile and run tests:
mvn clean package

Edit innlevering.properties with login and database connection string
Location: server/target/innlevering.properties

Start the server:
java -jar server/target/conference-api-server-0.1-SNAPSHOT.jar

Start the client:
java -jar cli/target/cli-0.1-SNAPSHOT.jar

This is an interactive client. It will list the actions available and promt with followup questions.
```

## Client example run
```
java -jar cli/target/cli-0.1-SNAPSHOT.jar

Choose startingpoint
> Talks
(use arrow keys)

What action do you want to take?
  List
> Add
  Update
  Delete
(use arrow keys)

Talk title 
> Sockets
Talk description 
> How to use java sockets
Do you want to add a topic? (Y/n)
> y
Select a topic
> Create a new topic
(use arrow keys)

Topic name 
> Java

Do you want to do another action (y/N)
> y

Choose startingpoint
> Talks
(use arrow keys)

What action do you want to take?
> List
  Add
  Update
  Delete
(use arrow keys)

{
  "values": [
    {
      "id": "292cdb0c-32ff-42a4-9a41-f73defc64962",
      "title": "12",
      "description": "saasdas",
      "topic_id": null,
      "topic": null
    },
    {
      "id": "618604c5-e7f7-424a-abb9-90d8ad0f9f9a",
      "title": "Sockets",
      "description": "How to use java sockets",
      "topic_id": "a5cccc9f-c2d8-40e7-96c0-83c6dc6574dc",
      "topic": {
        "id": "a5cccc9f-c2d8-40e7-96c0-83c6dc6574dc",
        "topic": "Java"
      }
    }
  ]
}

```

# PGR200 Hovedinnlevering

Innleveringsfrist: 12. november kl 09:00. **Viktig:** WiseFlow *stenger* når fristen er ute - lever i tide.

Tag koden med `innlevering` i GitHub og last opp en ZIP-fil til WiseFlow. Dersom du ikke fikk godkjent innlevering #1 eller #2 i første runde, last opp zip-fil av disse i tillegg.

## Oppgave

Du har funnet en konferanse du er interessert i å gå på, men du har ikke råd til billetten. Men frykt ikke: etter at du tok kontakt med de som organiserer konferansen fikk du høre at du kunne få gratisbillett dersom du hjelper til å lage noe programvare for konferansen.

Oppgaven din: lag en server for appen som inneholder konferanseprogrammet i en database. Funksjonaliteten må som et minimum tillate at man legger inn og lister ut foredrag på konferansen. Du bruke datamodellen angitt under eller forenkle eller endre den slik du selv ønsker.

Programmet skal følge god programmeringsskikk: Det skal ha enhetstester, det skal ha en god README-dokumentasjon, det skal hente inn konfigurasjon fra en .properties-fil. Fila skal ligge i current working directory, hete `innlevering.properties` og inneholde properties `dataSource.url`, `dataSource.username` og `dataSource.password`. Når vi evaluerer oppgaven ønsker vi å bruke egne verdier for disse. Prosjektet bør også bygge automatisk på [Travis CI](https://travis-ci.com).

Pass på at det er godt med tester, at koden kompilerer og kjører ok med "mvn test" og at du beskriver hvordan man tester løsningen manuelt.

Eksempel kjøring (inkluder dette i README.md-fila deres):

```bash
> mvn test
[INFO] Scanning for projects...
[INFO] ------------------------------------------------------------------------
[INFO] Building conference-server 0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ conference-server ---
[INFO] Compiling 25 source files to e:\Profiles\jbrodwal\workspaces\demo\conference-server\target\classes
[INFO]
[INFO] --- maven-compiler-plugin:3.1:testCompile (default-testCompile) @ conference-server ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 9 source files to e:\Profiles\jbrodwal\workspaces\demo\conference-server\target\test-classes
[INFO]
[INFO] --- maven-surefire-plugin:2.12.4:test (default-test) @ conference-server ---

-------------------------------------------------------
 T E S T S
-------------------------------------------------------
....
> mvn install
[INFO] Scanning for projects...
[INFO] ------------------------------------------------------------------------
[INFO] Building conference-server 0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO]
...
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ conference-server ---
[INFO] Building jar: e:\Profiles\jbrodwal\workspaces\demo\conference-server\target/conference-server-0.1-SNAPSHOT.jar
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
> psql --username postgres --command="create database ... with owner .."'
> Oppdater innlevering.properties med dataSource.url, dataSource.username, dataSource.password
> java -jar target/database-innlevering.jar resetdb
> java -jar target/database-innlevering.jar insert "Mitt foredrag"
> java -jar target/database-innlevering.jar list
```

Som en del av semesterarbeidet skal dere levere en video på 3-8 minutter. Dersom dere har laget dette i forbindelse med innlevering #1 eller innlevering #2 kan dere bare legge ved denne video. I motsatt fall skal dere ta opp en video for mappeinnleveringen på 3-8 minutter der dere parprogrammerer. Velg gjerne en bit med kode som dere refactorerer. Screencast-o-matic anbefales som verktøy for video-opptaket, men andre verktøy kan benyttes. En lenke til videoen skal leveres og ikke videoen selv. Husk å åpne for tilgang til videoen ("unlisted" i Youtube) og legge inn lenke fra README.

Dere skal også gi tilbakemelding på en annen gruppes besvarelse. Tilbakemeldingen skal skrives i en egen fil (tilsvarende format som en README-fil) og inkluderes både i deres prosjekt og den andre gruppens prosjekt. Tilbakemeldingen dere har mottatt skal ligge i en fil som heter `MOTTATT-TILBAKEMELDING.md` og tilbakemeldingen dere har gitt skal hete `GITT-TILBAKEMELDING.md`.

I tilbakemeldingen er det lurt å stille spørsmålene: 1. Hva lærte jeg av denne koden? 2. Hva forsto jeg ikke av denne koden? 3. Hva tror jeg forfatterne av koden kunne ha nyttig av å lære?

### Arkitektur

![Architecture Overview](doc/conference-server.png)

### Programflyt

![Programflyt](doc/conference-server-flow.png)

### Forslag til datamodell

![Datamodell](doc/conference-data-model.png)

## Sjekkliste for innleveringen

- [x] Kodekvalitet
  - [x] Koden er klonet fra GitHub classrom
  - [x] Produserer `mvn package` en executable jar? (tips: Bruk `maven-shade-plugin`)
  - [x] Bruker koden Java 8 og UTF-8
  - [x] Bygger prosjektet på [https://travis-ci.com](https://travis-ci.com)?
  - [x] Har du god test-dekning? (tips: Sett opp jacoco-maven-plugin til å kreve at minst 65% av linjene har testdekning)
  - [x] Er koden delt inn i flere Maven `<modules>`?
  - [x] Bruker kommunikasjon mellom klient og server HTTP korrekt?
  - [x] Kobler serveren seg opp mot PostgreSQL ved hjelp av konfigurasjon i fila `innlevering.properties` i *current working directory* med `dataSource.url`, `dataSource.username`, `dataSource.password`?
- [ ] Funksjonalitet
  - [x] add: Legg til et foredrag i databasen med title, description og topic (valgfritt)
  - [X] list: List opp alle foredrag i basen med et valgfritt topic
  - [ ] show: Vis detaljer for et foredrag
  - [x] update: Endre title, description eller topic for et foredrag
  - [ ] Valgfri tillegg: Kommandoer for å sette opp hvor mange dager og timer konferansen skal vare og hvor mange parallelle spor den skal inneholde.
- [ ] Dokumentasjon i form av README.md
  - [x] Navn og Feide-ID på dere de som var på teamet
  - [x] Inkluderer dokumentasjonen hvordan man tester ut funksjonaliteten programmet manuelt? (Inkludert eventuell ekstra funksjonalitet dere har tatt med)
  - [ ] Inkluderer dokumentasjonen en evaluering av hvordan man jobbet sammen?
  - [ ] Inkluderer dokumentasjonen en screencast av en parprogrammeringsesjon?
  - [ ] Inkluderer dokumentasjonen en evaluering *fra* en annen gruppe og en evaluering *til* en annen gruppe?
  - [x] Inkluderer dokumentasjonen en UML diagram med datamodellen?
  - [ ] Inkluderer dokumentasjonen en egenevaluering med hvilken karakter gruppen mener de fortjener?


### Innlevering

- [ ] Gi veilederne `hakonschutt` og `mudasar187` tilgang til repository
- [ ] Tag koden med `innlevering` i GitHub
- [ ] Ta en zip-eksport fra GitHub
- [ ] Last opp zip-fil i WiseFlow
- [ ] Dersom innlevering #1 eller innlevering #2 ikke ble godkjent *i WiseFlow*, last opp zip-fil med hver av disse innleveringene

## Retningslinjer for vurdering


### Minimum krav for B

De fleste av følgende må være oppfyllt:

- Kode uten slukte exceptions eller advarsler fra Eclipse((hehe))
- Readme som beskriver 4-10 steg for å demonstrere programmet
- Ingen alvorlige feil, krasj ved uventet input

### Krav for A

Løsningen må oppfylle alle krav til B og ha 2-3 områder som hever den ytterligere:

- Velskrevet (men ikke nødvendigvis omfattende) dokumentasjon
- At videoen får fram kvalitetene i designet
- Uttrykksfulle enhetstester som er effektive på å fange feil
- En velbegrunnet datamodell
- En lettfattelig og utvidbar http-server
- Spennende generisk kode som egentlig er unødvendig kompleks for å løse problemet
- Enkel kode som løser problemet presist og konsist (i konflikt med forrige)


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

# Architecture overview

![Architecture Overview](doc/database.png)

## Database assignment
[Video where we solve a bug](https://youtu.be/KL9j5lVmp0c)


## Running the Conference CLI client

1. `mvn package`
2. `java -classpath target/conference-server-0.1-SNAPSHOT.jar no.kristiania.prg200.conference.server.ConferenceServer`
3. `java -jar target/conference-server.jar help`
4. `java -jar target/conference-server.jar add --title "My Talk" --description "You should listen to my talk"`
5. `java -jar target/conference-server.jar listPrompt`
	* Will listPrompt the title and ids of talks
6. `java -jar target/conference-server.jar show --id xxxx-xx-xxxx-xxxxx`
	* Will show the title and description of a talk
7. `java -jar target/conference-server.jar update --id xxxx-xx-xxxx-xxxxx -title "New title"`
	* Will update the title (description, topic) of the talk in the database

![Usage flow](doc/conference-server-flow.png)

## Testing the server with curl

1. `java -classpath target/conference-server.jar no.kristiania.prg200.conference.server.ConferenceServer`
2. `curl -X POST --data-urlencode title="My Talk" --data-urlencode description="Come and see" http://localhost:8090/api/talks`
3. `curl http://localhost:8090/api/talks`
	* Will listPrompt all talks
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

$ java -jar target/conference-server-0.1-SNAPSHOT.jar listPrompt
200
4ece8579-452b-4d35-875e-67c5ca374081    My Talk
fd4d2022-fead-4003-ac21-a0595a360137    A Talk About Java
aeebffda-f3e9-4d55-a80e-3398345d62a3    Another Talk About Java
9508e602-5988-46c6-a79c-b5606d78cde6    My Talk on CURL

$ java -jar target/conference-server-0.1-SNAPSHOT.jar listPrompt -topic Java
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


	
