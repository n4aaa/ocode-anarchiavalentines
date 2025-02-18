package pl.ocode.anarchiavalentines.util;

import com.cryptomorin.xseries.SkullUtils;
import lombok.NonNull;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import pl.ocode.anarchiavalentines.config.skin.Skin;
import pl.ocode.anarchiavalentines.util.builder.ListBuilder;
import pl.ocode.anarchiavalentines.util.string.ChatUtil;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ItemBuilder {

    private ItemStack itemStack;

    public ItemBuilder(@NonNull Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder(@NonNull Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
    }

    public ItemBuilder(@NonNull ItemStack itemStack, boolean clone) {
        if (clone) {
            this.itemStack = new ItemStack(itemStack);
        }
        else {
            this.itemStack = itemStack;
        }
    }

    public static ItemBuilder of(@NonNull Material material) {
        return new ItemBuilder(material);
    }

    public static ItemBuilder of(@NonNull Material material, int amount) {
        return new ItemBuilder(material, amount);
    }

    public static ItemBuilder of(@NonNull ItemStack itemStack) {
        return new ItemBuilder(itemStack, true);
    }

    public static ItemBuilder manipulate(@NonNull ItemStack itemStack) {
        return new ItemBuilder(itemStack, false);
    }

    public ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder setType(@NonNull Material material) {
        this.itemStack.setType(material);
        return this;
    }

    public ItemBuilder setType(@NonNull ItemStack itemStack) {
        return this.setType(itemStack, true);
    }

    public ItemBuilder setType(@NonNull ItemStack itemStack, boolean clone) {

        final ItemStack copy;
        if (clone) {
            copy = new ItemStack(itemStack);
        }
        else {
            copy = itemStack;
        }

        copy.setAmount(this.itemStack.getAmount());

        if (this.itemStack.hasItemMeta()) {
            copy.setItemMeta(this.itemStack.getItemMeta());
        }

        this.itemStack = copy;
        return this;
    }

    public ItemBuilder withDurability(int durability) {
        this.itemStack.setDurability((short) durability);
        return this;
    }

    public ItemBuilder setName(@NonNull String name) {

        ItemMeta itemMeta = this.itemStack.getItemMeta();
        assert itemMeta != null;

        itemMeta.setDisplayName(name);
        this.itemStack.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder setColor(@NonNull Color color) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        assert itemMeta != null;

        if (itemMeta instanceof LeatherArmorMeta leatherArmorMeta) {
            leatherArmorMeta.setColor(color);
            this.itemStack.setItemMeta(leatherArmorMeta);
        }

        return this;
    }


    public ItemBuilder startLoreWith(@NonNull List<String> lore) {

        ItemMeta itemMeta = this.itemStack.getItemMeta();
        assert itemMeta != null;

        if (itemMeta.hasLore()) {
            itemMeta.setLore(new ListBuilder<String>()
                    .addAll(lore)
                    .addAll(Objects.requireNonNull(itemMeta.getLore()))
                    .build());
        }
        else {
            itemMeta.setLore(lore);
        }

        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder startLoreWith(@NonNull String... lore) {
        return this.startLoreWith(Arrays.asList(lore));
    }

    public ItemBuilder appendLore(@NonNull List<String> lore) {

        ItemMeta itemMeta = this.itemStack.getItemMeta();
        assert itemMeta != null;

        if (itemMeta.hasLore()) {
            itemMeta.setLore(new ListBuilder<String>()
                    .addAll(Objects.requireNonNull(itemMeta.getLore()))
                    .addAll(lore)
                    .build());
        }
        else {
            itemMeta.setLore(lore);
        }

        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder appendLore(@NonNull String... lore) {
        return this.appendLore(Arrays.asList(lore));
    }

    public ItemBuilder setLore(@NonNull List<String> lore) {

        ItemMeta itemMeta = this.itemStack.getItemMeta();
        assert itemMeta != null;

        itemMeta.setLore(lore);
        this.itemStack.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder setCmd(@NonNull int cmd) {
        if (cmd != 0) {
            ItemMeta itemMeta = this.itemStack.getItemMeta();
            assert itemMeta != null;

            itemMeta.setCustomModelData(cmd);
            this.itemStack.setItemMeta(itemMeta);
        }

        return this;
    }

    public ItemBuilder withKey(@NonNull NamespacedKey namespacedKey, @NonNull PersistentDataType persistentDataType, @NonNull Object object) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        assert itemMeta != null;

        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();

        persistentDataContainer.set(namespacedKey, persistentDataType, object);
        this.itemStack.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder setLore(@NonNull String... lore) {
        return this.setLore(Arrays.asList(lore));
    }

    public ItemBuilder addEnchant(@NonNull Enchantment enchantment, int level, boolean ignoreLevelRestriction) {

        ItemMeta itemMeta = this.itemStack.getItemMeta();
        assert itemMeta != null;

        itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction);
        this.itemStack.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder addEnchant(@NonNull Enchantment enchantment, int level) {
        return this.addEnchant(enchantment, level, true);
    }

    public ItemBuilder addFlags(@NonNull ItemFlag... itemFlag) {

        ItemMeta itemMeta = this.itemStack.getItemMeta();
        assert itemMeta != null;

        itemMeta.addItemFlags(itemFlag);
        this.itemStack.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder fixColors() {

        ItemMeta itemMeta = this.itemStack.getItemMeta();
        assert itemMeta != null;

        if (itemMeta.hasDisplayName()) {
            itemMeta.setDisplayName(ChatUtil.fixColor(itemMeta.getDisplayName()));
        }

        if (itemMeta.hasLore()) {
            itemMeta.setLore(Objects.requireNonNull(itemMeta.getLore())
                    .stream()
                    .map(ChatUtil::fixColor)
                    .collect(Collectors.toList()));
        }

        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder fixColors(@NonNull Map<String, Object> placeholders) {

        ItemMeta itemMeta = this.itemStack.getItemMeta();
        assert itemMeta != null;

        if (itemMeta.hasDisplayName()) {
            final String compiledMessage = ChatUtil.fixColor(itemMeta.getDisplayName(), placeholders);

            itemMeta.setDisplayName(compiledMessage);
        }

        if (itemMeta.hasLore()) {
            itemMeta.setLore(Objects.requireNonNull(itemMeta.getLore())
                    .stream()
                    .map(text -> ChatUtil.fixColor(text, placeholders))
                    .collect(Collectors.toList()));
        }

        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder fixColors(@NonNull Map<String, Object> placeholders, boolean colorizePlaceholders) {

        ItemMeta itemMeta = this.itemStack.getItemMeta();
        assert itemMeta != null;

        if (itemMeta.hasDisplayName()) {
            final String compiledMessage = ChatUtil.fixColor(itemMeta.getDisplayName(), placeholders, colorizePlaceholders);

            itemMeta.setDisplayName(compiledMessage);
        }

        if (itemMeta.hasLore()) {
            itemMeta.setLore(Objects.requireNonNull(itemMeta.getLore())
                    .stream()
                    .map(text -> ChatUtil.fixColor(text, placeholders, colorizePlaceholders))
                    .collect(Collectors.toList()));
        }

        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder withCustomMeta(@NonNull Function<ItemMeta, ItemMeta> function) {

        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        this.itemStack.setItemMeta(function.apply(itemMeta));

        return this;
    }

    public ItemBuilder setSkin(Skin skin) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        assert itemMeta != null;

        SkullMeta skullMeta = (SkullMeta) this.itemStack.getItemMeta();
        assert skullMeta != null;

        SkullUtils.setSkullBase64(skullMeta, skin.getValue(), skin.getSignature());
        this.itemStack.setItemMeta(skullMeta);

        return this;
    }

    public ItemStack toItemStack() {
        return this.itemStack;
    }
}