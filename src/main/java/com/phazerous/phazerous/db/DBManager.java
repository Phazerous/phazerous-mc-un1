package com.phazerous.phazerous.db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.phazerous.phazerous.dtos.*;
import com.phazerous.phazerous.entities.models.LocationedEntity;
import com.phazerous.phazerous.gui.actions.dtos.CustomInventoryActionDto;
import com.phazerous.phazerous.gui.dtos.CustomInventoryDto;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DBManager {
    private final MongoDatabase database;
    private final MongoClient mongoClient;
    private final HashMap<Class<?>, MongoCollection<?>> collections = new HashMap<>();

    public DBManager() {
        String MONGO_DB_CONNECTION_STRING = "mongodb://localhost:27017";
        String MONGO_DB_NAME = "mc-db";

        ConnectionString connectionString = new ConnectionString(MONGO_DB_CONNECTION_STRING);
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider
                .builder()
                .automatic(true)
                .build());
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

        MongoClientSettings settings = MongoClientSettings
                .builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build();

        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase(MONGO_DB_NAME);
    }

    // REFACOTREDDD

    public List<Document> getDocuments(CollectionType collectionType) {
        String collectionName = collectionType.getCollectionName();

        FindIterable<Document> documentsIterator = getCollection(collectionName).find();

        return documentsIterator.into(new ArrayList<>());
    }

    public Document getDocumentById(ObjectId id, CollectionType collectionType) {
        String collectionName = collectionType.getCollectionName();
        Document query = new Document("_id", id);

        return getCollection(collectionName)
                .find(query)
                .first();
    }

    public Document getDocument(Bson query, CollectionType collectionType) {
        String collectionName = collectionType.getCollectionName();

        return getCollection(collectionName)
                .find(query)
                .first();
    }

    public void insertDocuments(List<Document> documents, CollectionType collectionType) {
        String collectionName = collectionType.getCollectionName();

        documents.forEach(it -> it.remove("_id"));

        MongoCollection<Document> collection = getCollection(collectionName);

        collection.insertMany(documents);
    }

    public void insertDocument(Document document, CollectionType collectionType) {
        String collectionName = collectionType.getCollectionName();

        MongoCollection<Document> collection = getCollection(collectionName);

        collection.insertOne(document);
    }

    public void deleteDocument(ObjectId documentId, CollectionType collectionType) {
        String collectionName = collectionType.getCollectionName();

        MongoCollection<Document> collection = getCollection(collectionName);

        collection.deleteOne(Filters.eq("_id", documentId));
    }

    public void deleteDocuments(List<ObjectId> documentsIds, CollectionType collectionType) {
        String collectionName = collectionType.getCollectionName();

        MongoCollection<Document> collection = getCollection(collectionName);

        collection.deleteMany(Filters.in("_id", documentsIds));
    }

    public void updateOne(Bson update, ObjectId objectId, CollectionType collectionType) {
        String collectionName = collectionType.getCollectionName();

        MongoCollection<Document> collection = getCollection(collectionName);

        Bson filter = Filters.eq("_id", objectId);

        collection.updateOne(filter, update);
    }

    // REFACOTREDDD


    private <T> MongoCollection<T> getCollection(Class<T> collectionClass) {
        if (!collections.containsKey(collectionClass)) {
            String collectionName = CollectionType
                    .getCollectionTypeByClass(collectionClass)
                    .getCollectionName();
            MongoCollection<T> collection = database.getCollection(collectionName, collectionClass);

            collections.put(collectionClass, collection);
        }

        return (MongoCollection<T>) collections.get(collectionClass);
    }

    //REFACTOR
    private MongoCollection<Document> getCollection(String collectionName) {
        return database.getCollection(collectionName);
    }

    public ItemDto getItemDtoById(ObjectId id) {
        Document query = new Document("_id", id);

        return getCollection(ItemDto.class)
                .find(query)
                .first();
    }

    public PlayerBalanceDto getPlayerBalanceDtoByUUID(UUID playerUUID) {
        Document query = new Document("playerUUID", playerUUID);

        return getCollection(PlayerBalanceDto.class)
                .find(query)
                .first();
    }

    public CustomInventoryActionDto getCustomInventoryActionByID(ObjectId id) {
        Document query = new Document("_id", id);

        return getCollection(CustomInventoryActionDto.class)
                .find(query)
                .first();
    }

    public void setPlayerBalance(UUID playerUUID, double balance) {
        Document query = new Document("playerUUID", playerUUID);
        Document update = new Document("$set", new Document("balance", balance));

        getCollection(PlayerBalanceDto.class).updateOne(query, update);
    }

    public void createPlayerBalance(UUID playerUUID) {
        PlayerBalanceDto playerBalanceDto = new PlayerBalanceDto();
        playerBalanceDto.setPlayerUUID(playerUUID);
        playerBalanceDto.setBalance(0);

        getCollection(PlayerBalanceDto.class).insertOne(playerBalanceDto);
    }

    public CustomInventoryDto getCustomInventoryDtoById(ObjectId id) {
        Document query = new Document("_id", id);

        return getCollection(CustomInventoryDto.class)
                .find(query)
                .first();
    }

    public void close() {
        if (mongoClient != null) mongoClient.close();
    }
}


