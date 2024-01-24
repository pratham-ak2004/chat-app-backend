package com.chatapp.chatappbackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.jws.soap.SOAPBinding;
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
}
