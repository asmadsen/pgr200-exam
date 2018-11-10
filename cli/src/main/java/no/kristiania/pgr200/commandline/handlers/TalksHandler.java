package no.kristiania.pgr200.commandline.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import no.kristiania.pgr200.commandline.interactive_commands.InteractiveCommand;
import no.kristiania.pgr200.commandline.interactive_commands.Prompt;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.common.http.HttpStatus;
import no.kristiania.pgr200.common.models.Talk;
import no.kristiania.pgr200.common.models.Topic;
import org.jline.reader.LineReader;
import org.slf4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static no.kristiania.pgr200.commandline.interactive_commands.Prompt.confirm;
import static no.kristiania.pgr200.commandline.interactive_commands.Prompt.input;
import static org.slf4j.LoggerFactory.getLogger;

public class TalksHandler extends AbstractHandler {
    private static Logger logger = getLogger(TalksHandler.class);
    private LineReader lineReader;
    private Topic[] topics;
    private Topic topic = new Topic("Create a new topic");

    public TalksHandler(LineReader lineReader) {
        this.lineReader = lineReader;
    }


    public void execute() {
        InteractiveCommand command = new InteractiveCommand(lineReader);
        String[] actions = {"List", "Add", "Update", "Delete"};

        command.pushPrompt(
                Prompt.list("action", "What action do you want to take?", actions)
                      .getPromptsAsPrompt()
        );

        command.prompt();

        String action = (String) command.getValues().get("action");


        switch (action.toLowerCase()) {
            case "list":
                this.list();
                break;
            case "add":
                this.add();
                break;
            case "update":
                this.update();
                break;
            case "delete":
                this.delete();
                break;
        }
    }

    private Talk promptTalk() {
        return this.promptTalk(new Talk());
    }

    private Talk promptTalk(Talk talk) {
        InteractiveCommand command = new InteractiveCommand(lineReader);

        topics = null;
        command.pushPrompt(
                input("title", "Talk title", talk.getTitle(), Talk.class)
                        .input("description", "Talk description", talk.getDescription(), Talk.class)
                        .confirm("addTopic", "Do you want to add a topic?", true)
                        .conditional(command1 -> {
                                         if (!((Boolean) command1.getValues().get("addTopic"))) return false;
                                         HttpResponse response = this.fetch("/api/topics");
                                         JsonElement json = response.getJson();
                                         List<Topic> _topics = new LinkedList<>();
                                         _topics.add(topic);
                                         if (response.getStatus() == HttpStatus.OK) {
                                             JsonArray jsonTopics = response.getJson().getAsJsonObject().getAsJsonArray("values");
                                             for (JsonElement jsonTopic : jsonTopics) {
                                                 _topics.add(gson.fromJson(jsonTopic, Topic.class));
                                             }
                                         }
                                         topics = _topics.toArray(new Topic[]{});
                                         return true;
                                     }, Prompt.list("topic", "Select a topic", () -> topics, Topic::getTopic)
                                              .conditional(command1 -> {
                                                  return ((Topic) command1.getValues().get("topic")).equals(this.topic);
                                              }, input("topic", "Topic name", Topic.class))
                        )
                        .getPromptsAsPrompt()
        );

        command.prompt();

        Map<String, Object> values = command.getValues();
        UUID id = talk.getId();
        talk = talk.newStateInstance();
        talk.setId(id);

        if ((Boolean) values.get("addTopic")) {
            Topic topic;

            if (values.get("topic") instanceof String) {
                topic = new Topic((String) values.get("topic"));

                HttpResponse response = this.insert("/api/topics", gson.toJsonTree(topic));
                if (response.getStatus() == HttpStatus.Created) {
                    topic = gson.fromJson(response.getJson().getAsJsonObject().get("value"), Topic.class);
                }
            } else {
                topic = (Topic) values.get("topic");
            }
            talk.setTopic_id(topic.getId());
        }


        talk.title = (String) values.get("title");
        talk.description = (String) values.get("description");

        return talk;
    }


    private void add() {
        Talk talk = this.promptTalk();

        this.insert(this.baseUrl(), gson.toJsonTree(talk));
    }

    private Talk selectEntryPrompt(String message, boolean confirm) {
        LinkedList<Talk> talks = new LinkedList<>();
        HttpResponse response = this.fetch(this.baseUrl());

        if (response.getStatus() == HttpStatus.OK) {
            JsonArray jsonTalks = response.getJson().getAsJsonObject().getAsJsonArray("values");
            for (JsonElement jsonTalk : jsonTalks) {
                talks.add(gson.fromJson(jsonTalk, Talk.class));
            }
        }

        if (talks.isEmpty()) {
            lineReader.getTerminal().writer().println("There is no talks available");
            return null;
        }

        Talk[] tArray = talks.toArray(new Talk[]{});

        InteractiveCommand command = new InteractiveCommand(lineReader);

        command.pushPrompt(Prompt.list("talk", message, tArray, Talk::getTitle).getPromptsAsPrompt());

        if (confirm) {
            command.pushPrompt(confirm("confirm", "Are you sure?", false).getPromptsAsPrompt());
        }

        command.prompt();

        return (Talk) command.getValues().get("talk");
    }

    private void update() {
        Talk talk = this.selectEntryPrompt("Which talk do you want to update?", false);

        if (talk == null) return;

        talk = this.promptTalk(talk);

        this.update(this.baseUrl(), gson.toJsonTree(talk));
    }


    private void delete() {
        Talk talk = this.selectEntryPrompt("Which talk do you want to delete?", true);
        if (talk == null) return;

        this.delete(this.baseUrl(), talk.getId().toString());
    }

    @Override
    protected String baseUrl() {
        return "/api/talks";
    }

    private void list() {
        HttpResponse response = this.fetch(this.baseUrl());
        JsonElement json = response.getJson();
        System.out.println(gson.toJson(json));
    }
}
