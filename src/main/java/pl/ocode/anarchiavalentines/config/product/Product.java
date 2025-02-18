package pl.ocode.anarchiavalentines.config.product;

import eu.okaeri.configs.OkaeriConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.ocode.anarchiavalentines.config.customitem.CustomItem;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Product extends OkaeriConfig {
    private CustomItem presenterAvaiable;
    private CustomItem presenterUnavaiable;
    private int slot;

    private int price;

    private List<String> commands;
}
