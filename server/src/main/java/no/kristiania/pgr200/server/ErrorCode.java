package no.kristiania.pgr200.server;

public class ErrorCode {

    private String code;
    private String developerMessage;
    private transient String id;


    public ErrorCode(String code, String id){
        this.code = code;
        this.id = id;
        validate();
    }

    public ErrorCode validate(){
        if(code.equals("400")) this.developerMessage = "Method not allowed";
        if(code.equals("422")) this.developerMessage = "No talk found with id: " + id;
        return this;
    }
}
