package pl.ocode.anarchiavalentines.menu.builder;

import eu.okaeri.commons.cache.CacheMap;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pl.ocode.anarchiavalentines.config.menu.MenuBuilder;
import pl.ocode.anarchiavalentines.config.menu.MenuItem;
import pl.ocode.anarchiavalentines.util.ItemBuilder;
import pl.ocode.anarchiavalentines.util.string.ChatUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Getter
@Setter
public abstract class GuiBuilder implements InventoryHolder {
    protected Inventory inv;
    private String title;
    private int size;
    private MenuBuilder menuBuilder;

    private final CacheMap<Integer, Consumer<InventoryClickEvent>> actions = new CacheMap<>();

    public void createInventory(@NonNull String title, @NonNull int rows) {
        this.title = title;
        this.size = rows * 9;
        this.menuBuilder = new MenuBuilder(title, rows, new ArrayList<>());
        String fixedTitle = ChatUtil.fixColor(title);
        this.inv = Bukkit.createInventory(this, this.size, fixedTitle);
    }

    public void createInventoryWithItems(@NonNull String title, @NonNull int rows, @NonNull List<MenuItem> items) {
        this.title = title;
        this.size = rows * 9;
        this.menuBuilder = new MenuBuilder(title, rows, new ArrayList<>());
        String fixedTitle = ChatUtil.fixColor(title);
        this.inv = Bukkit.createInventory(this, this.size, fixedTitle);

        for (MenuItem menuItem : items) {
            ItemStack baseItem = menuItem.getItemStack();
            ItemStack finalItem = ItemBuilder.of(baseItem)
                    .setCmd(menuItem.getCustomModelData())
                    .fixColors()
                    .toItemStack();
            this.setItem(menuItem.getSlot(), finalItem);
        }
    }

    public void createInventoryWithItems(@NonNull String title, @NonNull int rows, @NonNull List<MenuItem> items, @NonNull Map<String, Object> placeholders) {
        this.title = title;
        this.size = rows * 9;
        this.menuBuilder = new MenuBuilder(title, rows, new ArrayList<>());
        String fixedTitle = ChatUtil.fixColor(title, placeholders);
        this.inv = Bukkit.createInventory(this, this.size, fixedTitle);

        for (MenuItem menuItem : items) {
            ItemStack baseItem = menuItem.getItemStack();
            ItemStack finalItem = ItemBuilder.of(baseItem)
                    .setCmd(menuItem.getCustomModelData())
                    .fixColors(placeholders)
                    .toItemStack();
            this.setItem(menuItem.getSlot(), finalItem);
        }
    }

    public void createInventory(@NonNull MenuBuilder menuBuilder) {
        this.title = menuBuilder.getTitle();
        this.size = menuBuilder.getRows() * 9;
        this.menuBuilder = menuBuilder;
        String fixedTitle = ChatUtil.fixColor(title);
        this.inv = Bukkit.createInventory(this, this.size, fixedTitle);
    }

    public void createInventoryWithItems(@NonNull MenuBuilder menuBuilder) {
        this.title = menuBuilder.getTitle();
        this.size = menuBuilder.getRows() * 9;
        this.menuBuilder = menuBuilder;
        String fixedTitle = ChatUtil.fixColor(title);
        this.inv = Bukkit.createInventory(this, this.size, fixedTitle);

        for (MenuItem menuItem : menuBuilder.getItems()) {
            ItemStack baseItem = menuItem.getItemStack();
            ItemStack finalItem = ItemBuilder.of(baseItem)
                    .setCmd(menuItem.getCustomModelData())
                    .fixColors()
                    .toItemStack();
            this.setItem(menuItem.getSlot(), finalItem);
        }
    }

    public void createInventoryWithItems(@NonNull MenuBuilder menuBuilder, @NonNull Map<String, Object> placeholders) {
        this.title = menuBuilder.getTitle();
        this.size = menuBuilder.getRows() * 9;
        this.menuBuilder = menuBuilder;
        String fixedTitle = ChatUtil.fixColor(title, placeholders);
        this.inv = Bukkit.createInventory(this, this.size, fixedTitle);

        for (MenuItem menuItem : menuBuilder.getItems()) {
            ItemStack baseItem = menuItem.getItemStack();
            ItemStack finalItem = ItemBuilder.of(baseItem)
                    .setCmd(menuItem.getCustomModelData())
                    .fixColors(placeholders)
                    .toItemStack();
            this.setItem(menuItem.getSlot(), finalItem);
        }
    }

    public GuiBuilder guiBuilder() {
        if (inv == null) {
            this.title = "Empty name!";
            this.size = 3 * 9;
            this.menuBuilder = new MenuBuilder(title, 3, new ArrayList<>());
            String fixedTitle = ChatUtil.fixColor(title);
            this.inv = Bukkit.createInventory(this, this.size, fixedTitle);
        }
        return this;
    }

    public void setItem(@NonNull int slot, @NonNull ItemStack itemStack) {
        this.inv.setItem(slot, itemStack);
    }

    public void setItem(@NonNull int slot, @NonNull ItemStack itemStack, @NonNull Consumer<InventoryClickEvent> consumer) {
        this.inv.setItem(slot, itemStack);
        this.actions.put(slot, consumer);
    }

    public void setItem(@NonNull MenuItem menuItem) {
        this.inv.setItem(menuItem.getSlot(),
                ItemBuilder.of(menuItem.getItemStack())
                        .setCmd(menuItem.getCustomModelData())
                        .toItemStack());
    }

    public void setItem(@NonNull MenuItem menuItem, @NonNull Consumer<InventoryClickEvent> consumer) {
        this.inv.setItem(menuItem.getSlot(),
                ItemBuilder.of(menuItem.getItemStack())
                        .setCmd(menuItem.getCustomModelData())
                        .toItemStack());
        this.actions.put(menuItem.getSlot(), consumer);
    }

    public void setItems(@NonNull ItemStack[] itemStacks) {
        this.inv.setContents(itemStacks);
    }

    public ItemStack getItemStack(@NonNull int slot) {
        return this.inv.getItem(slot - 1);
    }

    public @NonNull Inventory getInventory() {
        return this.inv;
    }

    public void show(@NonNull Player p) {
        p.openInventory(this.inv);
    }

    public void show(@NonNull Player p, @NonNull String title) {
        this.title = title;
        String fixedTitle = ChatUtil.fixColor(title);
        this.inv = Bukkit.createInventory(this, this.size, fixedTitle);
        p.openInventory(this.inv);
    }

    public abstract GuiBuilder build(@NonNull Player p);

    public void handleClickAction(InventoryClickEvent e) {}
    public void handleCloseAction(InventoryCloseEvent e) {}
}
