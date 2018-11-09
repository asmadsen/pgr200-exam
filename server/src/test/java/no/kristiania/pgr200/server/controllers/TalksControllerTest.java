package no.kristiania.pgr200.server.controllers;

import com.github.javafaker.Faker;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.http.HttpMethod;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.common.http.HttpStatus;
import no.kristiania.pgr200.common.models.Talk;
import no.kristiania.pgr200.orm.Orm;
import no.kristiania.pgr200.server.controllers.TalksController;
import no.kristiania.pgr200.server.models.ConferenceModel;
import no.kristiania.pgr200.server.models.TalkModel;
import no.kristiania.pgr200.server.models.TalkModel;
import no.kristiania.pgr200.server.models.TopicModel;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertTrue;

public class TalksControllerTest {

    private TopicModel topicModel;

    @Before
    public void beforeEach() throws SQLException {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:h2:mem:conference_server;DB_CLOSE_DELAY=-1", "sa", "sa");
        flyway.setLocations("filesystem:src/main/resources/db/migration");
        flyway.clean();
        flyway.migrate();
        Orm.connection = DriverManager.getConnection(
                "jdbc:h2:mem:conference_server;DB_CLOSE_DELAY=-1", "sa", "sa");
        topicModel = ControllerTestUtils.createTopicModel();
    }

    @Test
    public void shouldReturnAllTalks() {
        TalkModel model = ControllerTestUtils.createTalkModel(topicModel);
        TalksController controller = new TalksController(
                ControllerTestUtils.createHttpRequest(HttpMethod.GET, "/talks"));
        HttpResponse response = controller.index();
        JsonArray talkList = ControllerTestUtils.modelListFromResponse(response);

        assertThat(talkList.size()).isEqualTo(1);

        JsonObject talk = talkList.get(0).getAsJsonObject();
        assertThat(talk.get("id").getAsString()).isEqualTo(model.getState().getId().toString());
        assertThat(talk.get("topic_id").getAsString()).isEqualTo(model.getState().getTopic_id().toString());
    }

    @Test
    public void shouldReturnOneTalk() {
        TalkModel model = ControllerTestUtils.createTalkModel(topicModel);
        TalksController controller = new TalksController(
                ControllerTestUtils.createHttpRequest(HttpMethod.GET, "/talks/" + model.getState().getId()));
        HttpResponse response = controller.show();
        JsonObject talk = response.getJson().getAsJsonObject().get("value").getAsJsonObject();

        assertThat(UUID.fromString(talk.get("id").getAsString())).isEqualTo(model.getState().getId());
        assertThat(talk.get("topic_id").getAsString()).isEqualTo(model.getState().getTopic_id().toString());
    }

    @Test
    public void shouldCreateOneTalk() {
        HttpRequest request = ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/talks");
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody(ControllerTestUtils.modelToJson(
                new Talk("Java Generics", "How to use generics", topicModel.getState().getId())));
        TalksController controller = new TalksController(request);
        HttpResponse response = controller.create();
        Talk talk = ControllerTestUtils.jsonToModel(response, Talk.class);


        assertThat(talk.getTitle()).isEqualTo("Java Generics");
        assertThat(talk.getDescription()).isEqualTo("How to use generics");
        assertThat(talk.getTopic_id()).isEqualTo(topicModel.getState().getId());
    }

    @Test
    public void shouldUpdateOneTalk() {
        TalkModel talkModel = ControllerTestUtils.createTalkModel(topicModel);
        HttpRequest request = ControllerTestUtils.createHttpRequest(
                HttpMethod.PUT, "/talks/" + talkModel.getState().getId());
        request.getHeaders().put("Content-Type", "application/json");
        talkModel.getState().setDescription("Using generics the right way");
        request.setBody(ControllerTestUtils.modelToJson(talkModel.getState()));
        TalksController controller = new TalksController(request);
        HttpResponse response = controller.update();
        Talk talk = ControllerTestUtils.jsonToModel(response, Talk.class);

        assertThat(talk.getId()).isEqualTo(talkModel.getDbState().getId());
        assertThat(talk.getDescription()).isNotEqualTo(talkModel.getDbState().getDescription());
        assertThat(talk.getDescription()).isEqualTo("Using generics the right way");
    }

