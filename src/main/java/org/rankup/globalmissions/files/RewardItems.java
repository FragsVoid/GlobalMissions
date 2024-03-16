package org.rankup.globalmissions.files;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.rankup.globalmissions.GlobalMissions;

import java.util.HashMap;
import java.util.Map;

public class RewardItems {

    private final Material material;

    private final String customName;

    private final int amount;
    private final Map<Enchantment, Integer> enchantmentToLevelMap;



    public RewardItems(ConfigurationSection section) {
        Material material;
        this.enchantmentToLevelMap = new HashMap<>();
        try {
            material = Material.valueOf(section.getString("material"));
        } catch (Exception e) {
            material = Material.AIR;
        }
        this.material = material;
        this.customName = section.getString("name");
        ConfigurationSection enchantmentsSection = section.getConfigurationSection("enchantments");
        if (enchantmentsSection != null)
            for (String enchantmentKey : enchantmentsSection.getKeys(false)) {
                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentKey.toLowerCase()));
                if (enchantment != null) {
                    int level = enchantmentsSection.getInt(enchantmentKey);
                    this.enchantmentToLevelMap.put(enchantment, Integer.valueOf(level));
                }
            }
        this.amount = section.getInt("amount");
    }


    public ItemStack make() {
        int amount = this.amount;
        ItemStack itemStack = new ItemStack(this.material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return null;
        if (this.customName != null)
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.customName));
        if (!this.enchantmentToLevelMap.isEmpty())
            for (Map.Entry<Enchantment, Integer> enchantEntry : this.enchantmentToLevelMap.entrySet())
                itemMeta.addEnchant(enchantEntry.getKey(), ((Integer)enchantEntry.getValue()).intValue(), true);
        itemStack.setItemMeta(itemMeta);
        return itemStack;

    }
}
