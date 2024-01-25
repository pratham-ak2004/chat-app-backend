package com.chatapp.chatappbackend.controllers;

import com.chatapp.chatappbackend.models.BodyClasses;
import com.chatapp.chatappbackend.models.Chat;
import com.chatapp.chatappbackend.models.Socket;
import com.chatapp.chatappbackend.models.User;
import com.chatapp.chatappbackend.repository.ChatRepository;
import com.chatapp.chatappbackend.repository.SocketsRepository;
import com.chatapp.chatappbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "https://chatify-chat.vercel.app"})
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SocketsRepository socketsRepository;

    @Autowired
    private ChatRepository chatRepository;

    @PostMapping("/addUser")
    public ResponseEntity addUser(@RequestBody User user) {
        if (userRepository.findByUserId(user.getUserId()) != null) {
            Query query = new Query();
            query.addCriteria(Criteria.where("userId").is(user.getUserId()));

            Update update = new Update();
            update.set("userName", user.getUserName());
            update.set("userImg", user.getUserImg());

            mongoTemplate.updateFirst(query, update, User.class);

            User temp = userRepository.findByUserId(user.getUserId());
            return new ResponseEntity(temp, null, HttpStatus.IM_USED);
        } else {
            userRepository.save(user);
            return new ResponseEntity(userRepository.findByUserId(user.getUserId()), null, HttpStatus.OK);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity getAllUsers() {
        return new ResponseEntity(userRepository.findAll(), null, HttpStatus.OK);
    }

    @PostMapping("/addFriend")
    public ResponseEntity addFriend(@RequestBody User user) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(user.getUserId()));

        Update update = new Update();
        update.addToSet("friendsIds").each(user.getFriendsIds());

        mongoTemplate.upsert(query, update, User.class, "users");

        List<String> friends = user.getFriendsIds();
        for (String friend : friends) {
            query = new Query();
            query.addCriteria(Criteria.where("userId").is(friend));

            update = new Update();
            update.addToSet("friendsIds").each(user.getUserId());
            mongoTemplate.upsert(query, update, User.class, "users");
        }

        friends.add(user.getUserId());
        Collections.sort(friends);

        Socket socket = new Socket();
        socket.setParticipants(friends);

        if (socketsRepository.findByParticipants(friends) == null) {
            socketsRepository.save(socket);
        }

        return new ResponseEntity("Done", null, HttpStatus.ACCEPTED);
    }

    @GetMapping("/getUserDetails/{userId}")
    public ResponseEntity getUserDetails(@PathVariable String userId) {
        User user = userRepository.findByUserId(userId);
        return new ResponseEntity(user, null, HttpStatus.OK);
    }

    @PostMapping("/getUserDetailsWithSocket/{userId}")
    public ResponseEntity getUserDetailsWithSocket(@PathVariable String userId, @RequestBody BodyClasses.SocketBody body) {
        User user = userRepository.findByUserId(userId);
        BodyClasses.ResponseBody requestBody = new BodyClasses.ResponseBody();

        requestBody.setUser(user);

        if (socketsRepository.findByParticipantsContaining(userId) != null) {
            requestBody.setSocket(socketsRepository.findByParticipantsContaining(userId));
        }

        return new ResponseEntity(requestBody, null, HttpStatus.OK);
    }

    @GetMapping("/getChatMessages/{socketId}")
    public ResponseEntity getChatMessages(@PathVariable String socketId) {
        List<Chat> chatMessages = chatRepository.getChatBySocketId(socketId);

        Comparator<Chat> timeStampComparator = Comparator.comparingLong(Chat::getTimeStamp);
        Collections.sort(chatMessages, timeStampComparator);

        return new ResponseEntity(chatMessages, null, HttpStatus.OK);
    }

}
