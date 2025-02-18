package pl.ocode.anarchiavalentines.menu;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.tasker.core.Tasker;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import pl.ocode.anarchiavalentines.AnarchiaValentinesPlugin;
import pl.ocode.anarchiavalentines.AnarchiaValentinesService;
import pl.ocode.anarchiavalentines.config.MessageConfig;
import pl.ocode.anarchiavalentines.config.PluginConfig;
import pl.ocode.anarchiavalentines.config.menu.MenuBuilder;
import pl.ocode.anarchiavalentines.menu.builder.GuiBuilder;
import pl.ocode.anarchiavalentines.persistence.PlayerCache;
import pl.ocode.anarchiavalentines.persistence.PlayerProperties;
import pl.ocode.anarchiavalentines.util.ItemBuilder;
import pl.ocode.anarchiavalentines.util.ItemUtil;
import pl.ocode.anarchiavalentines.util.TimeUtil;
import pl.ocode.anarchiavalentines.util.builder.MapBuilder;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class CostumeMenu extends GuiBuilder {
    private final AnarchiaValentinesService anarchiaValentinesService;
    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;
    private final PlayerCache playerCache;
    private final Tasker tasker;

    @Override
    public GuiBuilder build(@NonNull Player p) {
        final Optional<PlayerProperties> playerProperties = this.playerCache.getProperties(p.getUniqueId());
        if (playerProperties.isPresent()) {
            final PlayerProperties properties = playerProperties.get();

            if (properties.getCostume().isEmpty()) {
                final MenuBuilder menuBuilder = this.pluginConfig.costumeNoActiveMenu;

                this.createInventoryWithItems(menuBuilder);

                this.getMenuBuilder().getItems().forEach(menuItem -> {
                    if (this.pluginConfig.costumeCloseSlot == menuItem.getSlot()) {
                        this.setItem(menuItem, event -> {
                            if (event.getWhoClicked() instanceof Player player) {
                                player.closeInventory();
                            }
                        });

                        return;
                    }

                    if (this.pluginConfig.costumeListSlot == menuItem.getSlot()) {
                        this.setItem(menuItem, event -> {
                            if (event.getWhoClicked() instanceof Player player) {
                                this.tasker.newChain()
                                        .supplyAsync(() -> {
                                            final CostumeListMenu costumeListMenu = AnarchiaValentinesPlugin.getAnarchiaValentinesPlugin().createInstance(CostumeListMenu.class);

                                            return costumeListMenu.build(player);
                                        })
                                        .acceptSync(menu -> menu.show(player))
                                        .execute();
                            }
                        });
                    }
                });
            } else {
                final MenuBuilder menuBuilder = this.pluginConfig.costumeActiveMenu;

                String date = Instant.ofEpochMilli(properties.getCostumeExpiration())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm:ss"));

                this.createInventoryWithItems(menuBuilder, new MapBuilder<String, Object>()
                        .put("costume", "&d" + properties.getCostume())
                        .put("date", date)
                        .build());

                this.getMenuBuilder().getItems().forEach(menuItem -> {
                    if (this.pluginConfig.costumeCloseSlot == menuItem.getSlot()) {
                        this.setItem(menuItem, event -> {
                            if (event.getWhoClicked() instanceof Player player) {
                                player.closeInventory();
                            }
                        });

                        return;
                    }

                    if (this.pluginConfig.costumeListSlot == menuItem.getSlot()) {
                        this.setItem(menuItem, event -> {
                            if (event.getWhoClicked() instanceof Player player) {
                                this.tasker.newChain()
                                        .supplyAsync(() -> {
                                            final CostumeListMenu costumeListMenu = AnarchiaValentinesPlugin.getAnarchiaValentinesPlugin().createInstance(CostumeListMenu.class);

                                            return costumeListMenu.build(player);
                                        })
                                        .acceptSync(menu -> menu.show(player))
                                        .execute();
                            }
                        });

                        return;
                    }

                    if (this.pluginConfig.costumeExpandSlot == menuItem.getSlot()) {
                        this.setItem(menuItem, event -> {
                            if (event.getWhoClicked() instanceof Player player) {
                                player.closeInventory();

                                if (properties.getCostumeExpiration() < System.currentTimeMillis()) {
                                    this.messageConfig.costumeExpired.send(player);

                                    properties.setCostume("");
                                    properties.setCostumeExpiration(0L);

                                    this.playerCache.markPropertiesToSave(properties);

                                    return;
                                }

                                final NamespacedKey rollValue = this.anarchiaValentinesService.getRollValue();
                                final ItemStack rollItem = ItemUtil.getItem(player, rollValue, PersistentDataType.LONG);

                                if (rollItem == null) {
                                    this.messageConfig.rollMissing.send(player);

                                    return;
                                }

                                final Long value = rollItem.getItemMeta().getPersistentDataContainer().get(rollValue, PersistentDataType.LONG);

                                ItemUtil.takeItem(player, rollItem, 1);

                                properties.setCostumeExpiration(properties.getCostumeExpiration() + value);

                                this.playerCache.markPropertiesToSave(properties);

                                this.messageConfig.costumeExpanded.send(player, new MapBuilder<String, Object>()
                                        .put("time", TimeUtil.convertTime(value))
                                        .build());
                            }
                        });

                        return;
                    }

                    if (this.pluginConfig.costumeUnequipSlot == menuItem.getSlot()) {
                        this.setItem(menuItem, event -> {
                            if (event.getWhoClicked() instanceof Player player) {
                                player.closeInventory();

                                if (properties.getCostumeExpiration() < System.currentTimeMillis()) {
                                    this.messageConfig.costumeExpired.send(player);

                                    properties.setCostume("");
                                    properties.setCostumeExpiration(0L);

                                    this.playerCache.markPropertiesToSave(properties);

                                    return;
                                }

                                final NamespacedKey costumeName = this.anarchiaValentinesService.getCostumeName();
                                final NamespacedKey costumeExpiration = this.anarchiaValentinesService.getCostumeExpiration();

                                ItemStack costumeItem = ItemBuilder.of(this.pluginConfig.valentineCostume.getPresenter().toItemStack())
                                        .appendLore(this.pluginConfig.costumeAdditionalLore)
                                        .addFlags(ItemFlag.HIDE_ATTRIBUTES)
                                        .fixColors(new MapBuilder<String, Object>()
                                                .put("date", date)
                                                .build())
                                        .setColor(Color.fromBGR(this.pluginConfig.valentineCostume.getColor()))
                                        .withKey(costumeName,
                                                PersistentDataType.STRING,
                                                properties.getCostume())
                                        .withKey(costumeExpiration,
                                                PersistentDataType.LONG,
                                                properties.getCostumeExpiration())
                                        .toItemStack();

                                ItemUtil.giveItem(player, costumeItem);

                                this.anarchiaValentinesService.remove(Attribute.GENERIC_MAX_HEALTH, player.getAttribute(Attribute.GENERIC_MAX_HEALTH));

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

                                properties.setCostume("");
                                properties.setCostumeExpiration(0L);

                                this.playerCache.markPropertiesToSave(properties);
                            }
                        });
                    }
                });

                this.setItem(this.pluginConfig.costumePresenterSlot, ItemBuilder.of(this.pluginConfig.valentineCostume.getPresenter().toItemStack()).setColor(Color.fromBGR(this.pluginConfig.valentineCostume.getColor())).toItemStack());
            }
        }

        return this.guiBuilder();
    }
}
