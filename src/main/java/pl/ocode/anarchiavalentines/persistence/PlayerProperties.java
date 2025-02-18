package pl.ocode.anarchiavalentines.persistence;

import eu.okaeri.persistence.document.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.ocode.anarchiavalentines.config.upgrade.UpgradeType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerProperties extends Document {
    private String name;

    private Map<UpgradeType, Boolean> upgrades = new HashMap<>();
    private boolean keepItemsUsed = false;

    private String costume = "";
    private Long costumeExpiration = 0L;

    public UUID getUniqueId() {
        return this.getPath().toUUID();
    }
}