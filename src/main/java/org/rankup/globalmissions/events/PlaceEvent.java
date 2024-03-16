package org.rankup.globalmissions.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.rankup.globalmissions.GlobalMissions;

public class PlaceEvent implements Listener {

    private final GlobalMissions plugin;

    public PlaceEvent(GlobalMissions plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Material mat = e.getBlock().getType();
        Material material;
        for (String materials : plugin.getConfig().getStringList("Place.material")) {
            try {
                material = Material.valueOf(materials);
            } catch (Exception ex) {
                material = Material.AIR;
            }
            if (mat == material &&
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
                                plugin.getConfig().getString("Mission-finish")).replace("{mission}",
                                plugin.getMission()).replace("{goal}",
                                plugin.getGoal()));
                    }

                    if (plugin.getConfig().getBoolean("Title-message")) {
                        players.sendTitle(ChatColor.translateAlternateColorCodes('&',
                                        plugin.getConfig().getString("Mission-finish-title")).replace("{mission}",
                                        plugin.getMission()).replace("{goal}",
                                        plugin.getGoal())
                                ,ChatColor.translateAlternateColorCodes('&',
                                        plugin.getConfig().getString("Mission-finish-subtitle")).replace("{mission}",
                                        plugin.getMission()).replace("{goal}",
                                        plugin.getGoal()),1,
                                plugin.getConfig().getInt("Title-time"), 1);
                    }

                    if (plugin.getConfig().getBoolean("SendSound")) {
                        players.playSound(players.getLocation(),
                                sound, 3.0F,0.5F);
                    }
                    plugin.giveRewards(player);
                }
            }
        }
    }
}
