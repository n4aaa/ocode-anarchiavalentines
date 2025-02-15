package pl.ocode.template.config.menu;

import eu.okaeri.configs.OkaeriConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MenuItem extends OkaeriConfig {
    private List<Integer> slots;
    private ItemStack itemStack;
    private int customModelData;
}