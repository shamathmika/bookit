package com._2.BookIt.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/debug")
public class DebugController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/mongo")
    public String checkMongoConnection() {
        return "✅ Connected to MongoDB database: " + mongoTemplate.getDb().getName();
    }

    @GetMapping("/mongo/collections")
    public List<String> getMongoCollections() {
        return mongoTemplate.getDb().listCollectionNames().into(new ArrayList<>());
    }

    @GetMapping("/mongo/uri")
    public String getMongoUri() {
        return System.getProperty("spring.data.mongodb.uri", "❌ Not set in system properties");
    }
}
