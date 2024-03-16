package org.rankup.globalmissions;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.rankup.globalmissions.commands.StartCommand;
import org.rankup.globalmissions.events.*;
import org.rankup.globalmissions.expansions.MissionExpansion;
import org.rankup.globalmissions.files.DataManager;
import org.rankup.globalmissions.files.RewardItems;
import org.rankup.globalmissions.files.RewardsManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class GlobalMissions extends JavaPlugin {

    FileConfiguration config;
    File cFile;

    public DataManager data;
    public RewardsManager rewards;

    private String mission;
    private int goal;

    private static Economy eco;

    public final List<RewardItems> rewardItems = new ArrayList<>();


    @Override
    public void onEnable() {
        this.config = getConfig();
        this.config.options().copyDefaults(true);
        this.cFile = new File(getDataFolder(), "config.yml");
        saveDefaultConfig();

        this.rewards = new RewardsManager(this);
        this.data = new DataManager(this);
        getEvent();
        getCommand("missionstart").setExecutor(new StartCommand(this));
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
        }
        new MissionExpansion(this).register();
    }

    @Override
    public void onDisable() {


    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("missionreload")) {

            if (sender instanceof Player) {
                if (!sender.hasPermission("mission.reload"))
                    return false;
                sender.sendMessage("Configs reloading!");
            } else if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender) {
                System.out.println("Configs reloading!");
            }
            config = YamlConfiguration.loadConfiguration(this.cFile);
            reloadConfig();
            rewards.reloadConfig();
            data.reloadConfig();
        }
        return false;
    }

    public String getMission() {
        return mission;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        eco = rsp.getProvider();
        return eco != null;
    }


    public Economy getEco() {
        return eco;
    }

    public String getGoal() {
        if (getConfig().get("Break") != null || getConfig().get("Place") != null || getConfig().get("Breed") != null
        || getConfig().get("Kill") != null || getConfig().getBoolean("Move")) {
            this.goal = getConfig().getInt("Goal");
        }
        return String.valueOf(goal);
    }

    private void getEvent() {
        if (getConfig().get("Break") != null) {
            getServer().getPluginManager().registerEvents(new BreakEvent(this), this);
            this.mission = "romper";
        }
        if (getConfig().get("Place") != null) {
            getServer().getPluginManager().registerEvents(new PlaceEvent(this), this);
            this.mission = "colocar";
        }
        if (getConfig().get("Breed") != null) {
            getServer().getPluginManager().registerEvents(new BreedEvent(this), this);
            this.mission = "reproducir";
        }
        if (getConfig().get("Kill") != null) {
            getServer().getPluginManager().registerEvents(new KillEvent(this), this);
            this.mission = "matar";
        }
        if (getConfig().getBoolean("Move")) {
            getServer().getPluginManager().registerEvents(new MoveEvent(this), this);
            this.mission = "mover";
        }
    }

    public void giveRewards(Player player) {
        ConfigurationSection itemsSection = rewards.getConfig()
                .getConfigurationSection("Rewards");
        if (itemsSection == null) {
            System.out.println("Rewards is null, it wont work if you delete it!");
            rewards.getConfig().createSection("Rewards");
        }
        for (String key : itemsSection.getKeys(false)) {
            ConfigurationSection section = itemsSection.getConfigurationSection(key);
            this.rewardItems.add(new RewardItems(section));
        }

        for (int i = 0; i < rewardItems.size(); i++) {
            RewardItems reward = this.rewardItems.get(i);
            ItemStack item = reward.make();
            player.getInventory().addItem(item);
        }
        if (rewards.getConfig().getDouble("Money") > 0) {
            getEco().depositPlayer(player, rewards.getConfig().getDouble("Money"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    getConfig().getString("Money-message")).replace("{money}",
                    String.valueOf(rewards.getConfig().getDouble("Money"))));
        }
    }
}
