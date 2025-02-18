package pl.ocode.anarchiavalentines.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.platform.core.annotation.Configuration;
import pl.ocode.anarchiavalentines.config.message.Message;
import pl.ocode.anarchiavalentines.config.message.MessageType;

@Configuration(path = "message.yml")
@Header("##")
@Header("## oCode-AnarchiaValentines (Message-Config) ##")
@Header("## Dostępne typy wiadomości: NOTHING, CHAT, ACTIONBAR, SUBTITLE, TITLE, TITLE_SUBTITLE")
@Header("##")
public class MessageConfig extends OkaeriConfig {

    @Comment
    @Comment("Wiadomość dla gracza ktory nadal serce:")
    public Message givenHearth = new Message(MessageType.SUBTITLE, "&aNadałeś serce walentynkowe dla {player}!");
    @Comment
    @Comment("Wiadomość dla gracza ktory dostal serce:")
    public Message receivedHearth = new Message(MessageType.SUBTITLE, "&aDostałeś serce walentynkowe!");
    @Comment
    @Comment("Wiadomość dla gracza ktory posiada juz to ulepszenie:")
    public Message upgradeOwned = new Message(MessageType.SUBTITLE, "&cPosiadasz już to ulepszenie!");
    @Comment
    @Comment("Wiadomość dla gracza ktory zakupil ulepszenie:")
    public Message upgradeBuy = new Message(MessageType.SUBTITLE, "&aZakupiono ulepszenie!");
    @Comment
    @Comment("Wiadomość dla gracza ktory posiada za malo serc walentynkowych:")
    public Message itemsMissing = new Message(MessageType.SUBTITLE, "&cNie posiadasz walentynkowych serc!");
    @Comment
    @Comment("Wiadomość dla gracza ktory ma juz zalozony kostium:")
    public Message cantEquipCostume = new Message(MessageType.SUBTITLE, "&cMasz już założony kostium!");
    @Comment
    @Comment("Wiadomość dla gracza ktory zalozyl kostium:")
    public Message costumeEquipped = new Message(MessageType.SUBTITLE, "&aZałożyłeś kostium!");
    @Comment
    @Comment("Wiadomość dla gracza ktory dostal kostium:")
    public Message receivedCostume = new Message(MessageType.SUBTITLE, "&7Dostałeś kostium `{costume}&7`");
    @Comment
    @Comment("Wiadomość dla gracza ktorego kostium wygasl:")
    public Message costumeExpired = new Message(MessageType.SUBTITLE, "&cKostium wygasł!");
    @Comment
    @Comment("Wiadomość dla gracza ktory przedluzyl kostium o dany czas:")
    public Message costumeExpanded = new Message(MessageType.SUBTITLE, "&aPrzedłużyłeś kostium o &2{time}&a!");
    @Comment
    @Comment("Wiadomość dla gracza ktory nie posiada zwoju ulepszenia:")
    public Message rollMissing = new Message(MessageType.SUBTITLE, "&cNie posiadasz zwoju przedłużenia!");
    @Comment
    @Comment("Wiadomość dla gracza ktory dostal zwoj ulepszenia:")
    public Message receivedRoll = new Message(MessageType.SUBTITLE, "&7Dostałeś zwój przedłużenia na &2{time}&a!");
    @Comment
    @Comment("Wiadomość dla gracza ktory zakupil produkt:")
    public Message productBuy = new Message(MessageType.SUBTITLE, "&aZakupiono produkt!");
    @Comment
    @Comment("Wiadomość dla gracza ktory wykopal serce z kamienia:")
    public Message hearthFromStone = new Message(MessageType.SUBTITLE, "&aWykopałeś serce walentynkowe z kamienia!");
    @Comment
    @Comment("Wiadomość o odswiezeniu konfiguracji:")
    public Message reload = new Message(MessageType.SUBTITLE, "&aOdświeżono konfigurację!");
}