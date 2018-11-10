package no.kristiania.pgr200.server.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.http.HttpMethod;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.common.http.HttpStatus;
import no.kristiania.pgr200.common.models.Topic;
import no.kristiania.pgr200.orm.Orm;
import no.kristiania.pgr200.server.models.TopicModel;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertTrue;

public class TopicsControllerTest {
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
    public void shouldReturnAllTopics() {
        TopicModel model = ControllerTestUtils.createTopicModel();
        TopicsController controller = new TopicsController(
                ControllerTestUtils.createHttpRequest(HttpMethod.GET, "/topics"));
        HttpResponse response = controller.index();
        JsonArray topicList = ControllerTestUtils.modelListFromResponse(response);

        assertThat(topicList.size()).isEqualTo(1);

        JsonObject topic = topicList.get(0).getAsJsonObject();
        assertThat(topic.get("id").getAsString()).isEqualTo(model.getState().getId().toString());
        assertThat(topic.get("topic").getAsString()).isEqualTo(model.getState().getTopic());
    }

    @Test
    public void shouldReturnOneTopic() {
        TopicModel model = ControllerTestUtils.createTopicModel();
        TopicsController controller = new TopicsController(
                ControllerTestUtils.createHttpRequest(HttpMethod.GET, "/topics/" + model.getState().getId()));
        HttpResponse response = controller.show();
        JsonObject topic = response.getJson().getAsJsonObject().get("value").getAsJsonObject();

        assertThat(UUID.fromString(topic.get("id").getAsString())).isEqualTo(model.getState().getId());
        assertThat(topic.get("topic").getAsString()).isEqualTo(model.getState().getTopic());
    }

    @Test
    public void shouldCreateOneTopic() {
        HttpRequest request = ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/topics");
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody(ControllerTestUtils.modelToJson(new Topic("Java generics")));
        TopicsController controller = new TopicsController(request);
        HttpResponse response = controller.create();
        Topic topic = ControllerTestUtils.jsonToModel(response, Topic.class);

        assertThat(topic.getTopic()).isEqualTo("Java generics");
    }

    @Test
    public void shouldUpdateOneTopic() {
        TopicModel topicModel = ControllerTestUtils.createTopicModel();
        HttpRequest request = ControllerTestUtils.createHttpRequest(
                HttpMethod.PUT, "/topics/" + topicModel.getState().getId());
        request.getHeaders().put("Content-Type", "application/json");
        topicModel.getState().setTopic("Algorithms");
        request.setBody(ControllerTestUtils.modelToJson(topicModel.getState()));
        TopicsController controller = new TopicsController(request);
        HttpResponse response = controller.update();
        Topic topic = ControllerTestUtils.jsonToModel(response, Topic.class);

        assertThat(topic.getId()).isEqualTo(topicModel.getState().getId());
        assertThat(topic.getTopic()).isNotEqualTo(topicModel.getDbState().getTopic());
        assertThat(topic.getTopic()).isEqualTo("Algorithms");
    }

    @Test
    public void shouldDeleteOneTopic() {
        TopicModel model = ControllerTestUtils.createTopicModel();
        TopicsController controller = new TopicsController(
                ControllerTestUtils.createHttpRequest(HttpMethod.DELETE, "/topics/" + model.getState().getId()));
        HttpResponse response = controller.destroy();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);

        response = controller.show();

        assertThat(response.getJson().toString()).isEqualTo("{\"value\":{\"data\":\"No results\"}}");
    }

    @Test
    public void shouldReturnBadRequestWhenUuidIsWrongFormat() {
        // Show
        TopicsController controller = new TopicsController(
                ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/tracks/notaUUID"));
        HttpResponse response = controller.show();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BadRequest);

        // Update
        controller = new TopicsController(
                ControllerTestUtils.createHttpRequest(HttpMethod.PUT, "/tracks/notaUUID"));
        response = controller.update();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BadRequest);

        // Destroy
        controller = new TopicsController(
                ControllerTestUtils.createHttpRequest(HttpMethod.DELETE, "/tracks/notaUUID"));
        response = controller.destroy();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BadRequest);
    }

    @Test
    public void shouldReturnUnprocessableWithWrongJsonFormatOnCreate() {
        HttpRequest request = ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/topics");
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{\"topic\"\" <-- missing colon\"}");
        TopicsController controller = new TopicsController(request);
        HttpResponse response = controller.create();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertTrue(response.getJson().getAsJsonObject()
                           .get("error").getAsJsonObject().get("message").getAsString()
                           .contains("com.google.gson.stream.MalformedJsonException"));
    }

    @Test
    public void shouldRespondWithViolationsOnCreate() {
        HttpRequest request = ControllerTestUtils.createHttpRequest(HttpMethod.POST, "/topics");
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{}");
        TopicsController controller = new TopicsController(request);
        HttpResponse response = controller.create();
        JsonObject errorResponse = ControllerTestUtils.stringToJsonObject(response.getBody());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertThat(errorResponse.get("errors").toString())
                .isEqualTo("{\"topic\":\"must not be blank\"}");
    }

    @Test
    public void shouldReturnUnprocessableWithWrongJsonFormatOnUpdate() {
        HttpRequest request = ControllerTestUtils.createHttpRequest(
                HttpMethod.PUT, "/topics/" + UUID.randomUUID());
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{\"topic\"\" <-- missing colon\"}");
        TopicsController controller = new TopicsController(request);
        HttpResponse response = controller.update();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertTrue(response.getJson().getAsJsonObject()
                           .get("error").getAsJsonObject().get("message").getAsString()
                           .contains("com.google.gson.stream.MalformedJsonException"));
    }

    @Test
    public void shouldRespondWithViolationsOnUpdate() {
        TopicModel model = ControllerTestUtils.createTopicModel();
        HttpRequest request = ControllerTestUtils.createHttpRequest(
                HttpMethod.PUT, "/topics/" + model.getState().getId());
        request.getHeaders().put("Content-Type", "application/json");
        Topic topic = model.getState();
        topic.setTopic(null);
        request.setBody(ControllerTestUtils.modelToJson(topic));
        TopicsController controller = new TopicsController(request);
        HttpResponse response = controller.update();
        JsonObject errorResponse = ControllerTestUtils.stringToJsonObject(response.getBody());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertThat(errorResponse.get("errors").toString()).isEqualTo("{\"topic\":\"must not be blank\"}");
    }

    @Test
    public void shouldReturnNoResultsWithIncorrectUuid() {
        TopicsController controller = new TopicsController(
                ControllerTestUtils.createHttpRequest(HttpMethod.GET, "/topics/" + UUID.randomUUID()));
        HttpResponse response = controller.show();

        assertThat(response.getJson().toString()).isEqualTo("{\"value\":{\"data\":\"No results\"}}");
    }
}
