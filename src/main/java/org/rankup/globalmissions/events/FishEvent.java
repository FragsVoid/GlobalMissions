package org.rankup.globalmissions.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.rankup.globalmissions.GlobalMissions;

import java.util.Objects;

public class FishEvent implements Listener {


    private final GlobalMissions plugin;

    public FishEvent(GlobalMissions plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if (e.getCaught() == null)
            return;
        if (!(e.getState() == PlayerFishEvent.State.CAUGHT_FISH))
            return;
        Player player = e.getPlayer();
    }
        /*
        Item fish = (Item) e.getCaught();
        ItemStack itemStack = fish.getItemStack();
        String fishString = itemStack.getType().toString();



        String cFish;
        for (String fishes : plugin.getConfig().getStringList("Fish.fishes")) {
            try {
                cFish = fishes;
            } catch (Exception ex) {
                cFish = null;
            }
            System.out.println(1);
            System.out.println(cFish);
            System.out.println(fishString);

            if (!(ent == entity)) {
                System.out.println(ent);
                System.out.println(entity);
            }
            if (ent == entity)
                System.out.println(1);
            if (ent == entity &&
                    plugin.getConfig().getBoolean("Enabled") && plugin.data.getConfig().
                    getInt("DataUntilGoal") < plugin.getConfig().getInt("Goal")) {
                plugin.data.getConfig().set("DataUntilGoal", plugin.data.getConfig().
                        getInt("DataUntilGoal") + 1);
                plugin.data.getConfig().set(player.getDisplayName(),
                        plugin.data.getConfig().getInt(player.getDisplayName()) + 1);
                plugin.data.saveConfig();
            }

             */
}
