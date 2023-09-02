package com.phazerous.phazerous.items;

import com.phazerous.phazerous.archtecture.AbstractModule;
import com.phazerous.phazerous.db.DBManager;
import lombok.Getter;

@Getter
public class ItemsModule extends AbstractModule {
    private final ItemManager itemManager;

    public ItemsModule(DBManager dbManager) {
        itemManager = new ItemManager(dbManager);
    }
}
