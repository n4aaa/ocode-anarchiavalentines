package pl.ocode.template.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.platform.core.annotation.Configuration;
import org.bukkit.Material;
import pl.ocode.template.config.menu.MenuBuilder;
import pl.ocode.template.config.menu.MenuItem;
import pl.ocode.template.config.storage.StorageConfig;
import pl.ocode.template.util.ItemBuilder;
import pl.ocode.template.util.builder.ListBuilder;

import java.util.Arrays;

@Configuration(path = "config.yml")
@Header("##")
@Header("## ocode-Template (Plugin-Config) ##")
@Header("##")
public class PluginConfig extends OkaeriConfig {

    @Comment
    @Comment("Skonfiguruj menu:")
    public MenuBuilder templateMenu = new MenuBuilder(
            "",
            6,
            new ListBuilder<MenuItem>()
                    .add(new MenuItem(
                            Arrays.asList(4),
                            ItemBuilder.of(Material.DIRT)
                                    .setName("&6Dirt")
                                    .toItemStack(),
                            0
                    ))
                    .build()
    );

    @Comment
    @Comment("Skonfiguruj dane dla bazy danych:")
    public StorageConfig storageConfig = new StorageConfig("ocodetemplate");
}