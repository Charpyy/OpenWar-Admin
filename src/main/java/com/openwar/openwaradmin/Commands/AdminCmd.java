package com.openwar.openwaradmin.Commands;

import com.openwar.openwarfaction.factions.Faction;
import com.openwar.openwarfaction.factions.FactionManager;
import com.openwar.openwarfaction.factions.Rank;
import com.openwar.openwarlevels.level.PlayerDataManager;
import com.openwar.openwarlevels.level.PlayerLevel;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AdminCmd implements CommandExecutor {

    PlayerDataManager pl;
    FactionManager fm;
    Economy eco;
    private String admin = "§8» §cAdmin §8« §f";

    public  AdminCmd (PlayerDataManager pl, FactionManager fm, Economy eco) {
        this.eco = eco;
        this.fm = fm;
        this.pl = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission("openwar.op")) {
            player.sendMessage(admin+" §cYou don't have the required permission to do that.");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "fac":
                //====== FACTION ADMIN COMMANDS========================
                switch (args[1].toLowerCase()) {
                    case "xp":
                        if (args[2] != null) {
                            int exp = Integer.parseInt(args[2]);
                            Faction faction = fm.getFactionByPlayer(player.getUniqueId());
                            if (faction != null) {
                                faction.addExp(exp);
                                player.sendMessage(admin + "§6Experience set to §e" + exp + " §6to faction §e" + faction.getName());
                            }
                        }
                        break;
                    case "lvl":
                        if (args[2] != null) {
                            int lvl = Integer.parseInt(args[2]);
                            Faction faction = fm.getFactionByPlayer(player.getUniqueId());
                            if (faction != null) {
                                faction.setLevel(lvl);
                                player.sendMessage(admin + "§6Level set to §e" + lvl + " §6to faction §e" + faction.getName());
                            }
                        }
                        break;
                    case "disband":
                        if (args[2] != null) {
                            String factionName = args[2];
                            Faction faction = fm.getFactionByName(factionName);
                            if (faction != null) {
                                fm.deleteFaction(faction);
                                Bukkit.broadcast(admin + "§cDecided to disband the faction " + faction.getName(), null);
                            }
                        }
                        break;
                    case "join":
                        if (args[2] != null) {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[2]);
                            UUID uuid = offlinePlayer.getUniqueId();
                            if (args[3] != null) {
                                Faction faction = fm.getFactionByName(args[3]);
                                if (faction != null) {
                                    fm.addMemberToFaction(uuid, faction.getFactionUUID(), Rank.RECRUE);
                                }
                            }
                        }
                        break;
                    case "promote":
                        if (args[2] != null) {
                            if (args[3] != null) {
                                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[2]);
                                UUID uuid = offlinePlayer.getUniqueId();
                                Faction faction = fm.getFactionByName(args[3]);
                                if (faction != null) {
                                    fm.promoteMember(uuid, faction);
                                }
                            }
                        }
                    }
                break;

            case "lvl":
                //====== LEVELS ADMIN COMMANDS========================
                switch (args[1].toLowerCase()) {
                    case "lvl": {
                        Player target = Bukkit.getPlayer(args[2]);
                        int level = Integer.parseInt(args[3]);
                        PlayerLevel playerLevel = pl.loadPlayerData(target.getUniqueId(), null);
                        playerLevel.setLevel(level);
                        sender.sendMessage(admin+"level of §c"+target.getName() +" §7set to §c"+playerLevel.getLevel());
                    }
                        break;
                    case "xp": {
                        Player target = Bukkit.getPlayer(args[2]);
                        double xp = Integer.parseInt(args[3]);
                        PlayerLevel playerLevel = pl.loadPlayerData(target.getUniqueId(), null);
                        playerLevel.setExperience(xp, target);
                        sender.sendMessage(admin + "Experience of §c" + target.getName() + " §7set to §c" + playerLevel.getExperience());
                    }
                        break;
                }
                break;
        }
        return true;
    }
}

// usage = /admin fac disband name