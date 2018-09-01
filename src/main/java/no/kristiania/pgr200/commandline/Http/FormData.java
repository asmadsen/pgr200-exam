package no.kristiania.pgr200.commandline.Http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;

public class FormData extends HashMap<String, String> {
    public String put(String key, Object value) {
        return super.put(key, String.valueOf(value));
    }

    @Override
    public String toString() {
        LinkedList<String> params = new LinkedList<>();
        try {
            for (Entry<String, String> entry: this.entrySet()) {
                params.push(URLEncoder.encode(entry.getKey(), "UTF-8")
                        + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
        }
        return String.join("&", params);
    }
}
