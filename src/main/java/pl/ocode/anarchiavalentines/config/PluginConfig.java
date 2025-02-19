package pl.ocode.anarchiavalentines.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.platform.core.annotation.Configuration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import pl.ocode.anarchiavalentines.config.customitem.CustomItem;
import pl.ocode.anarchiavalentines.config.menu.MenuBuilder;
import pl.ocode.anarchiavalentines.config.menu.MenuItem;
import pl.ocode.anarchiavalentines.config.product.Product;
import pl.ocode.anarchiavalentines.config.storage.StorageConfig;
import pl.ocode.anarchiavalentines.config.costume.Costume;
import pl.ocode.anarchiavalentines.config.upgrade.Upgrade;
import pl.ocode.anarchiavalentines.config.upgrade.UpgradeType;
import pl.ocode.anarchiavalentines.util.ItemBuilder;
import pl.ocode.anarchiavalentines.util.builder.ListBuilder;

import java.util.Arrays;
import java.util.List;

@Configuration(path = "config.yml")
@Header("##")
@Header("## oCode-AnarchiaValentines (Plugin-Config) ##")
@Header("##")
public class PluginConfig extends OkaeriConfig {

    @Comment
    @Comment("Skonfiguruj slot pod pktórym znajduje się przycisk zamkniecia:")
    public CustomItem valentineHearth = new CustomItem(ItemBuilder.of(Material.PINK_DYE)
            .setName("&d&lWalentynkowe serce")
            .setLore(" &8» &7Jest to przedmioty z:",
                    "&8» &feventu walentynkowego (2025)",
                    "",
                    " &8» &7Rozmień je u &fkupidyna&7, aby",
                    " &8» &7otrzymać &bepickie ulepszenia&7!")
            .toItemStack(),
            0);

    @Comment
    @Comment("Skonfiguruj permissje dla 2x serc walentynkowych:")
    public String doubleHearthPermission = "quick.double_hearth";

    @Comment
    @Comment("Szansa na wykopanie serca walentynkowego z kamienia:")
    public double hearthStoneChance = 5.5;

    @Comment
    @Comment("Skonfiguruj menu sklepu:")
    public MenuBuilder shopMenu = new MenuBuilder(
            "&8Walentynkowy sklep",
            5,
            new ListBuilder<MenuItem>()
                    .add(new MenuItem(
                            40,
                            ItemBuilder.of(Material.BARRIER)
                                    .setName("&cZamknij!")
                                    .toItemStack(),
                            0
                    ))
                    .build()
    );
    @Comment
    @Comment("Skonfiguruj slot pod którym znajduje się przycisk zamkniecia:")
    public int shopMenuCloseSlot = 40;

    @Comment
    @Comment("Skonfiguruj menu ulepszeń:")
    public MenuBuilder upgradesMenu = new MenuBuilder(
            "&8Dostępne bonusy",
            5,
            new ListBuilder<MenuItem>()
                    .add(new MenuItem(
                            4,
                            ItemBuilder.of(Material.NETHER_STAR)
                                    .setName("&c&lUWAGA!")
                                    .setLore(" &8» &7Wszystkie &ebonusy &7poniżej działają",
                                            " &8» &7tylko przez czas trwania &dwydarzenia",
                                            " &8» &dwalentynkowego&7!")
                                    .toItemStack(),
                            0
                    ))
                    .add(new MenuItem(
                            40,
                            ItemBuilder.of(Material.BARRIER)
                                    .setName("&cZamknij!")
                                    .toItemStack(),
                            0
                    ))
                    .build()
    );
    @Comment
    @Comment("Skonfiguruj slot pod którym znajduje się przycisk zamkniecia:")
    public int upgradesCloseSlot = 40;

