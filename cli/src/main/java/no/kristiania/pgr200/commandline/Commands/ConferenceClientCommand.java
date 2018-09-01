package no.kristiania.pgr200.commandline.Commands;

import no.kristiania.pgr200.commandline.CommandOptions;
import no.kristiania.pgr200.commandline.ConferenceCliClient;
import no.kristiania.pgr200.commandline.Http.HttpClient;

public abstract class ConferenceClientCommand {

    protected HttpClient client = null;

    public static void register(ConferenceCliClient client) throws Exception {
        throw new Exception("Must be implemented in subclass");
    }

    public abstract ConferenceClientCommand execute(CommandOptions options);

    public ConferenceClientCommand withHttpClient(HttpClient client) {
        this.client = client;
        return this;
    }
}
