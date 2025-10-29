package com.chatapp.chatappbackend.controllers;

import com.chatapp.chatappbackend.functionalComponents.SearchRepository;
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

import java.util.*;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "https://chit-chat-chat.vercel.app"})
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

    @Autowired
    private SearchRepository searchRepository;

    @GetMapping("/ping")
    public ResponseEntity healthCheck() {
        return new ResponseEntity(true,null, HttpStatus.OK);
    }

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

    // Not Useable
    @PostMapping("/getUserDetailsWithSocket/{userId}")
    public ResponseEntity getUserDetailsWithSocket(@PathVariable String userId, @RequestBody BodyClasses.SocketBody body) {
        User user = userRepository.findByUserId(userId);
        BodyClasses.ResponseBody requestBody = new BodyClasses.ResponseBody();

        requestBody.setUser(user);
        //needs fixing
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

    @PostMapping("/getFriend/{friendId}")
    public ResponseEntity getFriend(@PathVariable String friendId , @RequestBody BodyClasses.UserData userId){
        User friendUser = userRepository.findByUserId(friendId);

        List<String > searchList = new ArrayList<>();
        searchList.add(friendId);
        searchList.add(userId.getUserId());

        Collections.sort(searchList);
        Socket soc = searchRepository.getSockets(searchList.get(0),searchList.get(1));

        BodyClasses.Response ans = new BodyClasses.Response();
        ans.setUser(friendUser);
        ans.setSocket(soc);

        return new ResponseEntity(ans , null , HttpStatus.OK);
    }

}
