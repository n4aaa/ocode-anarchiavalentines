package pl.ocode.template.command;

import eu.okaeri.commands.annotation.Command;
import eu.okaeri.commands.annotation.Executor;
import eu.okaeri.commands.bukkit.annotation.Permission;
import eu.okaeri.commands.service.CommandService;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import pl.ocode.template.config.MessageConfig;
import pl.ocode.template.config.PluginConfig;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@Command(label = "example", description = "Example command")
public class ExampleCommand implements CommandService {
    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;

    @Executor(pattern = "reload", description = "Command is reloading configs")
    @Permission("ocode-template.reload")
    public void reload(CommandSender commandSender) {
        this.pluginConfig.load();
        this.messageConfig.load();

        this.messageConfig.reload.send(commandSender);
    }
}
