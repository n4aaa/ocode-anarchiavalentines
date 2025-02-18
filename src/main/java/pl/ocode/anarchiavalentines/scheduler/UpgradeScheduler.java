package pl.ocode.anarchiavalentines.scheduler;

import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.bukkit.annotation.Scheduled;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.ocode.anarchiavalentines.AnarchiaValentinesService;

import java.util.UUID;

@Scheduled(delay = 20, rate = 20)
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class UpgradeScheduler implements Runnable {

    private final AnarchiaValentinesService anarchiaValentinesService;

    @Override
    public void run() {
        var iterator = this.anarchiaValentinesService.getPlayersToUpdate().iterator();

        while (iterator.hasNext()) {
            UUID uuid = iterator.next();

            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                iterator.remove();
                continue;
            }

            if (!player.isOnGround()) {
                continue;
            }

            iterator.remove();
            player.setAllowFlight(true);
        }
    }
}