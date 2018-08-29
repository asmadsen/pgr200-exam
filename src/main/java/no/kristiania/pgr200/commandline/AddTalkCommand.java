package no.kristiania.pgr200.commandline;

public class AddTalkCommand implements ConferenceClientCommand {

    private String title;
    private String description;

    public AddTalkCommand withTitle(String title) {
        this.title = title;
        return this;
    }

    public AddTalkCommand withDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public ConferenceClientCommand decode(CommandOptions options) {
        this.title = options.get("title");
        this.description = options.get("description");
        return this;
    }
}
