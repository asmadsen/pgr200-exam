package no.kristiania.pgr200.server;

import com.github.javafaker.Faker;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.http.HttpMethod;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.common.http.HttpStatus;
import no.kristiania.pgr200.orm.Orm;
import no.kristiania.pgr200.server.controllers.TalksController;
import no.kristiania.pgr200.server.models.TalkModel;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TalksControllerTest {

    HttpRequest request = new HttpRequest();

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
    public void shouldReturnAllTalks() {
        TalkModel model = new TalkModel(new Faker().lorem().sentence(), new Faker().lorem().paragraph());
        model.save();
        request.setHttpMethod(HttpMethod.GET);
        request.setUri("/talks");
        TalksController controller = new TalksController(request);
        HttpResponse response = controller.index();
        JsonObject talks = response.getJson().getAsJsonObject();
        JsonArray talkList = talks.get("values").getAsJsonArray();
        assertThat(talkList.size()).isEqualTo(1);
        JsonObject talk = talkList.get(0).getAsJsonObject();
        assertThat(UUID.fromString(talk.get("id").getAsString())).isEqualTo(model.getState().getId());
        assertThat(talk.get("title").getAsString()).isEqualTo(model.getState().getTitle());
        assertThat(talk.get("description").getAsString()).isEqualTo(model.getState().getDescription());
    }

    @Test
    public void shouldReturnOneTalk() {
        TalkModel model = new TalkModel(new Faker().lorem().sentence(), new Faker().lorem().paragraph());
        model.save();
        request.setHttpMethod(HttpMethod.GET);
        request.setUri("/talks/" + model.getState().getId().toString());
        TalksController controller = new TalksController(request);
        HttpResponse response = controller.show();
        JsonObject talk = response.getJson().getAsJsonObject().get("value").getAsJsonObject();
        assertThat(UUID.fromString(talk.get("id").getAsString())).isEqualTo(model.getState().getId());
        assertThat(talk.get("title").getAsString()).isEqualTo(model.getState().getTitle());
        assertThat(talk.get("description").getAsString()).isEqualTo(model.getState().getDescription());
    }

    @Test
    public void shouldCreateOneTalk() {
        request.setHttpMethod(HttpMethod.POST);
        request.setUri("/talks/");
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{" +
                "\"title\": \"Java Talk\"," +
                "\"description\": \"How to use generics\"" +
                "}");
        TalksController controller = new TalksController(request);
        HttpResponse response = controller.create();
        JsonObject talk = response.getJson().getAsJsonObject().get("value").getAsJsonObject();
        assertThat(talk.get("title").getAsString()).isEqualTo("Java Talk");
        assertThat(talk.get("description").getAsString()).isEqualTo("How to use generics");
    }

    @Test
    public void shouldUpdateOneTalk() {
        TalkModel model = new TalkModel(new Faker().lorem().sentence(), new Faker().lorem().paragraph());
        model.save();
        request.setHttpMethod(HttpMethod.PUT);
        request.setUri("/talks/" + model.getState().getId().toString());
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{" +
                "\"title\": \"Java Talk\"," +
                "\"description\": \"How to use generics\"" +
                "}");
        TalksController controller = new TalksController(request);
        HttpResponse response = controller.update();
        JsonObject talk = response.getJson().getAsJsonObject().get("value").getAsJsonObject();
        assertThat(talk.get("title").getAsString()).isEqualTo("Java Talk");
        assertThat(talk.get("description").getAsString()).isEqualTo("How to use generics");
        assertThat(talk.get("title").getAsString()).isNotEqualTo(model.getState().getTitle());
        assertThat(talk.get("description").getAsString()).isNotEqualTo(model.getState().getDescription());
    }

    @Test
    public void shouldDeleteOneTalk() {
        TalkModel model = new TalkModel(new Faker().lorem().sentence(), new Faker().lorem().paragraph());
        model.save();
        request.setHttpMethod(HttpMethod.DELETE);
        request.setUri("/talks/" + model.getState().getId().toString());
        TalksController controller = new TalksController(request);
        HttpResponse response = controller.destroy();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);
        response = controller.show();
        assertThat(response.getJson().toString()).isEqualTo("{\"value\":{\"data\":\"No results\"}}");
    }

    @Test
    public void shouldReturnBadRequestWhenUuidIsWrongFormat() {
        // Show
        request.setHttpMethod(HttpMethod.POST);
        request.setUri("/talks/notaUUID");
        TalksController controller = new TalksController(request);
        HttpResponse response = controller.show();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BadRequest);

        // Update
        request.setHttpMethod(HttpMethod.PUT);
        request.setUri("/talks/notaUUID");
        controller = new TalksController(request);
        response = controller.update();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BadRequest);
    }

    @Test
    public void shouldReturnUnprocessableWithWrongJsonFormatOnCreate() {
        request.setHttpMethod(HttpMethod.POST);
        request.setUri("/talks");
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{" +
                "\"title\"\"Java Talk\"" +
                "}");
        TalksController controller = new TalksController(request);
        HttpResponse response = controller.create();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertThat(response.getBody()).isEqualTo("{\n" +
                "  \"error\": {\n" +
                "    \"message\": \"com.google.gson.stream.MalformedJsonException: Expected \\u0027:\\u0027 at line 1 column 10 path $.title\"\n" +
                "  }\n" +
                "}");
    }

    @Test
    public void shouldRespondWithViolationsOnCreate(){
        request.setHttpMethod(HttpMethod.POST);
        request.setUri("/talks");
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{" +
                "\"title\": \"Java Talk\"" +
                "}");
        TalksController controller = new TalksController(request);
        HttpResponse response = controller.create();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertThat(response.getBody()).isEqualTo("{\n" +
                "  \"errors\": {\n" +
                "    \"description\": \"must not be null\"\n" +
                "  }\n" +
                "}");
    }

    @Test
    public void shouldReturnUnprocessableWithWrongJsonFormatOnUpdate() {
        TalkModel model = new TalkModel(new Faker().lorem().sentence(), new Faker().lorem().paragraph());
        model.save();
        request.setHttpMethod(HttpMethod.PUT);
        request.setUri("/talks/" + model.getState().getId().toString());
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{" +
                "\"title\"\"Java Talk\"" +
                "}");
        TalksController controller = new TalksController(request);
        HttpResponse response = controller.update();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertThat(response.getBody()).isEqualTo("{\n" +
                "  \"error\": {\n" +
                "    \"message\": \"com.google.gson.stream.MalformedJsonException: Expected \\u0027:\\u0027 at line 1 column 10 path $.title\"\n" +
                "  }\n" +
                "}");
    }

    @Test
    public void shouldRespondWithViolationsOnUpdate(){
        TalkModel model = new TalkModel(new Faker().lorem().word(), new Faker().lorem().sentence());
        model.save();
        request.setHttpMethod(HttpMethod.PUT);
        request.setUri("/talks/" + model.getState().getId().toString());
        request.getHeaders().put("Content-Type", "application/json");
        request.setBody("{" +
                "\"title\": \"Java Talk\"" +
                "}");
        TalksController controller = new TalksController(request);
        HttpResponse response = controller.update();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UnprocessableEntity);
        assertThat(response.getBody()).isEqualTo("{\n" +
                "  \"errors\": {\n" +
                "    \"description\": \"must not be null\"\n" +
                "  }\n" +
                "}");
    }
}
