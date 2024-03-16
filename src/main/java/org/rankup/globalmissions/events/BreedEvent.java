package org.rankup.globalmissions.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.rankup.globalmissions.GlobalMissions;

import java.util.Objects;

public class BreedEvent implements Listener {

    private final GlobalMissions plugin;

    public BreedEvent(GlobalMissions plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    private void onBreed(EntityBreedEvent e) {

        if (!(e.getBreeder() instanceof Player))
            return;

        Player player = ((Player) e.getBreeder()).getPlayer();
        EntityType ent = e.getEntityType();
        EntityType entity;
        for (String animals : plugin.getConfig().getStringList("Breed.animals")) {
            try {
                entity = EntityType.valueOf(animals);
            } catch (Exception ex) {
                entity = null;
            }
            if (ent == entity &&
                    plugin.getConfig().getBoolean("Enabled") && plugin.data.getConfig().
                    getInt("DataUntilGoal") < plugin.getConfig().getInt("Goal")) {
                plugin.data.getConfig().set("DataUntilGoal", plugin.data.getConfig().
                        getInt("DataUntilGoal") + 1);
                plugin.data.getConfig().set(player.getDisplayName(),
                        plugin.data.getConfig().getInt(player.getDisplayName()) + 1);
                plugin.data.saveConfig();
            }

            if (plugin.data.getConfig().getInt("DataUntilGoal") >=
                    Integer.parseInt(plugin.getGoal())) {
                plugin.getConfig().set("Enabled", false);
                plugin.saveConfig();
                Sound sound;
                try {
                    sound = Sound.valueOf(plugin.getConfig().getString("Finish-Sound"));
                } catch (Exception eg) {
                    sound = null;
                }

                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (plugin.getConfig().getBoolean("Chat-message")) {
                        players.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                Objects.requireNonNull(plugin.getConfig().getString("Mission-finish"))).replace("{mission}",
                                plugin.getMission()).replace("{goal}",
                                plugin.getGoal()));
                    }

                    if (plugin.getConfig().getBoolean("Title-message")) {
                        players.sendTitle(ChatColor.translateAlternateColorCodes('&',
                                        Objects.requireNonNull(plugin.getConfig().getString("Mission-finish-title"))).replace("{mission}",
                                        plugin.getMission()).replace("{goal}",
                                        plugin.getGoal())
                                ,ChatColor.translateAlternateColorCodes('&',
                                        Objects.requireNonNull(plugin.getConfig().getString("Mission-finish-subtitle"))).replace("{mission}",
                                        plugin.getMission()).replace("{goal}",
                                        plugin.getGoal()),1,
                                plugin.getConfig().getInt("Title-time"), 1);
                    }

                    if (plugin.getConfig().getBoolean("SendSound")) {
                        assert sound != null;
                        players.playSound(players.getLocation(),
                                sound, 3.0F,0.5F);
                    }
                    plugin.giveRewards(player);
                }
            }
        }
    }
}
