package no.kristiania.pgr200.commandline;

import java.util.HashMap;
import java.util.Map;

public class CommandOptions {

    private Map<String, String> options = new HashMap<>();

    public CommandOptions(String[] options) {
        for (int i = 0; i < options.length; i++) {
            String optionKey = options[i];
            if (optionKey.charAt(0) == '-') {
                String optionsValue = options[i + 1];
                this.options.put(optionKey.replaceFirst("^-", ""), optionsValue);
                i++;
            }
        }
    }

    public String get(String optionKey) {
        if (!this.options.containsKey(optionKey)) {
            throw new RuntimeException("Option with name '" + optionKey + "' doesn't exist");
        }
        return this.options.get(optionKey);
    }
}
