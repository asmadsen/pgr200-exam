package no.kristiania.pgr200.commandline.utils;

import no.kristiania.pgr200.common.http.HttpClient;

public class Utils {

    private static HttpClient httpClient;

    public static HttpClient getHttpClient() {
        if (Utils.httpClient == null) {
            Utils.httpClient = new HttpClient();
        }
        return Utils.httpClient;
    }
}
