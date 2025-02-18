package pl.ocode.anarchiavalentines.menu;

import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import pl.ocode.anarchiavalentines.config.MessageConfig;
import pl.ocode.anarchiavalentines.config.PluginConfig;
import pl.ocode.anarchiavalentines.config.menu.MenuBuilder;
import pl.ocode.anarchiavalentines.menu.builder.GuiBuilder;
import pl.ocode.anarchiavalentines.persistence.PlayerCache;
import pl.ocode.anarchiavalentines.persistence.PlayerProperties;
import pl.ocode.anarchiavalentines.config.upgrade.UpgradeType;
import pl.ocode.anarchiavalentines.util.ItemBuilder;
import pl.ocode.anarchiavalentines.util.ItemUtil;
import pl.ocode.anarchiavalentines.util.builder.MapBuilder;

import java.util.Optional;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class UpgradesMenu extends GuiBuilder {
    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;
    private final PlayerCache playerCache;

    @Override
    public GuiBuilder build(@NonNull Player p) {
        final MenuBuilder menuBuilder = this.pluginConfig.upgradesMenu;

        this.createInventory(menuBuilder);

        this.getMenuBuilder().getItems().forEach(menuItem -> {
            if (this.pluginConfig.upgradesCloseSlot == menuItem.getSlot()) {
                this.setItem(menuItem, event -> {
                    if (event.getWhoClicked() instanceof Player player) {
                        player.closeInventory();
                    }
                });
            } else {
                this.setItem(menuItem);
            }
        });

        this.pluginConfig.upgrades.forEach(upgrade ->
                this.setItem(upgrade.getPresenter().getSlot(), ItemBuilder.of(upgrade.getPresenter().getItemStack())
                        .setCmd(upgrade.getPresenter().getCustomModelData())
                        .appendLore(this.pluginConfig.upgradeProductLore)
                        .fixColors(new MapBuilder<String, Object>()
                                .put("price", upgrade.getPrice())
                                .build()
                        ).toItemStack(), event -> {
                    if (event.getWhoClicked() instanceof Player player) {
                        Optional<PlayerProperties> playerProperties = this.playerCache.getProperties(player.getUniqueId());
                        if (playerProperties.isPresent()) {
                            PlayerProperties properties = playerProperties.get();

                            player.closeInventory();

                            if (properties.getUpgrades().containsKey(upgrade.getType()) && properties.getUpgrades().get(upgrade.getType())) {
                                this.messageConfig.upgradeOwned.send(player);

                                return;
                            }

                            if (ItemUtil.countItems(player, this.pluginConfig.valentineHearth.toItemStack()) < upgrade.getPrice()) {
                                this.messageConfig.itemsMissing.send(player);

                                return;
                            }

                            ItemUtil.takeItem(player, this.pluginConfig.valentineHearth.toItemStack(), upgrade.getPrice());

                            properties.getUpgrades().put(upgrade.getType(), true);
                            this.playerCache.markPropertiesToSave(properties);

                            if (upgrade.getType() == UpgradeType.DOUBLE_JUMP) {
                                player.setAllowFlight(true);
                            }

                            this.messageConfig.upgradeBuy.send(player);
                        }
                    }
                })
        );

        return this.guiBuilder();
    }
}
