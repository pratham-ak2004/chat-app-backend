package com.chatapp.chatappbackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chats")
@Data
public class Chat {

    @Id
    @JsonProperty("_id")
    private String id;

    @JsonProperty("content")
    private String content;

    @JsonProperty("sender")
    private String sender;

    @JsonProperty("socketId")
    private String socketId;

    @JsonProperty("timeStamp")
    private long timeStamp;

}