    @Comment
    @Comment("Skonfiguruj dodatkowy opis ulepszenia w menu:")
    public List<String> upgradeProductLore = Arrays.asList(
            "",
            " &8» &7Koszt: &f{price} walentynkowych serc",
            " &8» &aKliknij, aby zakupić bonus!"
    );
    @Comment
    @Comment("Skonfiguruj ulepszenia:")
    public List<Upgrade> upgrades = Arrays.asList(
            new Upgrade(
                    new MenuItem(
                            20,
                            ItemBuilder.of(Material.IRON_CHESTPLATE)
                                    .setName("&a&l+15% do obrony")
                                    .setLore(" &8» &7Otrzymujesz &f15% &7więcej obrony",
                                            " &8» &7od ataków!")
                                    .toItemStack(),
                            0
                    ),
                    UpgradeType.DAMAGE,
                    50
            ),
            new Upgrade(
                    new MenuItem(
                            21,
                            ItemBuilder.of(Material.DIAMOND_SWORD)
                                    .setName("&c&l+15% do obrażeń")
                                    .setLore(" &8» &7Zadajesz &f15% &7do ataków")
                                    .toItemStack(),
                            0
                    ),
                    UpgradeType.PROTECTION,
                    50
            ),
            new Upgrade(
                    new MenuItem(
                            22,
                            ItemBuilder.of(Material.FEATHER)
                                    .setName("&b&lPodwójny skok co 10 sekund")
                                    .setLore(" &8» &7Możesz skakać podwójnym skokiem",
                                            " &8» &7co 10 sekund!")
                                    .toItemStack(),
                            0
                    ),
                    UpgradeType.DOUBLE_JUMP,
                    50
            ),
            new Upgrade(
                    new MenuItem(
                            23,
                            ItemBuilder.of(Material.DRIPSTONE_BLOCK)
                                    .setName("&6&l+15% odporności od dripstone")
                                    .setLore(" &8» &7Otrzymujesz &f50% &7mniej obrażen",
                                            " &8» &7od dripstone!")
                                    .toItemStack(),
                            0
                    ),
                    UpgradeType.DRIPSTONE,
                    50
            ),
            new Upgrade(
                    new MenuItem(
                            24,
                            ItemBuilder.of(Material.TOTEM_OF_UNDYING)
                                    .setName("&d&l+50% zachowanych itemów po śmierci")
                                    .setLore(" &8» &7Po śmierci zachowujesz &f50% &7itemów",
                                            " &8» &7z ekwipunku tylko i wyłącznie na pierwsza",
                                            " &8» &7śmierć!")
                                    .toItemStack(),
                            0
                    ),
                    UpgradeType.KEEP_ITEMS,
                    70
            )
    );

