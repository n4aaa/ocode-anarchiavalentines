package pl.ocode.anarchiavalentines.command;

import eu.okaeri.commands.annotation.Command;
import eu.okaeri.commands.annotation.Executor;
import eu.okaeri.commands.bukkit.annotation.Permission;
import eu.okaeri.commands.service.CommandService;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.tasker.core.Tasker;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.ocode.anarchiavalentines.AnarchiaValentinesPlugin;
import pl.ocode.anarchiavalentines.config.MessageConfig;
import pl.ocode.anarchiavalentines.config.PluginConfig;
import pl.ocode.anarchiavalentines.menu.UpgradesMenu;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@Command(label = "valentineupgrade", aliases = "walentynkoweulepszenia", description = "Komenda z ulepszeniami walentynkowymi")
public class UpgradeCommand implements CommandService {
    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;
    private final Tasker tasker;

    @Executor(description = "Otwiera menu z ulepszeniami")
    public void _def(CommandSender commandSender) {
        if (commandSender instanceof final Player player) {
            this.tasker.newChain()
                    .supplyAsync(() -> {
                        final UpgradesMenu upgradesMenu = AnarchiaValentinesPlugin.getAnarchiaValentinesPlugin().createInstance(UpgradesMenu.class);

                        return upgradesMenu.build(player);
                    })
                    .acceptSync(menu -> menu.show(player))
                    .execute();
        }
    }
}