    @Test
    public void shouldDeleteOneTalk() {
        TalkModel model = ControllerTestUtils.createTalkModel(topicModel);
        TalksController controller = new TalksController(
                ControllerTestUtils.createHttpRequest(HttpMethod.DELETE, "/talks/" + model.getState().getId()));
        HttpResponse response = controller.destroy();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);

        response = controller.show();

        assertThat(response.getJson().toString()).isEqualTo("{\"value\":{\"data\":\"No results\"}}");
    }

    @Test
    public void shouldReturnBadRequestWhenUuidIsWrongFormat() {
        // Show
        TalksController controller = new TalksController(
                ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/tracks/notaUUID"));
        HttpResponse response = controller.show();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BadRequest);

        // Update
        controller = new TalksController(
                ControllerTestUtils.createHttpRequest(HttpMethod.PUT, "/tracks/notaUUID"));
        response = controller.update();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BadRequest);

        // Destroy
        controller = new TalksController(
                ControllerTestUtils.createHttpRequest(HttpMethod.DELETE, "/tracks/notaUUID"));
        response = controller.destroy();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BadRequest);
    }

    @Test
    public void shouldReturnUnprocessableWithWrongJsonFormatOnCreate() {
        HttpRequest request = ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/talks");
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{\"talk\"\" <-- missing colon\"}");
        TalksController controller = new TalksController(request);
        HttpResponse response = controller.create();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertTrue(response.getJson().getAsJsonObject()
                .get("error").getAsJsonObject().get("message").getAsString()
                .contains("com.google.gson.stream.MalformedJsonException"));
    }

    @Test
    public void shouldRespondWithViolationsOnCreate() {
        HttpRequest request = ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/talks");
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{}");
        TalksController controller = new TalksController(request);
        HttpResponse response = controller.create();
        JsonObject errorResponse = ControllerTestUtils.stringToJsonObject(response.getBody());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertTrue(errorResponse.get("errors").toString().contains("\"title\":\"must not be blank\""));
        assertTrue(errorResponse.get("errors").toString().contains("\"description\":\"must not be blank\""));
    }

    @Test
    public void shouldReturnUnprocessableWithWrongJsonFormatOnUpdate() {
        HttpRequest request = ControllerTestUtils.createHttpRequest(
                HttpMethod.PUT, "/talks/" + UUID.randomUUID());
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{\"talk\"\" <-- missing colon\"}");
        TalksController controller = new TalksController(request);
        HttpResponse response = controller.update();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertTrue(response.getJson().getAsJsonObject()
                .get("error").getAsJsonObject().get("message").getAsString()
                .contains("com.google.gson.stream.MalformedJsonException"));
    }

    @Test
    public void shouldRespondWithViolationsOnUpdate() {
        TalkModel model = ControllerTestUtils.createTalkModel(topicModel);
        HttpRequest request = ControllerTestUtils.createHttpRequest(
                HttpMethod.PUT, "/talks/" + model.getState().getId());
        request.getHeaders().put("Content-Type", "application/json");
        Talk talk = model.getState();
        talk.setDescription("");
        request.setBody(ControllerTestUtils.modelToJson(talk));
        TalksController controller = new TalksController(request);
        HttpResponse response = controller.update();
        JsonObject errorResponse = ControllerTestUtils.stringToJsonObject(response.getBody());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertThat(errorResponse.get("errors").toString()).isEqualTo("{\"description\":\"must not be blank\"}");
    }

    @Test
    public void shouldReturnNoResultsWithIncorrectUuid() {
        TalksController controller = new TalksController(
                ControllerTestUtils.createHttpRequest(HttpMethod.GET, "/talks/" + UUID.randomUUID()));
        HttpResponse response = controller.show();

        assertThat(response.getJson().toString()).isEqualTo("{\"value\":{\"data\":\"No results\"}}");
    }
}
