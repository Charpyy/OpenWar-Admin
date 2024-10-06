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

    public AdminCmd(PlayerDataManager pl, FactionManager fm, Economy eco) {
        this.eco = eco;
        this.fm = fm;
        this.pl = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(admin + "§cOnly players can execute this command.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("openwar.op")) {
            player.sendMessage(admin + " §cYou don't have the required permission to do that.");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(admin + "§cUsage: /admin|ad fac <xp|lvl|disband|join|promote|kick|invite|name> ...");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "fac":
                //====== FACTION ADMIN COMMANDS========================
                if (args.length < 3) {
                    player.sendMessage(admin + "§cUsage: /admin fac <xp|lvl|disband|join|promote|kick|invite|name> ...");
                    return true;
                }

                switch (args[1].toLowerCase()) {
                    case "xp":
                        if (args.length >= 3) {
                            try {
                                int exp = Integer.parseInt(args[2]);
                                Faction faction = fm.getFactionByPlayer(player.getUniqueId());
                                if (faction != null) {
                                    faction.addExp(exp);
                                    player.sendMessage(admin + "§6Experience set to §e" + exp + " §6for faction §e" + faction.getName());
                                }
                            } catch (NumberFormatException e) {
                                player.sendMessage(admin + "§cInvalid experience value.");
                            }
                        }
                        break;
                    case "lvl":
                        if (args.length >= 3) {
                            try {
                                int lvl = Integer.parseInt(args[2]);
                                Faction faction = fm.getFactionByPlayer(player.getUniqueId());
                                if (faction != null) {
                                    faction.setLevel(lvl);
                                    player.sendMessage(admin + "§6Level set to §e" + lvl + " §6for faction §e" + faction.getName());
                                }
                            } catch (NumberFormatException e) {
                                player.sendMessage(admin + "§cInvalid level value.");
                            }
                        }
                        break;
                    case "disband":
                        if (args.length >= 3) {
                            String factionName = args[2];
                            Faction faction = fm.getFactionByName(factionName);
                            if (faction != null) {
                                fm.deleteFaction(faction);
                                Bukkit.broadcastMessage(admin + "§cDecided to disband the faction " + faction.getName());
                            }
                        }
                        break;
                    case "join":
                        if (args.length >= 4) {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[2]);
                            UUID uuid = offlinePlayer.getUniqueId();
                            Faction faction = fm.getFactionByName(args[3]);
                            if (faction != null) {
                                fm.addMemberToFaction(uuid, faction.getFactionUUID(), Rank.RECRUE);
                                player.sendMessage(admin + " " + offlinePlayer.getName() + " joined the faction: " + faction.getName());
                            }
                        }
                        break;
                    case "promote":
                        if (args.length >= 4) {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[2]);
                            UUID uuid = offlinePlayer.getUniqueId();
                            Faction faction = fm.getFactionByName(args[3]);
                            if (faction != null) {
                                fm.promoteMember(uuid, faction);
                                player.sendMessage(admin + " promoted " + offlinePlayer.getName() + " in faction: " + faction.getName() + " to rank: " + faction.getRank(uuid));
                            }
                        }
                        break;
                    case "kick":
                        if (args.length >= 4) {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[2]);
                            UUID uuid = offlinePlayer.getUniqueId();
                            Faction faction = fm.getFactionByName(args[3]);
                            if (faction != null) {
                                fm.removePlayerFromFaction(uuid);
                                player.sendMessage(admin + " kicked " + offlinePlayer.getName() + " from faction: " + faction.getName());
                            }
                        }
                        break;
                    case "invite":
                        if (args.length >= 4) {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[2]);
                            UUID uuid = offlinePlayer.getUniqueId();
                            Faction faction = fm.getFactionByName(args[3]);
                            if (faction != null) {
                                fm.invitePlayerToFaction(uuid, faction.getFactionUUID());
                                player.sendMessage(admin + " invited " + offlinePlayer.getName() + " to faction: " + faction.getName());
                            }
                        }
                        break;
                    case "name":
                        if (args.length >= 4) {
                            Faction faction = fm.getFactionByName(args[2]);
                            if (faction != null) {
                                fm.setName(faction, args[3]);
                                player.sendMessage(admin + " Name of " + faction.getName() + " is now " + args[3]);
                            }
                        }
                        break;
                }
                break;

            case "lvl":
                //====== LEVELS ADMIN COMMANDS========================
                if (args.length < 4) {
                    player.sendMessage(admin + "§cUsage: /admin lvl <lvl|xp> <player> <value>");
                    return true;
                }

                switch (args[1].toLowerCase()) {
                    case "lvl":
                        Player target = Bukkit.getPlayer(args[2]);
                        if (target != null) {
                            try {
                                int level = Integer.parseInt(args[3]);
                                PlayerLevel playerLevel = pl.loadPlayerData(target.getUniqueId(), null);
                                playerLevel.setLevel(level);
                                sender.sendMessage(admin + "Level of §c" + target.getName() + " §7set to §c" + playerLevel.getLevel());
                            } catch (NumberFormatException e) {
                                sender.sendMessage(admin + "§cInvalid level value.");
                            }
                        } else {
                            sender.sendMessage(admin + "§cPlayer not found.");
                        }
                        break;
                    case "xp":
                        target = Bukkit.getPlayer(args[2]);
                        if (target != null) {
                            try {
                                double xp = Integer.parseInt(args[3]);
                                PlayerLevel playerLevel = pl.loadPlayerData(target.getUniqueId(), null);
                                playerLevel.setExperience(xp, target);
                                sender.sendMessage(admin + "Experience of §c" + target.getName() + " §7set to §c" + playerLevel.getExperience());
                            } catch (NumberFormatException e) {
                                sender.sendMessage(admin + "§cInvalid experience value.");
                            }
                        } else {
                            sender.sendMessage(admin + "§cPlayer not found.");
                        }
                        break;
                }
                break;

            default:
                player.sendMessage(admin + "§cUnknown command. Usage: /admin|ad <fac|lvl> ...");
                break;
        }
        return true;
    }
}
