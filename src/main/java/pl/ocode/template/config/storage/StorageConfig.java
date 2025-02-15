package pl.ocode.template.config.storage;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.CustomKey;
import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
public class StorageConfig extends OkaeriConfig {
    @Comment("Dostępne typy bazy danych: FLAT, MONGO, MYSQL")
    public StorageBackend backend = StorageBackend.FLAT;

    @Comment("Prefix dla bazy danych:")
    @CustomKey("prefix")
    public String prefix = "ocodetemplate";

    @Comment("FLAT   : zostaw puste pole")
    @Comment("MONGO  : mongodb://localhost:27017/db")
    @Comment("MYSQL  : jdbc:mysql://localhost:3306/db?user=root&password=1234")
    public String uri = "";

    public StorageConfig(@NonNull String prefix) {
        this.prefix = prefix;
    }
}