package no.kristiania.pgr200.server;

public class Helpers {

    public static String capitalize(String string){
        StringBuilder str = new StringBuilder();
        String lowerCase = string.toLowerCase();
        return str.append(lowerCase.substring(0, 1).toUpperCase()).append(lowerCase.substring(1)).toString();
    }
}
