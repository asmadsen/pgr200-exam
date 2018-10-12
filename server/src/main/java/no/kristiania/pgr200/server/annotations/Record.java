package no.kristiania.pgr200.server.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Record {
    String type();
    String column();
}
