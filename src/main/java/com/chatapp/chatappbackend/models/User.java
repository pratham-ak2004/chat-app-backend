package com.chatapp.chatappbackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "users")
public class User {

    @Id
    @JsonProperty("_id")
    private String id;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("userImg")
    private String userImg;

    @JsonProperty("friendsIds")
    List<String> friendsIds;
}
