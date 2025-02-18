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
import pl.ocode.anarchiavalentines.menu.CostumeMenu;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@Command(label = "valentinecostume", aliases = {
        "walentynkowykostium",
        "kostium"
}, description = "Komenda z kostiumami")
public class CostumeCommand implements CommandService {
    private final MessageConfig messageConfig;
    private final Tasker tasker;

    @Executor(description = "Otwiera menu z kostiumami")
    public void _def(CommandSender commandSender) {
        if (commandSender instanceof final Player player) {
            this.tasker.newChain()
                    .supplyAsync(() -> {
                        final CostumeMenu costumeMenu = AnarchiaValentinesPlugin.getAnarchiaValentinesPlugin().createInstance(CostumeMenu.class);

                        return costumeMenu.build(player);
                    })
                    .acceptSync(menu -> menu.show(player))
                    .execute();
        }
    }
}
