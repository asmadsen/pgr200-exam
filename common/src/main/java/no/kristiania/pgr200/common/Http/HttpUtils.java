package no.kristiania.pgr200.common.Http;

import java.util.Arrays;
import java.util.List;

public class HttpUtils {
    public static String capitalizeHeaderKey(String headerKey) {
        List<String> parts = Arrays.asList(headerKey.split("-"));
        return String.join("-", parts.parallelStream().map(part -> {
            return part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
        }).toArray(String[]::new));
    }
}