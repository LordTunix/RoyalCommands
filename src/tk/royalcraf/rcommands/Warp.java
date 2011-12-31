package tk.royalcraf.rcommands;

import java.io.File;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.RoyalCommands;

public class Warp implements CommandExecutor {

	RoyalCommands plugin;

	public Warp(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("warp")) {
			if (!plugin.isAuthorized(cs, "rcmds.warp")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				plugin.log.warning("[RoyalCommands] " + cs.getName()
						+ " was denied access to the command!");
				return true;
			}

			Player p = null;

			if (!(cs instanceof Player)) {
				cs.sendMessage(ChatColor.RED
						+ "This command is only available to players!");
				return true;
			} else {
				p = (Player) cs;
			}

			boolean warpSet = false;
			Double warpX = null;
			Double warpY = null;
			Double warpZ = null;
			Float warpYaw = null;
			Float warpPitch = null;
			World warpW = null;

			File pconfl = new File(plugin.getDataFolder() + "/warps.yml");
			if (pconfl.exists()) {
				FileConfiguration pconf = YamlConfiguration
						.loadConfiguration(pconfl);
				if (args.length < 1) {
					if (pconf.get("warps") == null) {
						cs.sendMessage(ChatColor.RED + "There are no warps!");
						return true;
					}
					final Map<String, Object> opts = pconf
							.getConfigurationSection("warps").getValues(false);
					if (opts.keySet().isEmpty()) {
						cs.sendMessage(ChatColor.RED + "There are no warps!");
						return true;
					}
					String warps = opts.keySet().toString();
					warps = warps.substring(1, warps.length() - 1);
					cs.sendMessage(ChatColor.BLUE + "Warps:");
					cs.sendMessage(warps);
					return true;
				}
				warpSet = pconf.getBoolean("warps." + args[0] + ".set");
				if (warpSet) {
					warpX = pconf.getDouble("warps." + args[0] + ".x");
					warpY = pconf.getDouble("warps." + args[0] + ".y");
					warpZ = pconf.getDouble("warps." + args[0] + ".z");
					warpYaw = Float.parseFloat(pconf.getString("warps."
							+ args[0] + ".yaw"));
					warpPitch = Float.parseFloat(pconf.getString("warps."
							+ args[0] + ".pitch"));
					warpW = plugin.getServer().getWorld(
							pconf.getString("warps." + args[0] + ".w"));
				} else {
					cs.sendMessage(ChatColor.RED + "That warp does not exist.");
					return true;
				}
			} else {
				cs.sendMessage(ChatColor.RED + "");
				return true;
			}
			Location warpLoc = new Location(warpW, warpX, warpY, warpZ,
					warpYaw, warpPitch);
			p.sendMessage(ChatColor.BLUE + "Going to warp \"" + ChatColor.GRAY
					+ args[0] + ChatColor.BLUE + ".\"");
			p.teleport(warpLoc);
			return true;

		}
		return false;
	}
}