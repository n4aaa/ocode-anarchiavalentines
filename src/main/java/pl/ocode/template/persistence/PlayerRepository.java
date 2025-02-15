package pl.ocode.template.persistence;

import eu.okaeri.persistence.document.DocumentPersistence;
import eu.okaeri.persistence.repository.DocumentRepository;
import eu.okaeri.persistence.repository.annotation.DocumentCollection;
import eu.okaeri.persistence.repository.annotation.DocumentIndex;
import eu.okaeri.platform.core.annotation.DependsOn;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

@DependsOn(
        type = DocumentPersistence.class,
        name = "persistence"
)
@DocumentCollection(path = "player", keyLength = 36, indexes = @DocumentIndex(path = "name", maxLength = 24))
public interface PlayerRepository extends DocumentRepository<UUID, PlayerProperties> {
    default PlayerProperties get(OfflinePlayer player) {
        PlayerProperties properties = this.findOrCreateByPath(player.getUniqueId());

        if (player.getName() != null) {
            properties.setName(player.getName());
        }

        return properties;
    }

    default PlayerProperties get(UUID uuid) {
        return this.findOrCreateByPath(uuid);
    }
}