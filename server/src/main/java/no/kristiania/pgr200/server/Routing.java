package no.kristiania.pgr200.server;

import no.kristiania.pgr200.common.Http.HttpMethod;

import java.lang.reflect.Method;

public interface Routing {

    Method parseRoute() throws Exception;
}
