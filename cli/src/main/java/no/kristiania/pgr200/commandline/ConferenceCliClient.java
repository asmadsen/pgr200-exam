package no.kristiania.pgr200.commandline;

import no.kristiania.pgr200.commandline.commands.ConferenceClientCommand;
import no.kristiania.pgr200.commandline.exceptions.DecodeCommandException;
import no.kristiania.pgr200.common.Spinner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConferenceCliClient {
    private Map<String, Class<? extends ConferenceClientCommand>> commands = new HashMap<>();

    public ConferenceCliClient() {
    }

    public static void main(String[] args) {
        Spinner.spin(() -> {
            try {
                Thread.sleep(5L * 1000);
            } catch (InterruptedException ignored) {
            }
        }, "Uploading Talk");
    }

    public ConferenceClientCommand decodeCommand(String[] strings) {
        String commandString = strings[0].toLowerCase();
        ConferenceClientCommand command;
        if (this.commands.containsKey(commandString)) {
            try {
                command = this.commands.get(commandString).newInstance();
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
