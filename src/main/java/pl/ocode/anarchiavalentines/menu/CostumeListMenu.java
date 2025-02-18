package pl.ocode.anarchiavalentines.menu;

import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.tasker.core.Tasker;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import pl.ocode.anarchiavalentines.AnarchiaValentinesPlugin;
import pl.ocode.anarchiavalentines.config.PluginConfig;
import pl.ocode.anarchiavalentines.config.menu.MenuBuilder;
import pl.ocode.anarchiavalentines.menu.builder.GuiBuilder;
import pl.ocode.anarchiavalentines.util.ItemBuilder;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class CostumeListMenu extends GuiBuilder {
    private final PluginConfig pluginConfig;
    private final Tasker tasker;

    @Override
    public GuiBuilder build(@NonNull Player p) {
        final MenuBuilder menuBuilder = this.pluginConfig.costumeListMenu;

        this.createInventory(menuBuilder);

        this.getMenuBuilder().getItems().forEach(menuItem -> {
            if (this.pluginConfig.costumeListCloseSlot == menuItem.getSlot()) {
                this.setItem(menuItem, event -> {
                    if (event.getWhoClicked() instanceof Player player) {
                        this.tasker.newChain()
                                .supplyAsync(() -> {
                                    final CostumeMenu costumeMenu = AnarchiaValentinesPlugin.getAnarchiaValentinesPlugin().createInstance(CostumeMenu.class);

                                    return costumeMenu.build(player);
                                })
                                .acceptSync(menu -> menu.show(player))
                                .execute();
                    }
                });
            } else {
                this.setItem(menuItem);
            }
        });

        this.setItem(this.pluginConfig.valentineCostume.getSlot(), ItemBuilder.of(this.pluginConfig.valentineCostume.getPresenter().toItemStack()).addFlags(ItemFlag.HIDE_ATTRIBUTES).setColor(Color.fromRGB(this.pluginConfig.valentineCostume.getColor())).toItemStack());

        return this.guiBuilder();
    }
}
