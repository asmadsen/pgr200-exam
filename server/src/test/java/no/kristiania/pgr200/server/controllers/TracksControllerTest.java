package no.kristiania.pgr200.server.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.http.HttpMethod;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.common.http.HttpStatus;
import no.kristiania.pgr200.common.models.Track;
import no.kristiania.pgr200.orm.Orm;
import no.kristiania.pgr200.server.models.ConferenceModel;
import no.kristiania.pgr200.server.models.DayModel;
import no.kristiania.pgr200.server.models.TrackModel;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertTrue;

public class TracksControllerTest {

    private ConferenceModel conferenceModel;
    private DayModel dayModel;

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
        dayModel = ControllerTestUtils.createDayModel(conferenceModel);
    }

    @Test
    public void shouldReturnAllTracks() {
        TrackModel model = ControllerTestUtils.createTrackModel(dayModel);
        TracksController controller = new TracksController(
                ControllerTestUtils.createHttpRequest(HttpMethod.GET, "/tracks"));
        HttpResponse response = controller.index();
        JsonArray trackList = ControllerTestUtils.modelListFromResponse(response);

        assertThat(trackList.size()).isEqualTo(1);

        JsonObject track = trackList.get(0).getAsJsonObject();
        assertThat(track.get("id").getAsString()).isEqualTo(model.getState().getId().toString());
        assertThat(track.get("day_id").getAsString()).isEqualTo(model.getState().getDay_id().toString());
    }

    @Test
    public void shouldReturnOneTrack() {
        TrackModel model = ControllerTestUtils.createTrackModel(dayModel);
        TracksController controller = new TracksController(
                ControllerTestUtils.createHttpRequest(HttpMethod.GET, "/tracks/" + model.getState().getId()));
        HttpResponse response = controller.show();
        JsonObject track = response.getJson().getAsJsonObject().get("value").getAsJsonObject();
        assertThat(UUID.fromString(track.get("id").getAsString())).isEqualTo(model.getState().getId());
        assertThat(track.get("day_id").getAsString()).isEqualTo(model.getState().getDay_id().toString());
    }

    @Test
    public void shouldCreateOneTrack() {
        HttpRequest request = ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/tracks");
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody(ControllerTestUtils.modelToJson(new Track(null, dayModel.getState().getId())));
        TracksController controller = new TracksController(request);
        HttpResponse response = controller.create();
        Track track = ControllerTestUtils.jsonToModel(response, Track.class);

        assertThat(track.getDay_id()).isEqualTo(dayModel.getState().getId());
    }

    @Test
    public void shouldUpdateOneTrack() {
        DayModel newDayModel = ControllerTestUtils.createDayModel(conferenceModel);
        TrackModel trackModel = ControllerTestUtils.createTrackModel(dayModel);
        HttpRequest request = ControllerTestUtils.createHttpRequest(
                HttpMethod.PUT, "/tracks/" + trackModel.getState().getId());
        request.getHeaders().put("Content-Type", "application/json");
        trackModel.getState().setDay_id(newDayModel.getPrimaryKeyValue());
        request.setBody(ControllerTestUtils.modelToJson(trackModel.getState()));
        TracksController controller = new TracksController(request);
        HttpResponse response = controller.update();
        Track track = ControllerTestUtils.jsonToModel(response, Track.class);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);
        assertThat(track.getDay_id()).isNotEqualTo(dayModel.getDbState().getId());
        assertThat(track.getDay_id()).isEqualTo(newDayModel.getPrimaryKeyValue());
    }

    @Test
    public void shouldDeleteOneTrack() {
        TrackModel model = ControllerTestUtils.createTrackModel(dayModel);
        TracksController controller = new TracksController(
                ControllerTestUtils.createHttpRequest(HttpMethod.DELETE, "/tracks/" + model.getState().getId()));
        HttpResponse response = controller.destroy();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);

        response = controller.show();

        assertThat(response.getJson().toString()).isEqualTo("{\"value\":{\"data\":\"No results\"}}");
    }

    @Test
    public void shouldReturnBadRequestWhenUuidIsWrongFormat() {
        // Show
        TracksController controller = new TracksController(
                ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/tracks/notaUUID"));
        HttpResponse response = controller.show();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BadRequest);

        // Update
        controller = new TracksController(
                ControllerTestUtils.createHttpRequest(HttpMethod.PUT, "/tracks/notaUUID"));
        response = controller.update();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BadRequest);

        // Destroy
        controller = new TracksController(
                ControllerTestUtils.createHttpRequest(HttpMethod.DELETE, "/tracks/notaUUID"));
        response = controller.destroy();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BadRequest);
    }

    @Test
    public void shouldReturnUnprocessableWithWrongJsonFormatOnCreate() {
        HttpRequest request = ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/tracks");
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{\"day_id\"\" <-- missing colon\"}");
        TracksController controller = new TracksController(request);
        HttpResponse response = controller.create();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertTrue(response.getJson().getAsJsonObject()
                           .get("error").getAsJsonObject().get("message").getAsString()
                           .contains("com.google.gson.stream.MalformedJsonException"));
    }

    @Test
    public void shouldRespondWithViolationsOnCreate() {
        HttpRequest request = ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/tracks");
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody(ControllerTestUtils.modelToJson(new Track()));
        TracksController controller = new TracksController(request);
        HttpResponse response = controller.create();

        JsonObject errorResponse = ControllerTestUtils.stringToJsonObject(response.getBody());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertThat(errorResponse.get("errors").toString())
                .isEqualTo("{\"day_id\":\"must be a valid UUID\"}");
    }

    @Test
    public void shouldReturnUnprocessableWithWrongJsonFormatOnUpdate() {
        TrackModel model = ControllerTestUtils.createTrackModel(dayModel);
        HttpRequest request = ControllerTestUtils.createHttpRequest(
                HttpMethod.PUT, "/tracks/" + model.getState().getId());
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{\"title\"\"Java Track\"}");
        TracksController controller = new TracksController(request);
        HttpResponse response = controller.update();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertTrue(response.getJson().getAsJsonObject()
                           .get("error").getAsJsonObject().get("message").getAsString()
                           .contains("com.google.gson.stream.MalformedJsonException"));
    }

    @Test
    public void shouldRespondWithViolationsOnUpdate() {
        TrackModel model = ControllerTestUtils.createTrackModel(dayModel);
        HttpRequest request = ControllerTestUtils.createHttpRequest(
                HttpMethod.PUT, "/tracks/" + model.getState().getId());
        request.getHeaders().put("Content-Type", "application/json");
        Track track = model.getState();
        track.setDay_id(null);
        request.setBody(ControllerTestUtils.modelToJson(track));
        TracksController controller = new TracksController(request);
        HttpResponse response = controller.update();
        JsonObject errorResponse = ControllerTestUtils.stringToJsonObject(response.getBody());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertThat(errorResponse.get("errors").toString()).isEqualTo("{\"day_id\":\"must be a valid UUID\"}");
    }

}