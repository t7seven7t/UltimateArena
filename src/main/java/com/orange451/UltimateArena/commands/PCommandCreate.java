package com.orange451.UltimateArena.commands;

import org.bukkit.ChatColor;

import com.orange451.UltimateArena.UltimateArena;
import com.orange451.UltimateArena.PermissionInterface.PermissionInterface;

public class PCommandCreate extends PBaseCommand {
	public PCommandCreate(UltimateArena plugin) {
		this.plugin = plugin;
		aliases.add("create");
		aliases.add("c");
		
		mode = "build";
		
		desc = ChatColor.DARK_RED + "<arena>" + ChatColor.YELLOW + " create an UltimateArena";
	}
	
	@Override
	public void perform() {
		if (PermissionInterface.checkPermission(player, plugin.uaBuilder) || PermissionInterface.checkPermission(player, plugin.uaAdmin)) {
			if (parameters.size() == 3) {
				String name = parameters.get(1);
				String type = parameters.get(2);
				plugin.createField(player, name, type);
			}else{
				if (parameters.size() == 1) {
					player.sendMessage(ChatColor.RED + "Missing name and type of arena");
				}else if (parameters.size() == 2) {
					player.sendMessage(ChatColor.RED + "Missing type of arena");
				}else{
					player.sendMessage(ChatColor.RED + "4 Parameters? really? try again.");
				}
				player.sendMessage(ChatColor.GOLD + "/ua create [name] [type]");
			}
		}
	}
}