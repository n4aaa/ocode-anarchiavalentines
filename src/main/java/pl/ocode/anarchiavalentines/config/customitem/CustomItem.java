package pl.ocode.anarchiavalentines.config.customitem;

import eu.okaeri.configs.OkaeriConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import pl.ocode.anarchiavalentines.util.ItemBuilder;

@Getter
@Setter
@AllArgsConstructor
public class CustomItem extends OkaeriConfig {
    private ItemStack itemStack;
    private int customModelData;

    public ItemStack toItemStack() {
        return ItemBuilder.of(this.itemStack.clone()).setCmd(this.customModelData).toItemStack();
    }
}
