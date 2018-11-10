package no.kristiania.pgr200.server.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.http.HttpMethod;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.common.http.HttpStatus;
import no.kristiania.pgr200.common.models.Timeslot;
import no.kristiania.pgr200.orm.Orm;
import no.kristiania.pgr200.server.models.TalkModel;
import no.kristiania.pgr200.server.models.TimeslotModel;
import no.kristiania.pgr200.server.models.TrackModel;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertTrue;

public class TimeslotsControllerTest {
    HttpRequest request = new HttpRequest();
    JsonObject timeslotJsonObject = new JsonObject();
    TalkModel talkModel;
    TrackModel trackModel;

    @Before
    public void beforeEach() throws SQLException {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:h2:mem:conference_server;DB_CLOSE_DELAY=-1", "sa", "sa");
        flyway.setLocations("filesystem:src/main/resources/db/migration");
        flyway.clean();
        flyway.migrate();
        Orm.connection = DriverManager.getConnection(
                "jdbc:h2:mem:conference_server;DB_CLOSE_DELAY=-1", "sa", "sa");
        talkModel = ControllerTestUtils.createTalkModel(ControllerTestUtils.createTopicModel());
        trackModel = ControllerTestUtils.createTrackModel(ControllerTestUtils.createDayModel(ControllerTestUtils.createConferenceModel()));
    }

    @Test
    public void shouldReturnAllTimeslots() {
        TimeslotModel model = ControllerTestUtils.createTimeslotModel(talkModel, trackModel);
        TimeslotsController controller = new TimeslotsController(
                ControllerTestUtils.createHttpRequest(HttpMethod.GET, "/timeslots"));
        HttpResponse response = controller.index();
        JsonArray timeslotList = ControllerTestUtils.modelListFromResponse(response);

        assertThat(timeslotList.size()).isEqualTo(1);

        JsonObject timeslot = timeslotList.get(0).getAsJsonObject();
        assertThat(timeslot.get("id").getAsString()).isEqualTo(model.getState().getId().toString());
        assertThat(timeslot.get("talk_id").getAsString()).isEqualTo(model.getState().getTalk_id().toString());
        assertThat(timeslot.get("track_id").getAsString()).isEqualTo(model.getState().getTrack_id().toString());
    }

    @Test
    public void shouldReturnOneTimeslot() {
        TimeslotModel model = ControllerTestUtils.createTimeslotModel(talkModel, trackModel);
        TimeslotsController controller = new TimeslotsController(
                ControllerTestUtils.createHttpRequest(HttpMethod.GET, "/timeslots/" + model.getState().getId()));
        HttpResponse response = controller.show();
        JsonObject timeslot = response.getJson().getAsJsonObject().get("value").getAsJsonObject();

        assertThat(UUID.fromString(timeslot.get("id").getAsString())).isEqualTo(model.getState().getId());
        assertThat(timeslot.get("talk_id").getAsString()).isEqualTo(model.getState().getTalk_id().toString());
        assertThat(timeslot.get("track_id").getAsString()).isEqualTo(model.getState().getTrack_id().toString());
    }

    @Test
    public void shouldCreateOneTimeslot() {
        HttpRequest request = ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/timeslots");
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody(ControllerTestUtils.modelToJson(
                new Timeslot(talkModel.getState().getId(), trackModel.getState().getId(), 5)));
        TimeslotsController controller = new TimeslotsController(request);
        HttpResponse response = controller.create();
        Timeslot timeslot = ControllerTestUtils.jsonToModel(response, Timeslot.class);

        assertThat(timeslot.getSlot_index()).isEqualTo(5);
        assertThat(timeslot.getTalk_id()).isEqualTo(talkModel.getState().getId());
        assertThat(timeslot.getTrack_id()).isEqualTo(trackModel.getState().getId());
    }

    @Test
    public void shouldUpdateOneTimeslot() {
        TimeslotModel timeslotModel = ControllerTestUtils.createTimeslotModel(talkModel, trackModel);
        HttpRequest request = ControllerTestUtils.createHttpRequest(
                HttpMethod.PUT, "/timeslots/" + timeslotModel.getState().getId());
        request.getHeaders().put("Content-Type", "application/json");
        timeslotModel.getState().setSlot_index(15);
        request.setBody(ControllerTestUtils.modelToJson(timeslotModel.getState()));
        TimeslotsController controller = new TimeslotsController(request);
        HttpResponse response = controller.update();
        Timeslot timeslot = ControllerTestUtils.jsonToModel(response, Timeslot.class);

        assertThat(timeslot.getId()).isEqualTo(timeslotModel.getDbState().getId());
        assertThat(timeslot.getSlot_index()).isNotEqualTo(timeslotModel.getDbState().getSlot_index());
        assertThat(timeslot.getSlot_index()).isEqualTo(15);
    }

