package pl.ocode.anarchiavalentines.config.costume;

import eu.okaeri.configs.OkaeriConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import pl.ocode.anarchiavalentines.config.customitem.CustomItem;

@Getter
@Setter
@AllArgsConstructor
public class Costume extends OkaeriConfig {
    private String name;
    private int slot;
    private CustomItem presenter;

    private int color;
    private String value;

    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
}
