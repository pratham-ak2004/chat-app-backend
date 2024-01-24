package com.chatapp.chatappbackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

public class Chat {

    // id content sender socketid
    @Id
    @JsonProperty("_id")
    private String id;

    @JsonProperty("content")
    private String content;

    @JsonProperty("sender")
    private String sender;

    @JsonProperty("socketid")
    private String socketid;

}
