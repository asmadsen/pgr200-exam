package no.kristiania.pgr200.server.controllers;

import com.github.javafaker.Faker;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.http.HttpMethod;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.common.http.HttpStatus;
import no.kristiania.pgr200.common.models.Conference;
import no.kristiania.pgr200.orm.Orm;
import no.kristiania.pgr200.server.models.ConferenceModel;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;

public class ConferencesControllerTest {
    @Before
    public void beforeEach() throws SQLException {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:h2:mem:conference_server;DB_CLOSE_DELAY=-1", "sa", "sa");
        flyway.setLocations("filesystem:src/main/resources/db/migration");
        flyway.clean();
        flyway.migrate();
        Orm.connection = DriverManager.getConnection(
                "jdbc:h2:mem:conference_server;DB_CLOSE_DELAY=-1", "sa", "sa");
    }

    @Test
    public void shouldReturnAllConferences() {
        ConferenceModel model = ControllerTestUtils.createConferenceModel();
        ConferencesController controller = new ConferencesController(
                ControllerTestUtils.createHttpRequest(HttpMethod.GET, "/conferences"));
        HttpResponse response = controller.index();
        JsonArray conferenceList = ControllerTestUtils.modelListFromResponse(response);

        assertThat(conferenceList.size()).isEqualTo(1);

        JsonObject conference = conferenceList.get(0).getAsJsonObject();
        assertThat(conference.get("id").getAsString()).isEqualTo(model.getState().getId().toString());
        assertThat(conference.get("name").getAsString()).isEqualTo(model.getState().getName().toString());
    }

    @Test
    public void shouldReturnOneConference() {
        ConferenceModel model = ControllerTestUtils.createConferenceModel();
        ConferencesController controller = new ConferencesController(
                ControllerTestUtils.createHttpRequest(HttpMethod.GET, "/conferences/" + model.getState().getId()));
        HttpResponse response = controller.show();
        JsonObject conference = response.getJson().getAsJsonObject().get("value").getAsJsonObject();
        assertThat(UUID.fromString(conference.get("id").getAsString())).isEqualTo(model.getState().getId());
        assertThat(conference.get("name").getAsString()).isEqualTo(model.getState().getName());
    }

    @Test
    public void shouldCreateOneConference() {
        HttpRequest request = ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/conferences");
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody(ControllerTestUtils.modelToJson(new Conference("MobileEra")));
        ConferencesController controller = new ConferencesController(request);
        HttpResponse response = controller.create();
        Conference conference = ControllerTestUtils.jsonToModel(response, Conference.class);

        assertThat(conference.getName()).isEqualTo("MobileEra");
    }

    @Test
    public void shouldUpdateOneConference() {
        String newName = new Faker().leagueOfLegends().champion();
        ConferenceModel conferenceModel = ControllerTestUtils.createConferenceModel();
        HttpRequest request = ControllerTestUtils.createHttpRequest(
                HttpMethod.PUT, "/conferences/" + conferenceModel.getState().getId());
        request.getHeaders().put("Content-Type", "application/json");
        conferenceModel.getState().setName(newName);
        request.setBody(ControllerTestUtils.modelToJson(conferenceModel.getState()));
        ConferencesController controller = new ConferencesController(request);
        HttpResponse response = controller.update();
        Conference conference = ControllerTestUtils.jsonToModel(response, Conference.class);

        assertThat(conference.getName()).isNotEqualTo(conferenceModel.getDbState().getName());
        assertThat(conference.getName()).isEqualTo(newName);
    }

    @Test
    public void shouldDeleteOneConference() {
        ConferenceModel model = ControllerTestUtils.createConferenceModel();
        ConferencesController controller = new ConferencesController(
                ControllerTestUtils.createHttpRequest(HttpMethod.DELETE, "/conferences/" + model.getState().getId()));
        HttpResponse response = controller.destroy();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);

        response = controller.show();

        assertThat(response.getJson().toString()).isEqualTo("{\"value\":{\"data\":\"No results\"}}");
    }

    @Test
    public void shouldReturnBadRequestWhenUuidIsWrongFormat() {
        // Show
        ConferencesController controller = new ConferencesController(
                ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/conferences/notaUUID"));
        HttpResponse response = controller.show();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BadRequest);

        // Update
        controller = new ConferencesController(
                ControllerTestUtils.createHttpRequest(HttpMethod.PUT, "/conferences/notaUUID"));
        response = controller.update();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BadRequest);

        // Destroy
        controller = new ConferencesController(
                ControllerTestUtils.createHttpRequest(HttpMethod.DELETE, "/conferences/notaUUID"));
        response = controller.destroy();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BadRequest);
    }

    @Test
    public void shouldReturnUnprocessableWithWrongJsonFormatOnCreate() {
        HttpRequest request = ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/conferences");
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{\"name\"\" <-- missing colon\"}");
        ConferencesController controller = new ConferencesController(request);
        HttpResponse response = controller.create();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertTrue(response.getJson().getAsJsonObject()
                .get("error").getAsJsonObject().get("message").getAsString()
                .contains("com.google.gson.stream.MalformedJsonException"));
    }

    @Test
    public void shouldRespondWithViolationsOnCreate(){
        HttpRequest request = ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/conferences");
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody(ControllerTestUtils.modelToJson(new Conference()));
        ConferencesController controller = new ConferencesController(request);
        HttpResponse response = controller.create();

        JsonObject errorResponse = ControllerTestUtils.stringToJsonObject(response.getBody());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertThat(errorResponse.get("errors").toString())
                .isEqualTo("{\"name\":\"must not be blank\"}");
    }

    @Test
    public void shouldReturnUnprocessableWithWrongJsonFormatOnUpdate() {
        ConferenceModel model = ControllerTestUtils.createConferenceModel();
        HttpRequest request = ControllerTestUtils.createHttpRequest(
                HttpMethod.PUT, "/conferences/" + model.getState().getId());
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{\"title\"\"Java Conference\"}");
        ConferencesController controller = new ConferencesController(request);
        HttpResponse response = controller.update();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertTrue(response.getJson().getAsJsonObject()
                .get("error").getAsJsonObject().get("message").getAsString()
                .contains("com.google.gson.stream.MalformedJsonException"));
    }

    @Test
    public void shouldRespondWithViolationsOnUpdate(){
        ConferenceModel model = ControllerTestUtils.createConferenceModel();
        HttpRequest request = ControllerTestUtils.createHttpRequest(
                HttpMethod.PUT, "/conferences/" + model.getState().getId());
        request.getHeaders().put("Content-Type", "application/json");
        Conference conference = model.getState();
        conference.setName("");
        request.setBody(ControllerTestUtils.modelToJson(conference));
        ConferencesController controller = new ConferencesController(request);
        HttpResponse response = controller.update();
        JsonObject errorResponse = ControllerTestUtils.stringToJsonObject(response.getBody());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertThat(errorResponse.get("errors").toString()).isEqualTo("{\"name\":\"must not be blank\"}");
    }

}