package com.lab4.demo.comment.mongoDatabase;

import java.util.logging.Logger;

import com.lab4.demo.comment.model.Comment;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDB {
    private static final Logger LOG = Logger.getLogger(MongoDB.class.getName());

    /** DB host. */
    public static final String DB_HOST = "localhost";
    /** DB port. */
    public static final int DB_PORT = 27017;
    /** DB name. */
    public static final String DB_NAME = "sampleDB";
    /** Shared instance. */
    private static final MongoDB INSTANCE = new MongoDB();
    /** MongoClient instance. */
    private final MongoClient mongoClient;
    /** MongoDatabase instance. */
    private final MongoDatabase database;

    private MongoDB() {
        MongoClient client = MongoClients.create("mongodb+srv://root:root@cluster0.v88ej.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");

        MongoDatabase db = client.getDatabase("sampleDB");

        MongoCollection<Document> col = db.getCollection("sampleCollection");

        this.mongoClient = client;
        this.database = db;
        LOG.info("Connection to database '" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "' initialized");
    }

    public static MongoDB instance() {
        return INSTANCE;
    }

    /**
     * @return the database
     */
    public MongoDatabase getDatabase() {
        return database;
    }

    /**
     * @return the mongoClient
     */
    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoCollection<Document> getCollection() {
        return database.getCollection("sampleCollection");
    }
//    public static void main(String[] args) {
//        Comment comment = new Comment();
//        comment.setContent("Aesop");
//        comment.setUserId(1L);
//        comment.setTrackId(1L);
//        MongoClient client = MongoClients.create("mongodb+srv://root:root@cluster0.v88ej.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
//
//        MongoDatabase db = client.getDatabase("sampleDB");
//        MongoCollection<Document> col = db.getCollection("sampleCollection");
//
//        col.insertOne(new Document("content", comment.getContent()).append("userId", comment.getUserId()).append("trackId", comment.getTrackId()));
//
//
//    }
}
