package com.phazerous.phazerous.db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.phazerous.phazerous.db.enums.CollectionType;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class DBManager {
    private final MongoDatabase database;
    private final MongoClient mongoClient;

    public DBManager() {
        String MONGO_DB_CONNECTION_STRING = "mongodb://localhost:27017";
        String MONGO_DB_NAME = "mc-db";

        ConnectionString connectionString = new ConnectionString(MONGO_DB_CONNECTION_STRING);
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder()
                .automatic(true).build());
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .uuidRepresentation(UuidRepresentation.STANDARD).build();

        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase(MONGO_DB_NAME);
    }


    public List<Document> getDocuments(CollectionType collectionType) {
        String collectionName = collectionType.getCollectionName();

        FindIterable<Document> documentsIterator = getCollection(collectionName).find();

        return documentsIterator.into(new ArrayList<>());
    }

    public List<Document> getDocuments(Bson query, CollectionType collectionType) {
        String collectionName = collectionType.getCollectionName();

        FindIterable<Document> documentsIterator = getCollection(collectionName).find(query);

        return documentsIterator.into(new ArrayList<>());
    }

    public Document getDocument(ObjectId id, CollectionType collectionType) {
        String collectionName = collectionType.getCollectionName();
        Document query = new Document("_id", id);

        return getCollection(collectionName).find(query).first();
    }

    public Document getDocument(Bson query, CollectionType collectionType) {
        String collectionName = collectionType.getCollectionName();

        return getCollection(collectionName).find(query).first();
    }

    public Document getDocument(Bson query, CollectionType collectionType, Bson projection) {
        String collectionName = collectionType.getCollectionName();

        return getCollection(collectionName).find(query).projection(projection)
                .first();
    }

    public void insertDocuments(List<Document> documents, CollectionType collectionType) {
        String collectionName = collectionType.getCollectionName();

        documents.forEach(it -> it.remove("_id"));

        MongoCollection<Document> collection = getCollection(collectionName);

        collection.insertMany(documents);
    }

    public void insertDocument(Document document, CollectionType collectionType) {
        document.remove("_id");

        String collectionName = collectionType.getCollectionName();

        MongoCollection<Document> collection = getCollection(collectionName);

        collection.insertOne(document);
    }

    public void deleteDocument(ObjectId documentId, CollectionType collectionType) {
        String collectionName = collectionType.getCollectionName();

        MongoCollection<Document> collection = getCollection(collectionName);

        collection.deleteOne(eq("_id", documentId));
    }

    public void deleteDocument(Bson query, CollectionType collectionType) {
        String collectionName = collectionType.getCollectionName();

        MongoCollection<Document> collection = getCollection(collectionName);

        collection.deleteOne(query);
    }

    public void deleteDocuments(List<ObjectId> documentsIds, CollectionType collectionType) {
        String collectionName = collectionType.getCollectionName();

        MongoCollection<Document> collection = getCollection(collectionName);

        collection.deleteMany(Filters.in("_id", documentsIds));
    }

    public void updateOne(Bson update, ObjectId objectId, CollectionType collectionType) {
        Bson filter = eq("_id", objectId);

        updateOne(update, filter, collectionType);
    }

    public void updateOne(Bson update, Bson filter, CollectionType collectionType) {
        String collectionName = collectionType.getCollectionName();

        MongoCollection<Document> collection = getCollection(collectionName);

        collection.updateOne(filter, update);
    }

    public void clearCollection(CollectionType collectionType) {
        String collectionName = collectionType.getCollectionName();

        MongoCollection<Document> collection = getCollection(collectionName);

        collection.deleteMany(new Document());
    }

    private MongoCollection<Document> getCollection(String collectionName) {
        return database.getCollection(collectionName);
    }

    public void close() {
        if (mongoClient != null) mongoClient.close();
    }
}