    @Comment
    @Comment("Skonfiguruj menu aktwynych kostiumów:")
    public MenuBuilder costumeActiveMenu = new MenuBuilder(
            "&8System kostiumów",
            5,
            new ListBuilder<MenuItem>()
                    .add(new MenuItem(
                            12,
                            ItemBuilder.of(Material.RED_DYE)
                                    .setName("&cZdejmij kostium")
                                    .toItemStack(),
                            0
                    ))
                    .add(new MenuItem(
                            13,
                            ItemBuilder.of(Material.CHAINMAIL_CHESTPLATE)
                                    .setName("&cBrak kostiumu")
                                    .toItemStack(),
                            0
                    ))
                    .add(new MenuItem(
                            14,
                            ItemBuilder.of(Material.LIME_DYE)
                                    .setName("&aPrzedłuż kostium")
                                    .setLore(" &8» &7Aby przedłużyć kostium musisz",
                                            " &8» &7posiadać &fzwój przedłużenia&7, który",
                                            " &8» &7uzyskasz z zrzutów bądź na naszej",
                                            " &8» &7 stronie &ewww.ocode.pl",
                                            "",
                                            " &8» &4UWAGA! &cSerwer pobierze z Twojego",
                                            " &8» &cekwipunku, zwój z najdłuższym czasem",
                                            " &8» &cżywotności!",
                                            "",
                                            " &8» &aKliknij, aby przedłużyć!")
                                    .toItemStack(),
                            0
                    ))
                    .add(new MenuItem(
                            22,
                            ItemBuilder.of(Material.LIME_STAINED_GLASS_PANE)
                                    .setName("&7Kostium wygaśnie: &f{date}")
                                    .setLore(" &8» &7Aby przedłużyć czas żywotności",
                                            " &8» &7kostiumu `{costume}&7` użyj przycisku",
                                            " &8» &7obok!")
                                    .toItemStack(),
                            0
                    ))
                    .add(new MenuItem(
                            40,
                            ItemBuilder.of(Material.BARRIER)
                                    .setName("&cZamknij!")
                                    .toItemStack(),
                            0
                    ))
                    .add(new MenuItem(
                            44,
                            ItemBuilder.of(Material.ARMOR_STAND)
                                    .setName("&6Kostiumy")
                                    .setLore(" &8» &7W tym miejscu odnajdziesz pełną listę",
                                            " &8» &7wszystkich dostepnych &ekostiumów",
                                            " &8» &7wraz z ich efektami dodatkowymi!",
                                            "",
                                            " &8» &aKliknij &2LEWYM&a, aby przejść!")
                                    .toItemStack(),
                            0
                    ))
                    .build()
    );
    @Comment
    @Comment("Skonfiguruj slot pod którym znajduje się przycisk zdejmowania kostiumu:")
    public int costumeUnequipSlot = 12;
    @Comment
    @Comment("Skonfiguruj slot pod którym znajduje się przycisk przedłużania kostiumu:")
    public int costumeExpandSlot = 14;
    @Comment
    @Comment("Skonfiguruj slot pod którym znajduje się item kostiumu:")
    public int costumePresenterSlot = 13;
    @Comment
    @Comment("Skonfiguruj menu nie aktwynych kostiumów:")
    public MenuBuilder costumeNoActiveMenu = new MenuBuilder(
            "&8System kostiumów",
            5,
            new ListBuilder<MenuItem>()
                    .add(new MenuItem(
                            13,
                            ItemBuilder.of(Material.CHAINMAIL_CHESTPLATE)
                                    .setName("&cBrak kostiumu")
                                    .toItemStack(),
                            0
                    ))
                    .add(new MenuItem(
                            22,
                            ItemBuilder.of(Material.RED_STAINED_GLASS_PANE)
                                    .setName("&cKostiumy możesz zdobyć poprzez zakup")
                                    .setLore("&cspecjalnych pakietów okazyjncyh na naszej",
                                            "&cstronie &4www.ocode.pl")
                                    .toItemStack(),
                            0
                    ))
                    .add(new MenuItem(
                            40,
                            ItemBuilder.of(Material.BARRIER)
                                    .setName("&cZamknij!")
                                    .toItemStack(),
                            0
                    ))
                    .add(new MenuItem(
                            44,
                            ItemBuilder.of(Material.ARMOR_STAND)
                                    .setName("&6Kostiumy")
                                    .setLore(" &8» &7W tym miejscu odnajdziesz pełną listę",
                                            " &8» &7wszystkich dostepnych &ekostiumów",
                                            " &8» &7wraz z ich efektami dodatkowymi!",
                                            "",
                                            " &8» &aKliknij &2LEWYM&a, aby przejść!")
                                    .toItemStack(),
                            0
                    ))
                    .build()
    );
    @Comment
    @Comment("Skonfiguruj slot pod którym znajduje się przycisk zamkniecia:")
    public int costumeCloseSlot = 40;
    @Comment
    @Comment("Skonfiguruj slot pod którym znajduje się przycisk listy kostiumów:")
    public int costumeListSlot = 44;
    @Comment
    @Comment("Skonfiguruj menu listy kostiumów:")
    public MenuBuilder costumeListMenu = new MenuBuilder(
            "&8Lista kostiumów",
            6,
            new ListBuilder<MenuItem>()
                    .add(new MenuItem(
                            49,
                            ItemBuilder.of(Material.ARROW)
                                    .setName("&cPowrót")
                                    .toItemStack(),
                            0
                    ))
                    .build()
    );
    @Comment
    @Comment("Skonfiguruj slot pod którym znajduje się przycisk zamkniecia:")
    public int costumeListCloseSlot = 49;

    @Comment
    @Comment("Skonfiguruj dodatkowy opis kostiumu:")
    public List<String> costumeAdditionalLore = Arrays.asList(
            "",
            "&7Kostium wygaśnie: &f{date}",
            "&aKliknij prawym, aby założyć!"
    );
    @Comment
    @Comment("Skonfiguruj dodatkowy opis kostiumu:")
    public String costumeAdditionalName = "&7Kostium &dwalentynkowy &7na czas &5{time}";
    @Comment
    @Comment("Skonfiguruj kostium walentynkowy:")
    public Costume valentineCostume = new Costume(
            "walentynkowy",
            22,
            new CustomItem(ItemBuilder.of(Material.LEATHER_CHESTPLATE)
                    .setName("&7Kostium &dwalentynkowy")
                    .setLore("&8Unikatowa wersja!",
                            "&7Został on wydany jako pierwszy w historii serwerów",
                            "&7w dniu &aWalentynek (2025)&7, aby uczcić miłość!",
                            "",
                            "&7Dzięki temu przedmiotowi otrzymasz",
                            "&funikatowy &7wyglą oraz &fepickie &7bonusy",
                            "",
                            "&7Lista bonusów:",
                            " &8» &4+5 &7serc",
                            " &8» &c12% &7dodatkowych obrażeń",
                            " &8» &e100% &7odporności od upadku",
                            " &8» &b+5% &7obrony")
                    .addFlags(ItemFlag.HIDE_ATTRIBUTES,
                            ItemFlag.HIDE_DYE,
                            ItemFlag.HIDE_UNBREAKABLE,
                            ItemFlag.HIDE_ENCHANTS,
                            ItemFlag.HIDE_DESTROYS,
                            ItemFlag.HIDE_POTION_EFFECTS)
                    .toItemStack(),
                    0),
            14378980,
            "ewogICJ0aW1lc3RhbXAiIDogMTczOTU2ODEzODgzNiwKICAicHJvZmlsZUlkIiA6ICJjNjI5MzRjYmE5YTE0NjYwYTk3NTk0YjNlMjQ0ZjhhOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJBQUFycm9iYSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80ZGUyMGRkYjJjN2NhZDJhMjc3ZjFhOTE3NTk1MTg5N2Y3NTNjMTE4MDE5YTVmZDQyMDc1OWQ2MzE1ODdlNDNhIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
            ItemBuilder.of(Material.PLAYER_HEAD).toItemStack(),
            ItemBuilder.of(Material.LEATHER_CHESTPLATE).toItemStack(),
            ItemBuilder.of(Material.LEATHER_LEGGINGS).toItemStack(),
            ItemBuilder.of(Material.LEATHER_BOOTS).toItemStack()
    );

