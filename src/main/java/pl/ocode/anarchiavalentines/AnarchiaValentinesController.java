package pl.ocode.anarchiavalentines;

import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.PlayerInventory;
import pl.ocode.anarchiavalentines.menu.builder.GuiBuilder;

import java.util.function.Consumer;

@Component
public class AnarchiaValentinesController implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();

        if (inventory == null) {
            return;
        }

        InventoryHolder inventoryHolder = inventory.getHolder();

        if (inventoryHolder instanceof GuiBuilder) {
            GuiBuilder menu = (GuiBuilder) inventoryHolder;
            Consumer<InventoryClickEvent> consumer;

            menu.handleClickAction(event);
            event.setCancelled(true);

            if ((consumer = menu.getActions().get(event.getSlot())) != null) {
                consumer.accept(event);
            }

            return;
        }

        Inventory openInventory = event.getWhoClicked().getOpenInventory().getTopInventory();

        if (openInventory.getHolder() instanceof GuiBuilder) {
            if (inventory instanceof PlayerInventory) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();

        if (inventory == null) {
            return;
        }

        InventoryHolder inventoryHolder = inventory.getHolder();

        if (inventoryHolder instanceof GuiBuilder) {
            GuiBuilder menu = (GuiBuilder) inventoryHolder;

            menu.handleCloseAction(event);

            return;
        }
    }
}