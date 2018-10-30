package no.kristiania.pgr200.commandline;

import no.kristiania.pgr200.commandline.exceptions.MissingOptionsException;

import java.util.HashMap;
import java.util.Map;

public class CommandOptions {

    private Map<String, String> options = new HashMap<>();

    public CommandOptions(String[] options) {
        boolean skipNext = false;
        for (int i = 0; i < options.length; i++) {
            if (skipNext) {
                skipNext = false;
                continue;
            }
            String optionKey = options[i];
            if (optionKey.charAt(0) == '-') {
                String optionsValue = options[i + 1];
                this.options.put(optionKey.replaceFirst("^-", ""), optionsValue);
                skipNext = true;
            }
        }
    }

    public String get(String optionKey) {
        if (!this.options.containsKey(optionKey)) {
            throw new MissingOptionsException("Option with name '" + optionKey + "' doesn't exist");
        }
        return this.options.get(optionKey);
    }
}
