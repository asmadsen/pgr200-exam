package no.kristiania.pgr200.commandline.exceptions;

public class MissingOptionsException extends RuntimeException {
    public MissingOptionsException(String message) {
        super(message);
    }
}
