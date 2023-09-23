package com.phazerous.phazerous.vein_gathering.manager;

import com.phazerous.phazerous.items.utils.ItemBuilder;
import com.phazerous.phazerous.models.PlayerModel;
import com.phazerous.phazerous.models.PlayerVeinToolMeta;
import com.phazerous.phazerous.player.PlayerRepository;
import com.phazerous.phazerous.vein_gathering.enums.VeinToolType;
import com.phazerous.phazerous.vein_gathering.models.Vein;
import com.phazerous.phazerous.vein_gathering.models.VeinTool;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class VeinToolsManager {
    private final PlayerRepository playerRepository;
    private final com.phazerous.phazerous.vein_gathering.repository.VeinToolsRepository veinToolsRepository;

    public VeinToolsManager(PlayerRepository playerRepository, com.phazerous.phazerous.vein_gathering.repository.VeinToolsRepository veinToolsRepository) {
        this.playerRepository = playerRepository;
        this.veinToolsRepository = veinToolsRepository;
    }

    public List<VeinTool> getPlayerVeinTools(Player player, Vein vein) {
        List<Integer> veinToolsTypesIds = vein
                .getVeinType()
                .getTools()
                .stream()
                .map(VeinToolType::getTypeId)
                .collect(Collectors.toList());

        PlayerModel playerModel = playerRepository.getPlayer(player);

        List<PlayerVeinToolMeta> playerVeinToolMetas = playerModel
                .getTools()
                .stream()
                .filter(playerVeinToolMeta -> veinToolsTypesIds.contains(playerVeinToolMeta.getToolType()))
                .collect(Collectors.toList());

        return veinToolsRepository.getVeinTools(playerVeinToolMetas);
    }

    public ItemStack buildTool(VeinTool veinTool) {
        return new ItemBuilder(veinTool.getMaterial())
                .setDisplayName(veinTool.getTitle())
                .build();
    }

}
