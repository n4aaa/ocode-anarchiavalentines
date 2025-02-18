package pl.ocode.anarchiavalentines.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

@UtilityClass
public final class ItemUtil {

    public static void giveItem(@NonNull Player player, @NonNull ItemStack itemStack) {
        player.getInventory().addItem(itemStack);
    }
    public static void giveItemOrDrop(@NonNull Player player, @NonNull ItemStack itemStack) {
        if (hasSpace(player.getInventory(), itemStack)) {
            player.getInventory().addItem(itemStack);

            return;
        }

        player.getLocation().getWorld().dropItemNaturally(player.getLocation(), itemStack);
    }

    public static int countItems(@NonNull Player player, @NonNull ItemStack item) {
        int itemCount = 0;

        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null && itemStack.isSimilar(item)) {
                itemCount += itemStack.getAmount();
            }
        }

        return itemCount;
    }

    public static boolean haveItem(@NonNull Player player, @NonNull NamespacedKey namespacedKey, @NonNull PersistentDataType dataType) {
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null && itemStack.hasItemMeta()) {
                if (itemStack.getItemMeta().getPersistentDataContainer().has(namespacedKey, dataType)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void takeItem(@NonNull Player player, @NonNull ItemStack itemStack, @NonNull int amount) {
        Inventory inventory = player.getInventory();
        int remaining = amount;

        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.isSimilar(itemStack)) {
                if (item.getAmount() > remaining) {
                    item.setAmount(item.getAmount() - remaining);
                    return;
                } else {
                    remaining -= item.getAmount();
                    inventory.removeItem(item);

                    if (remaining <= 0) {
                        return;
                    }
                }
            }
        }
    }
    public static ItemStack getItem(@NonNull Player player, @NonNull NamespacedKey namespacedKey, @NonNull PersistentDataType dataType) {
        Inventory inventory = player.getInventory();

        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.hasItemMeta()) {
                if (item.getItemMeta().getPersistentDataContainer().has(namespacedKey, dataType)) {
                    return item;
                }
            }
        }

        return null;
    }

    private static boolean hasSpace(@NonNull Inventory inventory, @NonNull ItemStack itemStack) {
        if (inventory.firstEmpty() != -1) {
            return true;
        }

        for (ItemStack itemInv : inventory.getContents()) {
            if (itemInv == null || !itemInv.isSimilar(itemStack) || itemInv.getMaxStackSize() <= itemInv.getAmount()) {
                continue;
            }

            return true;
        }

        return false;
    }
}