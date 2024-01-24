package com.chatapp.chatappbackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "sockets")
@Data
public class Socket {

    @Id
    @JsonProperty("_id")
    private String id;

    @JsonProperty("participants")
    private List<String> participants;

}
