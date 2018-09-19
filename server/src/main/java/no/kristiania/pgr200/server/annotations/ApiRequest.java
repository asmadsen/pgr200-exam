package no.kristiania.pgr200.server.annotations;

import no.kristiania.pgr200.common.Http.HttpMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ApiRequest {
    HttpMethod action();
    String route();
    String id() default "Method not allowed";
}
