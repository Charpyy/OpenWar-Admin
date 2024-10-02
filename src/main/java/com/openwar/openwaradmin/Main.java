package com.openwar.openwaradmin;

import com.openwar.openwaradmin.Commands.AdminCmd;
import com.openwar.openwarfaction.factions.FactionManager;
import com.openwar.openwarlevels.level.PlayerDataManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    Economy economy;
    PlayerDataManager pl;
    FactionManager fm;

    private boolean setupDepend() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        RegisteredServiceProvider<PlayerDataManager> levelProvider = getServer().getServicesManager().getRegistration(PlayerDataManager.class);
        RegisteredServiceProvider<FactionManager> factionDataProvider = getServer().getServicesManager().getRegistration(FactionManager.class);
        if (rsp == null || levelProvider == null || factionDataProvider == null) {
            System.out.println("ERROR !!!!!!!!!!!!!!!!!!!!");
            return false;
        }
        economy = rsp.getProvider();
        pl = levelProvider.getProvider();
        fm = factionDataProvider.getProvider();
        return true;
    }
    @Override
    public void onEnable() {
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" OpenWar - Admin v1.0");
        System.out.println(" ");
        System.out.println(" ");
        setupDepend();
        this.getCommand("admin").setExecutor(new AdminCmd(pl, fm, economy));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
