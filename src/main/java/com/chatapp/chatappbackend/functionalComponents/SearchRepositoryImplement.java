package com.chatapp.chatappbackend.functionalComponents;

import com.chatapp.chatappbackend.models.Socket;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SearchRepositoryImplement implements SearchRepository {

    @Autowired
    private MongoClient mongoClient;

    public Socket getSocketFromDocument(Document doc){

        ObjectId obj = doc.getObjectId("_id");
        String id = obj.toString();
        List<String> participants = doc.getList("participants" , String.class);

        Socket ans = new Socket();
        ans.setId(id);
        ans.setParticipants(participants);

        return ans;
    }

    @Override
    public Socket getSockets(String id1, String id2) {

        MongoDatabase database = mongoClient.getDatabase("ChatApp-Chit-Chat");
        MongoCollection<Document> collection = database.getCollection("sockets");

        AggregateIterable<Document> collected = collection.aggregate(Arrays.asList(
                new Document("$match",
                        new Document("participants",
                                new Document("$all", Arrays.asList(id1, id2)))
                )
        ));

        List<Socket> result = new ArrayList<>();

        MongoCursor<Document> iterator = collected.iterator();
        while (iterator.hasNext()) {
            Document document = iterator.next();
            result.add(getSocketFromDocument(document));
            System.out.println(document.toJson());
        }

        return  result.getFirst();
    }
}
