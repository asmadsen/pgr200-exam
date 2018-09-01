package no.kristiania.pgr200.commandline.Http;

import java.util.*;

public class ResponseTestUtils {
    public static Map<String, String> TransformKeys(Map<String, String> input) {
        Map<String, String> output = new HashMap<>();
        Iterator<String> keys = input.keySet().iterator();
        Iterator<String> values = input.values().iterator();
        while (keys.hasNext() && values.hasNext()) {
            output.put(ResponseTestUtils.CapitalizeHeaderKey(keys.next()), values.next());
        }
        return output;
    }

    private static String CapitalizeHeaderKey(String headerKey) {
        List<String> parts = Arrays.asList(headerKey.split("-"));
        return String.join("-", parts.parallelStream().map(part -> {
            return part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
        }).toArray(String[]::new));
    }
}
