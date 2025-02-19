package pl.ocode.anarchiavalentines;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.injector.annotation.PostConstruct;
import eu.okaeri.platform.core.annotation.Component;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import pl.ocode.anarchiavalentines.config.PluginConfig;
import pl.ocode.anarchiavalentines.config.costume.Costume;
import pl.ocode.anarchiavalentines.util.ItemBuilder;

import java.util.*;

@Getter
@Component
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class AnarchiaValentinesService {
    private final PluginConfig pluginConfig;

    private final List<UUID> playersToUpdate = new ArrayList<>();

    private final NamespacedKey costumeName = new NamespacedKey(AnarchiaValentinesPlugin.getAnarchiaValentinesPlugin(), "costumeName");
    private final NamespacedKey costumeDuration = new NamespacedKey(AnarchiaValentinesPlugin.getAnarchiaValentinesPlugin(), "costumeDuration");
    private final NamespacedKey costumeExpiration = new NamespacedKey(AnarchiaValentinesPlugin.getAnarchiaValentinesPlugin(), "costumeExpiration");
    private final NamespacedKey rollValue = new NamespacedKey(AnarchiaValentinesPlugin.getAnarchiaValentinesPlugin(), "rollValue");

    private ProtocolManager protocolManager;

    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;

    @PostConstruct
    private void setup() {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    public void buildItems() {
        Costume costume = this.pluginConfig.valentineCostume;

        helmet = ItemBuilder.of(costume.getHelmet())
                .setSkin(costume.getValue())
                .toItemStack();
        chestplate = ItemBuilder.of(costume.getChestplate())
                .setColor(Color.fromBGR(costume.getColor()))
                .toItemStack();
        leggings = ItemBuilder.of(costume.getLeggings())
                .setColor(Color.fromBGR(costume.getColor()))
                .toItemStack();
        boots = ItemBuilder.of(costume.getBoots())
                .setColor(Color.fromBGR(costume.getColor()))
                .toItemStack();
    }

    public void apply(@NonNull Attribute attribute, @NonNull AttributeInstance attributeInstance, @NonNull int amount) {
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "OCODE_" + attribute.name(), amount, AttributeModifier.Operation.ADD_NUMBER);
        attributeInstance.addModifier(modifier);
    }
    public void remove(@NonNull Attribute attribute, @NonNull AttributeInstance attributeInstance) {
        attributeInstance.getModifiers().stream().filter((modifier) -> modifier.getName().equals("OCODE_" + attribute.name())).forEach(attributeInstance::removeModifier);
    }
}