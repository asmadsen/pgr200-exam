package no.kristiania.pgr200.server;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ApiRequest {
    String action();
    String route();
    String id() default "Method not allowed";
}
