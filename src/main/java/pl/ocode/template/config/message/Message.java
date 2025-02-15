package pl.ocode.template.config.message;

import eu.okaeri.configs.OkaeriConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import pl.ocode.template.util.MessageUtil;
import pl.ocode.template.util.string.ChatUtil;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class Message extends OkaeriConfig {
    private MessageType type;
    private String message;

    public void send(CommandSender commandSender) {
        MessageUtil.message(commandSender, this.type, this.message);
    }

    public void send(CommandSender commandSender, Map<String, Object> placeholders) {
        MessageUtil.message(commandSender, this.type, ChatUtil.fixColor(this.message, placeholders));
    }
}
