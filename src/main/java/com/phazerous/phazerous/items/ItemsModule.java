package com.phazerous.phazerous.items;

import com.phazerous.phazerous.archtecture.AbstractModule;
import com.phazerous.phazerous.items.repository.ItemRepository;
import com.phazerous.phazerous.items.utils.DropManager;
import com.phazerous.phazerous.shared.SharedModule;
import lombok.Getter;

@Getter
public class ItemsModule extends AbstractModule {
    private final ItemManager itemManager;
    private final DropManager dropManager;

    public ItemsModule(SharedModule sharedModule) {
        ItemRepository itemRepository = new ItemRepository(sharedModule.getDbManager());
        ItemBuilderDirector itemBuilderDirector = new ItemBuilderDirector();

        itemManager = new ItemManager(sharedModule.getDbManager(), itemRepository, itemBuilderDirector); //REFACTOR

        dropManager = new DropManager(itemManager);
    }
}
