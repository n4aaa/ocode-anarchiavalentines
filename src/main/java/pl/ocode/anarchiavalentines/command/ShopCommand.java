package pl.ocode.anarchiavalentines.command;

import eu.okaeri.commands.annotation.Command;
import eu.okaeri.commands.annotation.Executor;
import eu.okaeri.commands.service.CommandService;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.tasker.core.Tasker;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.ocode.anarchiavalentines.AnarchiaValentinesPlugin;
import pl.ocode.anarchiavalentines.config.MessageConfig;
import pl.ocode.anarchiavalentines.config.PluginConfig;
import pl.ocode.anarchiavalentines.menu.ShopMenu;
import pl.ocode.anarchiavalentines.menu.UpgradesMenu;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@Command(label = "valentineshop", aliases = "walentynkowysklep", description = "Komenda ze sklepem walentynkowym")
public class ShopCommand implements CommandService {
    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;
    private final Tasker tasker;

    @Executor(description = "Otwiera menu z ulepszeniami")
    public void _def(CommandSender commandSender) {
        if (commandSender instanceof final Player player) {
            this.tasker.newChain()
                    .supplyAsync(() -> {
                        final ShopMenu shopMenu = AnarchiaValentinesPlugin.getAnarchiaValentinesPlugin().createInstance(ShopMenu.class);

                        return shopMenu.build(player);
                    })
                    .acceptSync(menu -> menu.show(player))
                    .execute();
        }
    }
}