    @Comment
    @Comment("Skonfiguruj zwój przedłużenia:")
    public CustomItem rollItem = new CustomItem(ItemBuilder.of(Material.FLOWER_BANNER_PATTERN)
            .setName("&a&lZwój przedłużenia")
            .setLore(" &8» &7Służy do &bprzedłużenia",
                    " &8» &7kostiumu o &b{time}")
            .toItemStack(),
            0);

    @Comment
    @Comment("Lista przedmiotów:")
    public List<Product> products = Arrays.asList(
            new Product(new CustomItem(ItemBuilder.of(Material.TRIPWIRE_HOOK)
                    .setName("&5Epicki klucz")
                    .setLore(" &8» &7Koszt to 400 sztuk:",
                            "  &4● &cWalentynkowe serca",
                            "",
                            " &8» &7Kupując tą usługę otrzymasz",
                            " &8» &7jedna sztuke &depickiego klucza&7!",
                            "",
                            " &8» &eBrak wystarczającej ilosći!")
                    .toItemStack(),
                    0),
                    new CustomItem(ItemBuilder.of(Material.TRIPWIRE_HOOK)
                            .setName("&5Epicki klucz")
                            .setLore(" &8» &7Koszt to 400 sztuk:",
                                    "  &2● &aWalentynkowe serca",
                                    "",
                                    " &8» &7Kupując tą usługę otrzymasz",
                                    " &8» &7jedna sztuke &depickiego klucza&7!")
                            .toItemStack(),
                            0),
                    11,
                    400,
                    Arrays.asList("say {player} zakupil epicki klucz")
            ),
            new Product(new CustomItem(ItemBuilder.of(Material.TRIPWIRE_HOOK)
                    .setName("&4Anarchiczny klucz")
                    .setLore(" &8» &7Koszt to 2,000 sztuk:",
                            "  &4● &cWalentynkowe serca",
                            "",
                            " &8» &7Kupując tą usługę otrzymasz",
                            " &8» &7jedna sztuke &canarchicznego&7!",
                            "",
                            " &8» &eBrak wystarczającej ilosći!")
                    .toItemStack(),
                    0),
                    new CustomItem(ItemBuilder.of(Material.TRIPWIRE_HOOK)
                            .setName("&4Anarchiczny kluczw")
                            .setLore(" &8» &7Koszt to 2,000 sztuk:",
                                    "  &2● &aWalentynkowe serca",
                                    "",
                                    " &8» &7Kupując tą usługę otrzymasz",
                                    " &8» &7jedna sztuke &canarchicznego&7!")
                            .toItemStack(),
                            0),
                    15,
                    2000,
                    Arrays.asList("say {player} zakupil anarchiczny klucz")
            )
    );

    @Comment
    @Comment("UWAGA! Edytuj to ostrożnie!")
    @Comment("Moc podwójnego skoku:")
    public double doubleJumpMultiply = 0.6;
    @Comment
    @Comment("UWAGA! Edytuj to ostrożnie!")
    @Comment("Moc podwójnego skoku Y:")
    public double doubleJumpY = 0.7;

    @Comment
    @Comment("Skonfiguruj dane dla bazy danych:")
    public StorageConfig storageConfig = new StorageConfig("ocodeanarchiavalentines");
}