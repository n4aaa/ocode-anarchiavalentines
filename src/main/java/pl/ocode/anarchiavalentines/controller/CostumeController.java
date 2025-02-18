package pl.ocode.anarchiavalentines.controller;

import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import eu.okaeri.tasker.core.Tasker;
import lombok.RequiredArgsConstructor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import pl.ocode.anarchiavalentines.AnarchiaValentinesService;
import pl.ocode.anarchiavalentines.config.MessageConfig;
import pl.ocode.anarchiavalentines.config.PluginConfig;
import pl.ocode.anarchiavalentines.persistence.PlayerCache;
import pl.ocode.anarchiavalentines.persistence.PlayerProperties;
import pl.ocode.anarchiavalentines.util.ItemUtil;

import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class CostumeController implements Listener {
    private final AnarchiaValentinesService anarchiaValentinesService;
    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;
    private final PlayerCache playerCache;
    private final Tasker tasker;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void fallDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof final Player player && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            final Optional<PlayerProperties> playerProperties = this.playerCache.getProperties(player.getUniqueId());
            if (playerProperties.isPresent()) {
                final PlayerProperties properties = playerProperties.get();

                if (!properties.getCostume().isEmpty()) {
                    event.setCancelled(true);
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void protection(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof final Player player) {
            final Optional<PlayerProperties> playerProperties = this.playerCache.getProperties(player.getUniqueId());
            if (playerProperties.isPresent()) {
                final PlayerProperties properties = playerProperties.get();

                if (!properties.getCostume().isEmpty()) {
                    event.setDamage(event.getDamage() * 0.95);
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void damage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof final Player player) {
            final Optional<PlayerProperties> playerProperties = this.playerCache.getProperties(player.getUniqueId());
            if (playerProperties.isPresent()) {
                final PlayerProperties properties = playerProperties.get();

                if (!properties.getCostume().isEmpty()) {
                    event.setDamage(event.getDamage() * 1.12);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void activate(PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        final NamespacedKey costumeName = this.anarchiaValentinesService.getCostumeName();
        final NamespacedKey costumeExpiration = this.anarchiaValentinesService.getCostumeExpiration();
        final NamespacedKey costumeDuration = this.anarchiaValentinesService.getCostumeDuration();

        final ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.hasItemMeta()) {
            return;
        }

        final ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;

        final PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        if (!dataContainer.has(costumeName, PersistentDataType.STRING)) {
            return;
        }

        event.setCancelled(true);

        final Optional<PlayerProperties> playerProperties = this.playerCache.getProperties(player.getUniqueId());
        if (playerProperties.isPresent()) {
            final PlayerProperties properties = playerProperties.get();
            if (!properties.getCostume().isEmpty()) {
                this.messageConfig.cantEquipCostume.send(player);

                return;
            }

            ItemUtil.takeItem(player, item, 1);

            final String name = dataContainer.get(costumeName, PersistentDataType.STRING);
            if (dataContainer.has(costumeDuration, PersistentDataType.LONG)) {
                final Long duration = dataContainer.get(costumeDuration, PersistentDataType.LONG);
                assert duration != null;

                properties.setCostume(name);
                properties.setCostumeExpiration(System.currentTimeMillis() + duration);
            } else {
                final Long expiration = dataContainer.get(costumeExpiration, PersistentDataType.LONG);
                assert expiration != null;

                if (expiration < System.currentTimeMillis()) {
                    this.messageConfig.costumeExpired.send(player);

                    return;
                }

                properties.setCostume(name);
                properties.setCostumeExpiration(expiration);
            }

            this.messageConfig.costumeEquipped.send(player);

            this.playerCache.markPropertiesToSave(properties);
        }
    }
}
