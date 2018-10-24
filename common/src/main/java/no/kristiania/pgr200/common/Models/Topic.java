package no.kristiania.pgr200.common.Models;

import java.util.UUID;

public class Topic extends BaseModel<Topic>{

    protected UUID id;
    private String topic;


    @Override
    public boolean equals(Object other) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
