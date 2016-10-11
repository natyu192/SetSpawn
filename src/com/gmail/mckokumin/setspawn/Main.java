package com.gmail.mckokumin.setspawn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements CommandExecutor {

	public void onEnable() {
		this.getCommand("setspawn").setExecutor(this);
		this.getCommand("spawn").setExecutor(this);
		this.getCommand("worldspawn").setExecutor(this);
		saveDefaultConfig();
		saveConfig();
	}

	public void onDisable() {

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		switch (cmd.getName()) {
		case "setspawn":
			if (sender instanceof Player) {
				Player p = (Player) sender;
				Location l = p.getLocation();
				this.setSpawn(l.getBlockX() + 0.5, l.getBlockY(),
						l.getBlockZ() + 0.5, l.getPitch(), l.getYaw(), l
								.getWorld().getName());
				sender.sendMessage(ChatColor.GREEN + "スポーンを設定しました");
			}
			break;
		case "worldspawn":
			if (sender instanceof Player) {
				Player p = (Player) sender;
				p.teleport(p.getWorld().getSpawnLocation());
				sender.sendMessage(ChatColor.YELLOW + "ワールドのスポーンにテレポートしました");
			}
			break;
		case "spawn":
			try {
				if (sender instanceof Player) {
					Player p = (Player) sender;
					if (args.length <= 1) {
						if (!p.hasPermission("natyu.spawn.other")) {
							p.teleport(this.getSpawn());
							return true;
						}
						Player t = Bukkit.getPlayer(args[0]);
						if (t == null) {
							sender.sendMessage(ChatColor.RED + "プレイヤーが見つかりません");
							return true;
						}
						t.teleport(this.getSpawn());
						sender.sendMessage(ChatColor.YELLOW + t.getName()
								+ "をスポーンにテレポートさせました");
						t.sendMessage(ChatColor.YELLOW + sender.getName()
								+ "によってスポーンにテレポートされました");
					} else {
						p.teleport(this.getSpawn());
						sender.sendMessage(ChatColor.YELLOW + "スポーンにテレポートしました");
					}
				}
			} catch (NullPointerException e) {
				sender.sendMessage(ChatColor.RED + "スポーンが設定されていません");
			}
			break;
		default:
			break;
		}

		return true;
	}

	public void setSpawn(double x, double y, double z, float pitch, float yaw,
			String world) {
		Configuration c = this.getConfig();
		c.set("spawn.x", x);
		c.set("spawn.y", y);
		c.set("spawn.z", z);
		c.set("spawn.pitch", pitch);
		c.set("spawn.yaw", yaw);
		c.set("spawn.world", world);
		saveConfig();
	}

	public Location getSpawn() {
		Configuration c = this.getConfig();
		double x = c.getDouble("spawn.x");
		double y = c.getDouble("spawn.y");
		double z = c.getDouble("spawn.z");
		float pitch = (float) c.getDouble("spawn.pitch");
		float yaw = (float) c.getDouble("spawn.yaw");
		World world = Bukkit.getWorld(c.getString("spawn.world"));
		Location loc = new Location(world, x, y, z, yaw, pitch);
		return loc;
	}

}
