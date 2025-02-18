package pl.ocode.anarchiavalentines.config.menu;

import eu.okaeri.configs.OkaeriConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
@AllArgsConstructor
public class MenuItem extends OkaeriConfig {
    private int slot;
    private ItemStack itemStack;
    private int customModelData;
}