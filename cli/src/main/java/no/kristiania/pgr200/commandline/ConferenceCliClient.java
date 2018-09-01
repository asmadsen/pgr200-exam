package no.kristiania.pgr200.commandline;

import no.kristiania.pgr200.commandline.Commands.ConferenceClientCommand;
import no.kristiania.pgr200.commandline.Exceptions.DecodeCommandException;
import no.kristiania.pgr200.commandline.Http.HttpClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConferenceCliClient {
    private Map<String, Class<? extends ConferenceClientCommand>> commands = new HashMap<>();
    private HttpClient client;

    public ConferenceCliClient(HttpClient client) {
        this.client = client;
    }

    public static void main(String[] args) {
        System.out.println(String.join(", ", args));
    }

    public ConferenceClientCommand decodeCommand(String[] strings) {
        String commandString = strings[0].toLowerCase();
        ConferenceClientCommand command;
        if (this.commands.containsKey(commandString)) {
            try {
                command = this.commands.get(commandString).newInstance().withHttpClient(this.client);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodeCommandException();
            }
        } else {
            throw new DecodeCommandException();
        }
        CommandOptions options = new CommandOptions(Arrays.copyOfRange(strings, 1, strings.length));

        return command.execute(options);
    }

    public void register(String commandName, Class<? extends ConferenceClientCommand> command) {
        this.commands.put(commandName, command);
    }
}
