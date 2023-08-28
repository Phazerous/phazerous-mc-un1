package com.phazerous.phazerous.db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.phazerous.phazerous.dtos.*;
import com.phazerous.phazerous.gui.actions.dtos.CustomInventoryActionDto;
import com.phazerous.phazerous.gui.dtos.CustomInventoryDto;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
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
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder()
                .automatic(true)
                .build());
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build();

        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase(MONGO_DB_NAME);
    }

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

    public void insertRuntimeEntitiesDtos(List<RuntimeEntityDto> runtimeEntitiesDtos) {
        try {
            MongoCollection<Document> runtimeEntitiesCollection = getCollection("runtime_entities");

            List<Document> runtimeEntitiesDtosDocuments = runtimeEntitiesDtos.stream()
                    .map(RuntimeEntityDto::toInsertDocument)
                    .collect(Collectors.toList());

            runtimeEntitiesCollection.insertMany(runtimeEntitiesDtosDocuments);
        } catch (Exception e) {
            System.out.println("Error inserting runtime entities dtos: " + e.getMessage());
        }
    }

    public void deleteRuntimeEntities(List<RuntimeEntityDto> runtimeEntitiesDtos) {
        try {
            MongoCollection<RuntimeEntityDto> runtimeEntitiesCollection = getCollection(RuntimeEntityDto.class);

            List<ObjectId> runtimeEntitiesDtosIds = runtimeEntitiesDtos.stream()
                    .map(RuntimeEntityDto::getId)
                    .collect(Collectors.toList());

            Document inFilter = new Document("$in", runtimeEntitiesDtosIds);

            runtimeEntitiesCollection.deleteMany(new Document("_id", inFilter));
        } catch (Exception e) {
            System.out.println("Error deleting runtime entities: " + e.getMessage());
        }
    }

    public List<RuntimeEntityDto> getRuntimeEntitiesDtos() {
        FindIterable<RuntimeEntityDto> runtimeEntityDtosIterator = getCollection(RuntimeEntityDto.class).find();

        return runtimeEntityDtosIterator.into(new ArrayList<>());
    }

    public List<LocationedEntityDto> getLocationedEntitiesDtos() {
        FindIterable<LocationedEntityDto> locationedEntityDtosIterator = getCollection(LocationedEntityDto.class).find();

        return locationedEntityDtosIterator.into(new ArrayList<>());
    }

    public LocationedEntityDto getLocationedEntityDtoById(ObjectId id) {
        Document query = new Document("_id", id);

        return getCollection(LocationedEntityDto.class).find(query)
                .first();
    }

    public EntityDto getEntityDtoById(ObjectId id) {
        Document query = new Document("_id", id);

        return getCollection(EntityDto.class).find(query)
                .first();
    }

    public ItemDto getItemDtoById(ObjectId id) {
        Document query = new Document("_id", id);

        return getCollection(ItemDto.class).find(query)
                .first();
    }

    public RuntimeEntityDto getRuntimeEntityDtoByUUID(UUID uuid) {
        Document query = new Document("uuid", uuid);

        return getCollection(RuntimeEntityDto.class).find(query)
                .first();
    }

    public PlayerBalanceDto getPlayerBalanceDtoByUUID(UUID playerUUID) {
        Document query = new Document("playerUUID", playerUUID);

        return getCollection(PlayerBalanceDto.class).find(query)
                .first();
    }

    public CustomInventoryActionDto getCustomInventoryActionByID(ObjectId id) {
        Document query = new Document("_id", id);

        return getCollection(CustomInventoryActionDto.class).find(query)
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

        return getCollection(CustomInventoryDto.class).find(query)
                .first();
    }

    public void close() {
        if (mongoClient != null) mongoClient.close();
    }
}


