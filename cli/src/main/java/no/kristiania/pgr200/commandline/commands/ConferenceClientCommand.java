package no.kristiania.pgr200.commandline.commands;

import no.kristiania.pgr200.commandline.CommandOptions;
import no.kristiania.pgr200.commandline.ConferenceCliClient;

public abstract class ConferenceClientCommand {

    public static void register(ConferenceCliClient client) throws Exception {
        throw new Exception("Must be implemented in subclass");
    }

    public abstract ConferenceClientCommand execute(CommandOptions options);
}
