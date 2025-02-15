package pl.ocode.template.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.platform.core.annotation.Configuration;
import pl.ocode.template.config.message.Message;
import pl.ocode.template.config.message.MessageType;

@Configuration(path = "message.yml")
@Header("##")
@Header("## ocode-Template (Message-Config) ##")
@Header("## Dostępne typy wiadomości: NOTHING, CHAT, ACTIONBAR, SUBTITLE, TITLE, TITLE_SUBTITLE")
@Header("##")
public class MessageConfig extends OkaeriConfig {

    public Message reload = new Message(MessageType.SUBTITLE, "&aOdświeżono konfigurację!");
}