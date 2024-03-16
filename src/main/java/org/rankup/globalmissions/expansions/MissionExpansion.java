package org.rankup.globalmissions.expansions;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.rankup.globalmissions.GlobalMissions;

public class MissionExpansion extends PlaceholderExpansion {

    private final GlobalMissions plugin;

    public MissionExpansion(GlobalMissions plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "mission";
    }

    @Override
    public @NotNull String getAuthor() {
        return "frags";
    }

    @Override
    public @NotNull String getVersion() {
        return null;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {

        if (player == null) {
            return "";
        }
        if (params.equals("mission")) {
            return plugin.getMission();
        } else if (params.equals("progress")) {
            return String.valueOf(plugin.data.getConfig().getInt("DataUntilGoal"));
        } else if (params.equals("goal")) {
            return plugin.getGoal();
        } else if (params.equals("money")) {
            return String.valueOf(plugin.rewards.getConfig().getDouble("Money"));
        }

        return null;
    }
}


