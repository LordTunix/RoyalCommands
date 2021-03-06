package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.List;

public class CmdWorld implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdWorld(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("world")) {
            if (!plugin.isAuthorized(cs, "rcmds.world")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                List<World> ws = plugin.getServer().getWorlds();
                String worlds = "";
                for (World w : ws)
                    worlds = (worlds.equals("")) ? worlds.concat(ChatColor.GRAY + RUtils.getMVWorldName(w)) : worlds.concat(ChatColor.WHITE + ", " + ChatColor.GRAY + RUtils.getMVWorldName(w));
                cs.sendMessage(ChatColor.BLUE + "Worlds: " + worlds);
                return true;
            }
            World w = plugin.getServer().getWorld(args[0].trim());
            if (w == null) {
                cs.sendMessage(ChatColor.RED + "That world does not exist!");
                List<World> ws = plugin.getServer().getWorlds();
                String worlds = "";
                for (World w2 : ws) {
                    if (worlds.equals(""))
                        worlds = worlds.concat(ChatColor.GRAY + RUtils.getMVWorldName(w2));
                    else
                        worlds = worlds.concat(ChatColor.WHITE + ", " + ChatColor.GRAY + RUtils.getMVWorldName(w2));
                }
                cs.sendMessage(ChatColor.BLUE + "Worlds: " + worlds);
                return true;
            }
            Player p = (Player) cs;
            p.sendMessage(ChatColor.BLUE + "Teleporting you to world " + ChatColor.GRAY + RUtils.getMVWorldName(w) + ChatColor.BLUE + ".");
            String error = RUtils.teleport(p, CmdSpawn.getWorldSpawn(w));
            if (!error.isEmpty()) {
                p.sendMessage(ChatColor.RED + error);
                return true;
            }
            return true;
        }
        return false;
    }

}
