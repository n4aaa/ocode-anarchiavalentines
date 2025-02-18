package pl.ocode.anarchiavalentines.scheduler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.bukkit.annotation.Scheduled;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.ocode.anarchiavalentines.AnarchiaValentinesService;
import pl.ocode.anarchiavalentines.config.MessageConfig;
import pl.ocode.anarchiavalentines.persistence.PlayerCache;
import pl.ocode.anarchiavalentines.persistence.PlayerProperties;
import pl.ocode.anarchiavalentines.util.ProtocolUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Scheduled(delay = 1, rate = 1)
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class CostumeScheduler implements Runnable {

    private final AnarchiaValentinesService anarchiaValentinesService;
    private final MessageConfig messageConfig;
    private final PlayerCache playerCache;

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Optional<PlayerProperties> playerProperties = this.playerCache.getProperties(player.getUniqueId());
            if (playerProperties.isPresent()) {
                PlayerProperties properties = playerProperties.get();

                if (!properties.getCostume().isEmpty()) {
                    if (properties.getCostumeExpiration() < System.currentTimeMillis()) {
                        this.anarchiaValentinesService.remove(Attribute.GENERIC_MAX_HEALTH, player.getAttribute(Attribute.GENERIC_MAX_HEALTH));

                        this.messageConfig.costumeExpired.send(player);

                        properties.setCostume("");
                        properties.setCostumeExpiration(0L);

                        this.playerCache.markPropertiesToSave(properties);

                        List<Pair<EnumWrappers.ItemSlot, ItemStack>> equipment = new ArrayList<>();
                        equipment.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, player.getInventory().getHelmet()));
                        equipment.add(new Pair<>(EnumWrappers.ItemSlot.CHEST, player.getInventory().getChestplate()));
                        equipment.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, player.getInventory().getLeggings()));
                        equipment.add(new Pair<>(EnumWrappers.ItemSlot.FEET, player.getInventory().getBoots()));

                        try {
                            PacketContainer equipmentPacket = this.anarchiaValentinesService.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
                            equipmentPacket.getIntegers().write(0, player.getEntityId());
                            equipmentPacket.getSlotStackPairLists().write(0, equipment);

                            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                this.anarchiaValentinesService.getProtocolManager().sendServerPacket(onlinePlayer, equipmentPacket);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        continue;
                    }

                    List<Pair<EnumWrappers.ItemSlot, ItemStack>> equipment = new ArrayList<>();
                    equipment.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, this.anarchiaValentinesService.getHelmet()));
                    equipment.add(new Pair<>(EnumWrappers.ItemSlot.CHEST, this.anarchiaValentinesService.getChestplate()));
                    equipment.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, this.anarchiaValentinesService.getLeggings()));
                    equipment.add(new Pair<>(EnumWrappers.ItemSlot.FEET, this.anarchiaValentinesService.getBoots()));

                    try {
                        PacketContainer equipmentPacket = this.anarchiaValentinesService.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
                        equipmentPacket.getIntegers().write(0, player.getEntityId());
                        equipmentPacket.getSlotStackPairLists().write(0, equipment);

                        ProtocolUtil.broadcastPlayerPacket(this.anarchiaValentinesService.getProtocolManager(), equipmentPacket, player);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    this.anarchiaValentinesService.remove(Attribute.GENERIC_MAX_HEALTH, player.getAttribute(Attribute.GENERIC_MAX_HEALTH));
                    this.anarchiaValentinesService.apply(Attribute.GENERIC_MAX_HEALTH, player.getAttribute(Attribute.GENERIC_MAX_HEALTH), 10);
                }
            }
        }
    }
}