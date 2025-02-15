package pl.ocode.template.persistence;

import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.persistence.document.Document;
import eu.okaeri.platform.core.annotation.Component;
import eu.okaeri.tasker.core.Tasker;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class PlayerCache extends Document {
    private final PlayerRepository playerRepository;
    private final Tasker tasker;

    private final Map<UUID, PlayerProperties> propertiesCache = new HashMap<>();
    private final List<UUID> propertiesToSave = new ArrayList<>();

    public Optional<PlayerProperties> getProperties(@NonNull UUID playerUuid) {
        return Optional.ofNullable(this.propertiesCache.get(playerUuid));
    }

    public void createProperties(@NonNull Player player) {
        final UUID playerUuid = player.getUniqueId();
        if (this.propertiesCache.containsKey(playerUuid)) {
            return;
        }

        this.tasker.newSharedChain("dbops:" + playerUuid)
                .supplyAsync(() -> this.playerRepository.get(player))
                .acceptSync(properties -> this.propertiesCache.put(playerUuid, properties))
                .execute();
    }

    public void removeProperties(@NonNull UUID playerUuid) {
        if (!this.propertiesCache.containsKey(playerUuid)) {
            return;
        }

        this.propertiesCache.remove(playerUuid);
    }

    public List<PlayerProperties> getPropertiesToSave() {
        return this.propertiesToSave
                .stream()
                .map(uuid -> this.getProperties(uuid).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void markPropertiesToSave(@NonNull PlayerProperties playerProperties) {
        if (this.propertiesToSave.contains(playerProperties.getUniqueId())) {
            return;
        }

        this.propertiesToSave.add(playerProperties.getUniqueId());
    }

    public void clearPropertiesToSave() {
        this.propertiesToSave.clear();
    }
}