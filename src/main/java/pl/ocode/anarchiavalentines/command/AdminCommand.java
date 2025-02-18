package pl.ocode.anarchiavalentines.command;

import eu.okaeri.commands.annotation.Arg;
import eu.okaeri.commands.annotation.Command;
import eu.okaeri.commands.annotation.Completion;
import eu.okaeri.commands.annotation.Executor;
import eu.okaeri.commands.bukkit.annotation.Permission;
import eu.okaeri.commands.service.CommandService;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import pl.ocode.anarchiavalentines.AnarchiaValentinesService;
import pl.ocode.anarchiavalentines.config.MessageConfig;
import pl.ocode.anarchiavalentines.config.PluginConfig;
import pl.ocode.anarchiavalentines.util.ItemBuilder;
import pl.ocode.anarchiavalentines.util.ItemUtil;
import pl.ocode.anarchiavalentines.util.TimeUtil;
import pl.ocode.anarchiavalentines.util.builder.MapBuilder;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@Command(label = "ocodeanarchiavalentines", description = "Komenda administracyjna")
public class AdminCommand implements CommandService {
    private final AnarchiaValentinesService anarchiaValentinesService;
    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;

    @Executor(pattern = "reload", description = "Odświeża pliki konfiguracyjne")
    @Permission("ocode-anarchiavalentines.reload")
    public void reload(CommandSender commandSender) {
        this.pluginConfig.load();
        this.messageConfig.load();

        this.anarchiaValentinesService.buildItems();

        this.messageConfig.reload.send(commandSender);
    }

    @Executor(pattern = "kostium *", description = "Nadaje kostium graczowi na określony czas")
    @Completion(arg = "time", value = {
            "1s",
            "1m",
            "1h",
            "1d",
            "1w",
            "1y"
    })
    @Permission("ocode-anarchiavalentines.costume")
    public void costume(CommandSender commandSender, @Arg String time) {
        if (commandSender instanceof Player player) {
            final NamespacedKey costumeName = this.anarchiaValentinesService.getCostumeName();
            final NamespacedKey costumeDuration = this.anarchiaValentinesService.getCostumeDuration();

            final long duration = TimeUtil.stringToTime(time);

            ItemStack costumeItem = ItemBuilder.of(this.pluginConfig.valentineCostume.getPresenter().toItemStack())
                    .setName(this.pluginConfig.costumeAdditionalName)
                    .fixColors(new MapBuilder<String, Object>()
                            .put("time", TimeUtil.convertTime(duration))
                            .build())
                    .setColor(Color.fromBGR(this.pluginConfig.valentineCostume.getColor()))
                    .withKey(costumeName,
                            PersistentDataType.STRING,
                            this.pluginConfig.valentineCostume.getName())
                    .withKey(costumeDuration,
                            PersistentDataType.LONG,
                            duration)
                    .toItemStack();

            ItemUtil.giveItem(player, costumeItem);

            this.messageConfig.receivedCostume.send(commandSender, new MapBuilder<String, Object>()
                    .put("costume", "&d" + this.pluginConfig.valentineCostume.getName())
                    .build());
        }
    }

    @Executor(pattern = "roll *", description = "Nadaje graczowi zwój do przedłużenia kostiumu o dany czas")
    @Completion(arg = "time", value = {
            "1s",
            "1m",
            "1h",
            "1d",
            "1w",
            "1y"
    })
    @Permission("ocode-anarchiavalentines.roll")
    public void roll(CommandSender commandSender, @Arg String time) {
        if (commandSender instanceof Player player) {
            final NamespacedKey rollValue = this.anarchiaValentinesService.getRollValue();

            final long duration = TimeUtil.stringToTime(time);

            ItemStack rollItem = ItemBuilder.of(this.pluginConfig.rollItem.toItemStack())
                    .fixColors(new MapBuilder<String, Object>()
                            .put("time", TimeUtil.convertTime(duration))
                            .build())
                    .withKey(rollValue,
                            PersistentDataType.LONG,
                            duration)
                    .toItemStack();

            ItemUtil.giveItem(player, rollItem);

            this.messageConfig.receivedRoll.send(commandSender, new MapBuilder<String, Object>()
                    .put("time", TimeUtil.convertTime(duration))
                    .build());
        }
    }

    @Executor(pattern = "serce", description = "Nadaje serce walentynkowe danemu graczu")
    @Permission("ocode-anarchiavalentines.hearth")
    public void hearth(CommandSender commandSender) {
        if (commandSender instanceof Player player) {
            ItemUtil.giveItem(player, this.pluginConfig.valentineHearth.toItemStack());

            this.messageConfig.receivedHearth.send(commandSender);
        }
    }
    @Executor(pattern = "serce *", description = "Nadaje serce walentynkowe danemu graczu")
    @Permission("ocode-anarchiavalentines.hearth")
    public void hearth(CommandSender commandSender, @Arg Player target) {
        ItemUtil.giveItem(target, this.pluginConfig.valentineHearth.toItemStack());

        this.messageConfig.receivedHearth.send(target);
        this.messageConfig.givenHearth.send(commandSender, new MapBuilder<String, Object>()
                .put("player", target.getName())
                .build());
    }
}
