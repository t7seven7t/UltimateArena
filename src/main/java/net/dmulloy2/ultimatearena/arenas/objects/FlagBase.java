package net.dmulloy2.ultimatearena.arenas.objects;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import net.dmulloy2.ultimatearena.UltimateArena;
import net.dmulloy2.ultimatearena.arenas.Arena;

public class FlagBase
{
	public Location loc;
	public Block notify = null;
	public Arena arena;
	public UltimateArena plugin;
	
	public FlagBase(Arena arena, Location loc) 
	{
		this.arena = arena;
		Location safe = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
		this.setLoc(safe.clone().subtract(0, 1, 0));
		this.plugin = arena.az.plugin;
		
		setup();
	}
	
	public void setup() 
	{
		Location flag = getLoc().clone().add(0, 5, 0);
		notify = flag.getBlock();
		notify.setType(Material.WOOL);
		((getLoc().clone()).add(1, 0, 0)).getBlock().setType(Material.STONE);
		((getLoc().clone()).add(1, 0, 1)).getBlock().setType(Material.STONE);
		((getLoc().clone()).add(-1, 0, -1)).getBlock().setType(Material.STONE);
		((getLoc().clone()).add(1, 0, -1)).getBlock().setType(Material.STONE);
		((getLoc().clone()).add(-1, 0, 1)).getBlock().setType(Material.STONE);
		((getLoc().clone()).add(2, 0, 0)).getBlock().setType(Material.STONE);
		((getLoc().clone()).add(-1, 0, 0)).getBlock().setType(Material.STONE);
		((getLoc().clone()).add(-2, 0, 0)).getBlock().setType(Material.STONE);
		((getLoc().clone()).add(0, 0, 1)).getBlock().setType(Material.STONE);
		((getLoc().clone()).add(0, 0, 2)).getBlock().setType(Material.STONE);
		((getLoc().clone()).add(0, 0, -1)).getBlock().setType(Material.STONE);
		((getLoc().clone()).add(0, 0, -2)).getBlock().setType(Material.STONE);
	}
	
	public synchronized void checkNear(List<ArenaPlayer> arenaplayers) {}

	public Location getLoc()
	{
		return loc;
	}

	public void setLoc(Location loc) 
	{
		this.loc = loc;
	}
}