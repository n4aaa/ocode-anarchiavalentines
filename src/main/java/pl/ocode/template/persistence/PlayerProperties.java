package pl.ocode.template.persistence;

import eu.okaeri.persistence.document.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerProperties extends Document {
    private String name;

    public UUID getUniqueId() {
        return this.getPath().toUUID();
    }
}