package no.kristiania.pgr200.commandline;

public interface ConferenceClientCommand {
    abstract ConferenceClientCommand decode(CommandOptions options);
}
