package pl.ocode.template.menu;

import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import pl.ocode.template.config.PluginConfig;
import pl.ocode.template.config.menu.MenuBuilder;
import pl.ocode.template.menu.builder.GuiBuilder;
import pl.ocode.template.util.ItemBuilder;

public class ExampleMenu extends GuiBuilder {
    public ExampleMenu(@NonNull MenuBuilder menuBuilder) {
        super(menuBuilder);
    }

    private @Inject PluginConfig pluginConfig;

    @Override
    public void build(Player p) {
        this.getMenuBuilder().getItems().forEach(menuItem -> setItem(menuItem.getSlots(), ItemBuilder.of(menuItem.getItemStack().clone()).setCmd(menuItem.getCustomModelData()).fixColors().toItemStack()));
    }

    @Override
    public void handleClickAction(InventoryClickEvent e) {
    }

    @Override
    public void handleCloseAction(InventoryCloseEvent e) {
    }
}
