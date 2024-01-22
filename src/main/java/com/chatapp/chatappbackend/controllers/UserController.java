package com.chatapp.chatappbackend.controllers;

import com.chatapp.chatappbackend.models.User;
import com.chatapp.chatappbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @PostMapping("/addUser")
    public ResponseEntity addUser(@RequestBody User user) {
        if (userRepository.findByUserId(user.getUserId()) != null) {
            return new ResponseEntity("User already exists", null, HttpStatus.IM_USED);
        }
        return new ResponseEntity(userRepository.save(user), null, HttpStatus.OK);
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

        return new ResponseEntity( mongoTemplate.upsert(query, update, User.class, "users"), null, HttpStatus.ACCEPTED);
    }
}