    @Test
    public void shouldDeleteOneTimeslot() {
        TimeslotModel model = ControllerTestUtils.createTimeslotModel(talkModel, trackModel);
        TimeslotsController controller = new TimeslotsController(
                ControllerTestUtils.createHttpRequest(HttpMethod.DELETE, "/timeslots/" + model.getState().getId()));
        HttpResponse response = controller.destroy();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);

        response = controller.show();

        assertThat(response.getJson().toString()).isEqualTo("{\"value\":{\"data\":\"No results\"}}");
    }

    @Test
    public void shouldReturnBadRequestWhenUuidIsWrongFormat() {
        // Show
        TimeslotsController controller = new TimeslotsController(
                ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/tracks/notaUUID"));
        HttpResponse response = controller.show();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BadRequest);

        // Update
        controller = new TimeslotsController(
                ControllerTestUtils.createHttpRequest(HttpMethod.PUT, "/tracks/notaUUID"));
        response = controller.update();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BadRequest);

        // Destroy
        controller = new TimeslotsController(
                ControllerTestUtils.createHttpRequest(HttpMethod.DELETE, "/tracks/notaUUID"));
        response = controller.destroy();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BadRequest);
    }

    @Test
    public void shouldReturnUnprocessableWithWrongJsonFormatOnCreate() {
        HttpRequest request = ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/timeslots");
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{\"timeslot\"\" <-- missing colon\"}");
        TimeslotsController controller = new TimeslotsController(request);
        HttpResponse response = controller.create();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertTrue(response.getJson().getAsJsonObject()
                           .get("error").getAsJsonObject().get("message").getAsString()
                           .contains("com.google.gson.stream.MalformedJsonException"));
    }

    @Test
    public void shouldRespondWithViolationsOnCreate() {
        HttpRequest request = ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/timeslots");
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{}");
        TimeslotsController controller = new TimeslotsController(request);
        HttpResponse response = controller.create();
        JsonObject errorResponse = ControllerTestUtils.stringToJsonObject(response.getBody());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertTrue(errorResponse.get("errors").toString().contains("\"talk_id\":\"must be a valid UUID\""));
        assertTrue(errorResponse.get("errors").toString().contains("\"track_id\":\"must be a valid UUID\""));
    }

    @Test
    public void shouldReturnUnprocessableWithWrongJsonFormatOnUpdate() {
        HttpRequest request = ControllerTestUtils.createHttpRequest(
                HttpMethod.PUT, "/timeslots/" + UUID.randomUUID());
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{\"timeslot\"\" <-- missing colon\"}");
        TimeslotsController controller = new TimeslotsController(request);
        HttpResponse response = controller.update();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertTrue(response.getJson().getAsJsonObject()
                           .get("error").getAsJsonObject().get("message").getAsString()
                           .contains("com.google.gson.stream.MalformedJsonException"));
    }

    @Test
    public void shouldRespondWithViolationsOnUpdate() {
        TimeslotModel model = ControllerTestUtils.createTimeslotModel(talkModel, trackModel);
        HttpRequest request = ControllerTestUtils.createHttpRequest(
                HttpMethod.PUT, "/timeslots/" + model.getState().getId());
        request.getHeaders().put("Content-Type", "application/json");
        Timeslot timeslot = model.getState();
        timeslot.setSlot_index(-1);
        request.setBody(ControllerTestUtils.modelToJson(timeslot));
        TimeslotsController controller = new TimeslotsController(request);
        HttpResponse response = controller.update();
        JsonObject errorResponse = ControllerTestUtils.stringToJsonObject(response.getBody());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertThat(errorResponse.get("errors").toString()).isEqualTo(
                "{\"slot_index\":\"must be greater than or equal to 0\"}");
    }

    @Test
    public void shouldReturnNoResultsWithIncorrectUuid() {
        TimeslotsController controller = new TimeslotsController(
                ControllerTestUtils.createHttpRequest(HttpMethod.GET, "/timeslots/" + UUID.randomUUID()));
        HttpResponse response = controller.show();

        assertThat(response.getJson().toString()).isEqualTo("{\"value\":{\"data\":\"No results\"}}");
    }
}