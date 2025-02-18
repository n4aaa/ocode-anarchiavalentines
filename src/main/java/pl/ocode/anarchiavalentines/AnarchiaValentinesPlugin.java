package pl.ocode.anarchiavalentines;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.zaxxer.hikari.HikariConfig;
import eu.okaeri.configs.json.simple.JsonSimpleConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.persistence.PersistencePath;
import eu.okaeri.persistence.document.DocumentPersistence;
import eu.okaeri.persistence.jdbc.MariaDbPersistence;
import eu.okaeri.persistence.mongo.MongoPersistence;
import eu.okaeri.platform.bukkit.OkaeriBukkitPlugin;
import eu.okaeri.platform.bukkit.persistence.YamlBukkitPersistence;
import eu.okaeri.platform.core.annotation.Bean;
import eu.okaeri.platform.core.annotation.Register;
import eu.okaeri.platform.core.plan.ExecutionPhase;
import eu.okaeri.platform.core.plan.Planned;
import lombok.Getter;
import pl.ocode.anarchiavalentines.command.AdminCommand;
import pl.ocode.anarchiavalentines.command.CostumeCommand;
import pl.ocode.anarchiavalentines.command.ShopCommand;
import pl.ocode.anarchiavalentines.command.UpgradeCommand;
import pl.ocode.anarchiavalentines.config.MessageConfig;
import pl.ocode.anarchiavalentines.config.PluginConfig;
import pl.ocode.anarchiavalentines.controller.CostumeController;
import pl.ocode.anarchiavalentines.controller.UpgradeController;
import pl.ocode.anarchiavalentines.persistence.PlayerCache;
import pl.ocode.anarchiavalentines.persistence.PlayerController;
import pl.ocode.anarchiavalentines.persistence.PlayerRepository;
import pl.ocode.anarchiavalentines.scheduler.CostumeScheduler;
import pl.ocode.anarchiavalentines.scheduler.UpgradeScheduler;

import java.io.File;

@Register(PluginConfig.class)
@Register(MessageConfig.class)

@Register(PlayerRepository.class)
@Register(PlayerCache.class)
@Register(PlayerController.class)

@Register(AnarchiaValentinesService.class)
@Register(AnarchiaValentinesController.class)
@Register(AnarchiaValentinesScheduler.class)

@Register(UpgradeController.class)
@Register(UpgradeScheduler.class)

@Register(CostumeScheduler.class)
@Register(CostumeController.class)

@Register(AdminCommand.class)
@Register(UpgradeCommand.class)
@Register(CostumeCommand.class)
@Register(ShopCommand.class)

public final class AnarchiaValentinesPlugin extends OkaeriBukkitPlugin {
    @Getter private static AnarchiaValentinesPlugin anarchiaValentinesPlugin;

    private @Inject AnarchiaValentinesService anarchiaValentinesService;

    @Planned(ExecutionPhase.PRE_SETUP)
    public void setup() {
        anarchiaValentinesPlugin = this;
    }

    @Planned(ExecutionPhase.STARTUP)
    public void onStartup() {
        this.getLogger().info("Enabled!");

        this.anarchiaValentinesService.buildItems();
    }

    @Planned(ExecutionPhase.SHUTDOWN)
    public void onShutdown() {
        this.getLogger().info("Disabled!");
    }

    @Bean(value = "persistence")
    public DocumentPersistence configurePersistence(@Inject("dataFolder") File dataFolder, PluginConfig config) {
        try { Class.forName("org.mariadb.jdbc.Driver"); } catch (ClassNotFoundException ignored) { }
        try { Class.forName("org.h2.Driver"); } catch (ClassNotFoundException ignored) { }

        PersistencePath basePath = PersistencePath.of(config.storageConfig.prefix);

        switch (config.storageConfig.backend) {
            case FLAT:
                return YamlBukkitPersistence.of(new File(dataFolder, "storage"));
            case MYSQL:
                HikariConfig mariadbHikari = new HikariConfig();
                mariadbHikari.setJdbcUrl(config.storageConfig.uri);

                return new DocumentPersistence(new MariaDbPersistence(basePath, mariadbHikari), JsonSimpleConfigurer::new, new SerdesBukkit());
            case MONGO:
                ConnectionString mongoUri = new ConnectionString(config.storageConfig.uri);
                MongoClient mongoClient = MongoClients.create(mongoUri);
                if (mongoUri.getDatabase() == null) {
                    throw new IllegalArgumentException("Mongo URI needs to specify the database");
                }
                return new DocumentPersistence(new MongoPersistence(basePath, mongoClient, mongoUri.getDatabase()), JsonSimpleConfigurer::new, new SerdesBukkit());
            default:
                throw new RuntimeException("unsupported storage backend: " + config.storageConfig.backend);
        }
    }
}
