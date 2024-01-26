package com.chatapp.chatappbackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
public class BodyClasses {

    @Document
    @Data
    public static class SocketBody{

        @JsonProperty("participants")
        private List<String> participants;
    }

    @Document
    @Data
    public static class ResponseBody{

        @JsonProperty("socket")
        private Socket socket;

        @JsonProperty("user")
        private User user;
    }

    @Document
    @Data
    public static  class UserData {

        @JsonProperty("userId")
        private String userId;
    }

    @Document
    @Data
    public static class Response{

        @JsonProperty("User")
        private User user;

        @JsonProperty("socket")
        private Socket socket;
    }
}
