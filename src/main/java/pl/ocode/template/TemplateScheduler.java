package pl.ocode.template;

import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.bukkit.annotation.Scheduled;
import eu.okaeri.tasker.core.Tasker;
import lombok.RequiredArgsConstructor;
import pl.ocode.template.persistence.PlayerCache;
import pl.ocode.template.persistence.PlayerProperties;

import java.util.concurrent.atomic.AtomicInteger;

@Scheduled(delay = 100, rate = 100)
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class TemplateScheduler implements Runnable {

    private final PlayerCache playerCache;
    private final Tasker tasker;

    @Override
    public void run() {
        AtomicInteger delay = new AtomicInteger();
        AtomicInteger players = new AtomicInteger();

        for (PlayerProperties properties : this.playerCache.getPropertiesToSave()) {
            if (players.get() >= 10) {
                delay.getAndIncrement();
                players.set(0);
            }

            TemplatePlugin.templatePlugin.getServer().getScheduler().runTaskLater(TemplatePlugin.templatePlugin, () ->
                    this.tasker.newSharedChain("dbops:" + properties.getUniqueId())
                            .supplyAsync(properties::save)
                            .execute(), delay.get());

            players.getAndIncrement();
        }

        this.playerCache.clearPropertiesToSave();
    }
}