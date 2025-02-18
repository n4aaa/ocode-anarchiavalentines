package pl.ocode.anarchiavalentines.controller;

import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import eu.okaeri.tasker.core.Tasker;
import lombok.RequiredArgsConstructor;
import org.bukkit.*;
import org.bukkit.block.data.type.PointedDripstone;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import pl.ocode.anarchiavalentines.AnarchiaValentinesService;
import pl.ocode.anarchiavalentines.config.MessageConfig;
import pl.ocode.anarchiavalentines.config.PluginConfig;
import pl.ocode.anarchiavalentines.persistence.PlayerCache;
import pl.ocode.anarchiavalentines.persistence.PlayerProperties;
import pl.ocode.anarchiavalentines.config.upgrade.UpgradeType;
import pl.ocode.anarchiavalentines.util.ItemUtil;
import pl.ocode.anarchiavalentines.util.NumberUtil;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class UpgradeController implements Listener {
    private final AnarchiaValentinesService anarchiaValentinesService;
    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;
    private final PlayerCache playerCache;
    private final Tasker tasker;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (NumberUtil.reachChance(this.pluginConfig.hearthStoneChance)) {
            ItemStack item = this.pluginConfig.valentineHearth.toItemStack();
            item.setAmount(1);

            ItemUtil.giveItem(player, item);

            this.messageConfig.hearthFromStone.send(player);
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void entityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof final Player player) {
            final Optional<PlayerProperties> playerProperties = this.playerCache.getProperties(player.getUniqueId());
            if (playerProperties.isPresent()) {
                final PlayerProperties properties = playerProperties.get();

                if (properties.getUpgrades().containsKey(UpgradeType.PROTECTION) && properties.getUpgrades().get(UpgradeType.PROTECTION)) {
                    event.setDamage(event.getDamage() * 0.85);
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void entityDamageByPlayer(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof final Player player) {
            final Optional<PlayerProperties> playerProperties = this.playerCache.getProperties(player.getUniqueId());
            if (playerProperties.isPresent()) {
                final PlayerProperties properties = playerProperties.get();

                if (properties.getUpgrades().containsKey(UpgradeType.DAMAGE) && properties.getUpgrades().get(UpgradeType.DAMAGE)) {
                    event.setDamage(event.getDamage() * 1.15);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void entityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            final Player player = event.getEntity().getKiller();

            ItemStack item = this.pluginConfig.valentineHearth.toItemStack();
            item.setAmount(1);

            if (player.hasPermission(this.pluginConfig.doubleHearthPermission)) {
                item.setAmount(2);
            }

            ItemUtil.giveItem(player, item);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void entityDamageByDripstone(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof PointedDripstone && event.getEntity() instanceof final Player player) {
            final Optional<PlayerProperties> playerProperties = this.playerCache.getProperties(player.getUniqueId());
            if (playerProperties.isPresent()) {
                final PlayerProperties properties = playerProperties.get();

                if (properties.getUpgrades().containsKey(UpgradeType.DRIPSTONE) && properties.getUpgrades().get(UpgradeType.DRIPSTONE)) {
                    event.setDamage(event.getDamage() * 0.5);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerDeath(PlayerDeathEvent event) {
        final Player player = event.getEntity().getPlayer();

        final Optional<PlayerProperties> playerProperties = this.playerCache.getProperties(player.getUniqueId());
        if (playerProperties.isPresent()) {
            final PlayerProperties properties = playerProperties.get();

            if ((properties.getUpgrades().containsKey(UpgradeType.KEEP_ITEMS) && properties.getUpgrades().get(UpgradeType.KEEP_ITEMS)) && !properties.isKeepItemsUsed()) {
                for (int i = 0; i < event.getDrops().size() / 2; i++) {
                    ItemStack itemStack = event.getDrops().get(i);

                    ItemUtil.giveItem(player, itemStack);
                    event.getDrops().remove(itemStack);
                }

                properties.setKeepItemsUsed(true);
                this.playerCache.markPropertiesToSave(properties);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().setAllowFlight(true);
    }
    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        final Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        event.setCancelled(true);
        player.setAllowFlight(false);
        player.setFlying(false);

        final Optional<PlayerProperties> playerProperties = this.playerCache.getProperties(player.getUniqueId());
        if (playerProperties.isPresent()) {
            final PlayerProperties properties = playerProperties.get();

            if (properties.getUpgrades().containsKey(UpgradeType.DOUBLE_JUMP) && properties.getUpgrades().get(UpgradeType.DOUBLE_JUMP)) {
                player.setVelocity(player.getLocation().getDirection().multiply(this.pluginConfig.doubleJumpMultiply).setY(this.pluginConfig.doubleJumpY));

                this.tasker.newDelayer(Duration.ofSeconds(10))
                        .abortIfNot(player::isOnline)
                        .delayed(() -> this.anarchiaValentinesService.getPlayersToUpdate().add(player.getUniqueId()))
                        .execute(true);
            }
        }
    }
}
