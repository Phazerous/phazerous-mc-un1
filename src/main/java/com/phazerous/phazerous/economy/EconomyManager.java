package com.phazerous.phazerous.economy;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.phazerous.phazerous.db.enums.CollectionType;
import com.phazerous.phazerous.db.utils.DocumentParser;
import com.phazerous.phazerous.economy.interfaces.IEconomySubscriber;
import com.phazerous.phazerous.economy.models.PlayerBalance;
import com.phazerous.phazerous.db.DBManager;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EconomyManager {
    private final DBManager dbManager;
    private final List<IEconomySubscriber> subscribers = new ArrayList<>();

    public EconomyManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }


    public boolean withdraw(UUID playerUUID, double amount) {
        double balance = getPlayerBalanceByUUID(playerUUID);

        if (balance < amount) {
            return false;
        }

        double newBalance = balance - amount;

        setPlayerBalance(playerUUID, newBalance);
        handleBalanceChange(playerUUID, newBalance);

        return true;
    }

    public boolean setPlayerBalance(UUID playerUUID, double balance) {
        Bson update = Updates.set("balance", balance);
        Bson filter = Filters.eq("playerUUID", playerUUID);

        dbManager.updateOne(update, filter, CollectionType.PLAYERS_BALANCES);

        handleBalanceChange(playerUUID, balance);

        return true;
    }

    private void handleBalanceChange(UUID playerUUID, double balance) {
        for (IEconomySubscriber subscriber : subscribers) {
            subscriber.update(playerUUID, balance);
        }
    }

    public double getPlayerBalanceByUUID(UUID playerUUID) {
        Bson filter = Filters.eq("playerUUID", playerUUID);

        Document playerBalanceDoc = dbManager.getDocument(filter, CollectionType.PLAYERS_BALANCES);
        PlayerBalance playerBalance = DocumentParser.parseDocument(playerBalanceDoc, PlayerBalance.class);

        return playerBalance.getBalance();
    }

    public void validatePlayerBalance(UUID playerUUID) {
        if (!hasVault(playerUUID)) {
            createVault(playerUUID);
        }
    }

    private boolean hasVault(UUID playerUUId) {
        Bson filter = Filters.eq("playerUUID", playerUUId);
        Document playerBalanceDoc = dbManager.getDocument(filter, CollectionType.PLAYERS_BALANCES);

        return playerBalanceDoc != null;
    }

    private void createVault(UUID playerUUID) {
        Document document = new Document().append("playerUUID", playerUUID).append("balance", 0.0d);

        dbManager.insertDocument(document, CollectionType.PLAYERS_BALANCES);
    }

    public void subscribe(IEconomySubscriber subscriber) {
        subscribers.add(subscriber);
    }
}
