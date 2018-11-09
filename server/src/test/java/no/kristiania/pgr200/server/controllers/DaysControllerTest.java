package no.kristiania.pgr200.server.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.http.HttpMethod;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.common.http.HttpStatus;
import no.kristiania.pgr200.common.models.Day;
import no.kristiania.pgr200.orm.Orm;
import no.kristiania.pgr200.server.models.*;
import no.kristiania.pgr200.server.models.DayModel;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;

public class DaysControllerTest {

    private ConferenceModel conferenceModel;

    @Before
    public void beforeEach() throws SQLException {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:h2:mem:conference_server;DB_CLOSE_DELAY=-1", "sa", "sa");
        flyway.setLocations("filesystem:src/main/resources/db/migration");
        flyway.clean();
        flyway.migrate();
        Orm.connection = DriverManager.getConnection(
                "jdbc:h2:mem:conference_server;DB_CLOSE_DELAY=-1", "sa", "sa");
        conferenceModel = ControllerTestUtils.createConferenceModel();
    }

    @Test
    public void shouldReturnAllDays() {
        DayModel model = ControllerTestUtils.createDayModel(conferenceModel);
        DaysController controller = new DaysController(
                ControllerTestUtils.createHttpRequest(HttpMethod.GET, "/days"));
        HttpResponse response = controller.index();
        JsonArray dayList = ControllerTestUtils.modelListFromResponse(response);

        assertThat(dayList.size()).isEqualTo(1);

        JsonObject day = dayList.get(0).getAsJsonObject();
        assertThat(day.get("id").getAsString()).isEqualTo(model.getState().getId().toString());
        assertThat(day.get("conference_id").getAsString()).isEqualTo(model.getState().getConference_id().toString());
    }

    @Test
    public void shouldReturnOneDay() {
        DayModel model = ControllerTestUtils.createDayModel(conferenceModel);
        DaysController controller = new DaysController(
                ControllerTestUtils.createHttpRequest(HttpMethod.GET, "/days/" + model.getState().getId()));
        HttpResponse response = controller.show();
        JsonObject day = response.getJson().getAsJsonObject().get("value").getAsJsonObject();

        assertThat(UUID.fromString(day.get("id").getAsString())).isEqualTo(model.getState().getId());
        assertThat(day.get("conference_id").getAsString()).isEqualTo(model.getState().getConference_id().toString());
    }

    @Test
    public void shouldCreateOneDay() {
        HttpRequest request = ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/days");
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody(ControllerTestUtils.modelToJson(
                new Day(Date.valueOf(LocalDate.now()).toString(), conferenceModel.getState().getId())));
        DaysController controller = new DaysController(request);
        HttpResponse response = controller.create();
        Day day = ControllerTestUtils.jsonToModel(response, Day.class);

        assertThat(day.getConference_id()).isEqualTo(conferenceModel.getState().getId());
    }

    @Test
    public void shouldUpdateOneDay() {
        ConferenceModel newConference = ControllerTestUtils.createConferenceModel();
        DayModel dayModel = ControllerTestUtils.createDayModel(conferenceModel);
        HttpRequest request = ControllerTestUtils.createHttpRequest(
                HttpMethod.PUT, "/days/" + dayModel.getState().getId());
        request.getHeaders().put("Content-Type", "application/json");
        dayModel.getState().setConference_id(newConference.getPrimaryKeyValue());
        request.setBody(ControllerTestUtils.modelToJson(dayModel.getState()));
        DaysController controller = new DaysController(request);
        HttpResponse response = controller.update();
        Day day = ControllerTestUtils.jsonToModel(response, Day.class);

        assertThat(day.getId()).isEqualTo(dayModel.getDbState().getId());
        assertThat(day.getConference_id()).isNotEqualTo(conferenceModel.getPrimaryKeyValue());
        assertThat(day.getConference_id()).isEqualTo(newConference.getPrimaryKeyValue());
    }

    @Test
    public void shouldDeleteOneDay() {
        DayModel model = ControllerTestUtils.createDayModel(conferenceModel);
        DaysController controller = new DaysController(
                ControllerTestUtils.createHttpRequest(HttpMethod.DELETE, "/days/" + model.getState().getId()));
        HttpResponse response = controller.destroy();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);

        response = controller.show();

        assertThat(response.getJson().toString()).isEqualTo("{\"value\":{\"data\":\"No results\"}}");
    }

    @Test
    public void shouldReturnBadRequestWhenUuidIsWrongFormat() {
        // Show
        DaysController controller = new DaysController(
                ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/tracks/notaUUID"));
        HttpResponse response = controller.show();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BadRequest);

        // Update
        controller = new DaysController(
                ControllerTestUtils.createHttpRequest(HttpMethod.PUT, "/tracks/notaUUID"));
        response = controller.update();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BadRequest);

        // Destroy
        controller = new DaysController(
                ControllerTestUtils.createHttpRequest(HttpMethod.DELETE, "/tracks/notaUUID"));
        response = controller.destroy();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BadRequest);
    }

    @Test
    public void shouldReturnUnprocessableWithWrongJsonFormatOnCreate() {
        HttpRequest request = ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/days");
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{\"day\"\" <-- missing colon\"}");
        DaysController controller = new DaysController(request);
        HttpResponse response = controller.create();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertTrue(response.getJson().getAsJsonObject()
                .get("error").getAsJsonObject().get("message").getAsString()
                .contains("com.google.gson.stream.MalformedJsonException"));
    }

    @Test
    public void shouldRespondWithViolationsOnCreate() {
        HttpRequest request = ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/days");
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{}");
        DaysController controller = new DaysController(request);
        HttpResponse response = controller.create();
        JsonObject errorResponse = ControllerTestUtils.stringToJsonObject(response.getBody());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertTrue(errorResponse.get("errors").toString().contains("\"conference_id\":\"must be a valid UUID\""));
    }

    @Test
    public void shouldReturnUnprocessableWithWrongJsonFormatOnUpdate() {
        HttpRequest request = ControllerTestUtils.createHttpRequest(
                HttpMethod.PUT, "/days/" + UUID.randomUUID());
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{\"day\"\" <-- missing colon\"}");
        DaysController controller = new DaysController(request);
        HttpResponse response = controller.update();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertTrue(response.getJson().getAsJsonObject()
                .get("error").getAsJsonObject().get("message").getAsString()
                .contains("com.google.gson.stream.MalformedJsonException"));
    }

    @Test
    public void shouldRespondWithViolationsOnUpdate() {
        DayModel model = ControllerTestUtils.createDayModel(conferenceModel);
        HttpRequest request = ControllerTestUtils.createHttpRequest(
                HttpMethod.PUT, "/days/" + model.getState().getId());
        request.getHeaders().put("Content-Type", "application/json");
        Day day = model.getState();
        day.setConference_id(null);
        request.setBody(ControllerTestUtils.modelToJson(day));
        DaysController controller = new DaysController(request);
        HttpResponse response = controller.update();
        JsonObject errorResponse = ControllerTestUtils.stringToJsonObject(response.getBody());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertThat(errorResponse.get("errors").toString()).isEqualTo("{\"conference_id\":\"must be a valid UUID\"}");
    }

    @Test
    public void shouldReturnNoResultsWithIncorrectUuid() {
        DaysController controller = new DaysController(
                ControllerTestUtils.createHttpRequest(HttpMethod.GET, "/days/" + UUID.randomUUID()));
        HttpResponse response = controller.show();

        assertThat(response.getJson().toString()).isEqualTo("{\"value\":{\"data\":\"No results\"}}");
    }
}