package no.kristiania.pgr200.commandline;

import java.util.Arrays;

public class ConferenceCliClient {
    public ConferenceClientCommand decodeCommand(String[] strings) {
        String commandString = strings[0];
        ConferenceClientCommand command;
        switch (commandString.toLowerCase()) {
            case "add": {
                command = new AddTalkCommand();
                break;
            }
            default: {
                throw new RuntimeException("Command not found");
            }
        }
        CommandOptions options = new CommandOptions(Arrays.copyOfRange(strings, 1, strings.length));

        return command.decode(options);
    }
}
