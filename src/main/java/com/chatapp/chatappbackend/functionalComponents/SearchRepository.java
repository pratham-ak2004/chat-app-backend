package com.chatapp.chatappbackend.functionalComponents;

import com.chatapp.chatappbackend.models.Socket;

public interface SearchRepository {

    Socket getSockets(String id1 , String id2);
}
