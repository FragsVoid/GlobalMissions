package org.rankup.globalmissions.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.rankup.globalmissions.GlobalMissions;

public class StartCommand implements CommandExecutor {

    private final GlobalMissions plugin;

    public StartCommand(GlobalMissions plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (!player.hasPermission("mission.start"))
                return false;
            Sound sound;
            try {
                sound = Sound.valueOf(plugin.getConfig().getString("Start-Sound"));
            } catch (Exception e) {
                sound = null;
            }

            for (Player players : Bukkit.getOnlinePlayers()) {
                if (plugin.getConfig().getBoolean("Chat-message")) {
                    players.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("Mission-start")).replace("{mission}",
                            plugin.getMission()).replace("{goal}",
                            plugin.getGoal()));
                }

                if (plugin.getConfig().getBoolean("Title-message")) {
                    players.sendTitle(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("Mission-start-title")).replace("{mission}",
                                    plugin.getMission()).replace("{goal}",
                                    plugin.getGoal())
                            ,ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("Mission-start-subtitle")).replace("{mission}",
                                    plugin.getMission()).replace("{goal}",
                                    plugin.getGoal()),1,
                    plugin.getConfig().getInt("Title-time"), 1);
                }

                if (plugin.getConfig().getBoolean("SendSound")) {
                    players.playSound(players.getLocation(),
                            sound, 3.0F,0.5F);
                }
                plugin.data.resetFile();
                plugin.getConfig().set("Enabled", true);
                plugin.saveConfig();
            }
        }



        return false;
    }
}
