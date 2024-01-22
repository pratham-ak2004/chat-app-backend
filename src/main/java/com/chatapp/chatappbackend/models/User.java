package com.chatapp.chatappbackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "users")
public class User {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("friendsIds")
    List<String> friendsIds;
}
