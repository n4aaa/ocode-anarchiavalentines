package pl.ocode.template;

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
import lombok.RequiredArgsConstructor;
import pl.ocode.template.command.ExampleCommand;
import pl.ocode.template.config.MessageConfig;
import pl.ocode.template.config.PluginConfig;
import pl.ocode.template.persistence.PlayerCache;
import pl.ocode.template.persistence.PlayerController;
import pl.ocode.template.persistence.PlayerRepository;

import java.io.File;

@Register(PluginConfig.class)
@Register(MessageConfig.class)

@Register(PlayerRepository.class)
@Register(PlayerCache.class)
@Register(PlayerController.class)

@Register(TemplateScheduler.class)
@Register(ExampleCommand.class)

@RequiredArgsConstructor(onConstructor_ = @Inject)
public final class TemplatePlugin extends OkaeriBukkitPlugin {
    public static TemplatePlugin templatePlugin;

    @Planned(ExecutionPhase.STARTUP)
    public void onStartup() {
        templatePlugin = this;

        this.getLogger().info("Enabled!");
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
