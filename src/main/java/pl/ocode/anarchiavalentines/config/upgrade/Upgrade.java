package pl.ocode.anarchiavalentines.config.upgrade;

import eu.okaeri.configs.OkaeriConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.ocode.anarchiavalentines.config.menu.MenuItem;

@Getter
@Setter
@AllArgsConstructor
public class Upgrade extends OkaeriConfig {
    private MenuItem presenter;

    private UpgradeType type;

    private int price;
}
