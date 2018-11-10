package no.kristiania.pgr200.server.controllers;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.http.HttpMethod;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.common.models.BaseModel;
import no.kristiania.pgr200.server.models.*;

import java.sql.Date;
import java.time.LocalDate;

public class ControllerTestUtils {

    public static ConferenceModel createConferenceModel() {
        ConferenceModel conferenceModel = new ConferenceModel("MobileEra");
        conferenceModel.save();
        return conferenceModel;
    }

    public static DayModel createDayModel(ConferenceModel conferenceModel) {
        DayModel dayModel = new DayModel(Date.valueOf(LocalDate.now()).toString(), conferenceModel.getState().getId());
        dayModel.save();
        return dayModel;
    }

    public static TrackModel createTrackModel(DayModel dayModel) {
        TrackModel trackModel = new TrackModel(null, dayModel.getState().getId());
        trackModel.save();
        return trackModel;
    }

    public static TimeslotModel createTimeslotModel(TalkModel talkModel, TrackModel trackModel) {
        TimeslotModel timeslot = new TimeslotModel(talkModel.getState().getId(),
                                                   trackModel.getState().getId(),
                                                   new Faker().number().numberBetween(0, 10));
        timeslot.save();
        return timeslot;
    }

    public static TalkModel createTalkModel(TopicModel topicModel) {
        TalkModel talkModel = new TalkModel(new Faker().lordOfTheRings().character(),
                                            new Faker().ancient().hero(),
                                            topicModel.getPrimaryKeyValue());
        talkModel.save();
        return talkModel;
    }

    public static TopicModel createTopicModel() {
        TopicModel topicModel = new TopicModel(new Faker().chuckNorris().fact());
        topicModel.save();
        return topicModel;
    }

    public static HttpRequest createHttpRequest(HttpMethod method, String uri) {
        HttpRequest request = new HttpRequest();
        request.setHttpMethod(method);
        request.setUri(uri);
        return request;
    }

    public static JsonArray modelListFromResponse(HttpResponse response) {
        return response.getJson().getAsJsonObject().get("values").getAsJsonArray();
    }

    public static JsonObject stringToJsonObject(String json) {
        return new Gson().fromJson(json, JsonObject.class);
    }

    public static <T> String modelToJson(T model) {
        return new GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(model);
    }

    public static <T extends BaseModel> T jsonToModel(HttpResponse response, Class<T> tClass) {
        return new Gson().fromJson(response.getJson().getAsJsonObject().get("value").toString(), tClass);
    }
}
