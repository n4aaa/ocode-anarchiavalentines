package pl.ocode.anarchiavalentines.menu;

import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.ocode.anarchiavalentines.config.MessageConfig;
import pl.ocode.anarchiavalentines.config.PluginConfig;
import pl.ocode.anarchiavalentines.config.menu.MenuBuilder;
import pl.ocode.anarchiavalentines.menu.builder.GuiBuilder;
import pl.ocode.anarchiavalentines.util.ItemUtil;
import pl.ocode.anarchiavalentines.util.builder.MapBuilder;
import pl.ocode.anarchiavalentines.util.string.StringUtil;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ShopMenu extends GuiBuilder {
    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;

    @Override
    public GuiBuilder build(@NonNull Player p) {
        final MenuBuilder menuBuilder = this.pluginConfig.shopMenu;

        this.createInventory(menuBuilder);

        this.getMenuBuilder().getItems().forEach(menuItem -> {
            if (this.pluginConfig.shopMenuCloseSlot == menuItem.getSlot()) {
                this.setItem(menuItem, event -> {
                    if (event.getWhoClicked() instanceof Player player) {
                        player.closeInventory();
                    }
                });
            } else {
                this.setItem(menuItem);
            }
        });

        int hearthCount = ItemUtil.countItems(p, this.pluginConfig.valentineHearth.toItemStack());

        this.pluginConfig.products.forEach(product -> {
            ItemStack presenter = (hearthCount >= product.getPrice()) ? product.getPresenterAvaiable().toItemStack() : product.getPresenterUnavaiable().toItemStack();

            this.setItem(product.getSlot(), presenter, event -> {
                if (event.getWhoClicked() instanceof Player player) {
                    player.closeInventory();

                    if (ItemUtil.countItems(player, this.pluginConfig.valentineHearth.toItemStack()) < product.getPrice()) {
                        this.messageConfig.itemsMissing.send(player);

                        return;
                    }

                    product.getCommands().forEach(cmd -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtil.replace(cmd, new MapBuilder<String, Object>()
                            .put("player", player.getName())
                            .build())));

                    ItemUtil.takeItem(player, this.pluginConfig.valentineHearth.toItemStack(), product.getPrice());

                    this.messageConfig.productBuy.send(player);
                }
            });
        });

        return this.guiBuilder();
    }
}
