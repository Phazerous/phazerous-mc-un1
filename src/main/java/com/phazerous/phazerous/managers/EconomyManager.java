package com.phazerous.phazerous.managers;

import com.phazerous.phazerous.dtos.PlayerBalanceDto;
import com.phazerous.phazerous.managers.DBManager;

import java.util.UUID;

public class EconomyManager {
    private DBManager dbManager;

    public EconomyManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public float getPlayerBalanceByUUID(UUID uuid) {
        PlayerBalanceDto playerBalanceDto = dbManager.getPlayerBalanceDtoByUUID(uuid);

        return playerBalanceDto.getBalance();
    }

    public boolean setPlayerBalance(UUID playerUUID, double balance) {
        dbManager.setPlayerBalance(playerUUID, balance);
        return true;
    }

    public void validatePlayerBalance(UUID playerUUID) {
        if (!hasVault(playerUUID)) {
            createVault(playerUUID);
        }
    }

    private boolean hasVault(UUID playerUUId) {
        return dbManager.getPlayerBalanceDtoByUUID(playerUUId) != null;
    }

    private void createVault(UUID playerUUID) {
        dbManager.createPlayerBalance(playerUUID);
    }
}
