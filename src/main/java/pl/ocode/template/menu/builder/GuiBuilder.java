package pl.ocode.template.menu.builder;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pl.ocode.template.config.menu.MenuBuilder;
import pl.ocode.template.util.string.ChatUtil;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class GuiBuilder implements InventoryHolder {
    protected Inventory inv;
    private String title;
    private int size;
    private MenuBuilder menuBuilder;

    public GuiBuilder(@NonNull String title, @NonNull int rows) {
        this.title = title;
        this.size = rows * 9;
        this.menuBuilder = new MenuBuilder(title, rows, new ArrayList<>());
        this.inv = Bukkit.createInventory(this, this.size, ChatUtil.fixColor(title));
    }
    public GuiBuilder(@NonNull MenuBuilder menuBuilder) {
        this.title = menuBuilder.getTitle();
        this.size = menuBuilder.getRows() * 9;
        this.menuBuilder = menuBuilder;
        this.inv = Bukkit.createInventory(this, this.size, ChatUtil.fixColor(title));
    }

    public void setItem(@NonNull int slot, @NonNull ItemStack itemStack) {
        this.inv.setItem(slot, itemStack);
    }
    public void setItem(@NonNull List<Integer> slots, @NonNull ItemStack itemStack) {
        slots.forEach(slot -> this.inv.setItem(slot, itemStack));
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
        this.inv = Bukkit.createInventory(this, this.size, ChatUtil.fixColor(title));

        p.openInventory(this.inv);
    }

    public abstract void build(Player p);
    public abstract void handleClickAction(InventoryClickEvent e);
    public abstract void handleCloseAction(InventoryCloseEvent e);
}