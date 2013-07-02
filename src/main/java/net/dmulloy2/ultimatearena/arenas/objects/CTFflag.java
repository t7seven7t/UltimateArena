package net.dmulloy2.ultimatearena.arenas.objects;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.dmulloy2.ultimatearena.UltimateArena;
import net.dmulloy2.ultimatearena.arenas.Arena;
import net.dmulloy2.ultimatearena.util.Util;

public class CTFflag
{
	public Player riding;
	
	public String flagType = "";
	
	public Arena arena;
	
	public Location returnto;
	public Location myloc;
	public Location toloc;
	private Location lastloc;
	
	public int lastBlockType;
	public int team;
	public int timer = 15;
	
	public boolean pickedUp;
	public boolean stopped;
	
	public byte color;
	public byte lastBlockDat;
	
	public UltimateArena plugin;
	
	public CTFflag(Arena a, Location loc, int team)
	{
		this.team = team;
		this.arena = a;
		this.plugin = arena.az.plugin;
		
		this.returnto = loc.clone();
		this.myloc = loc.clone();
		this.lastloc = loc.clone();
		this.toloc = loc.clone();
		
		loc.getBlock().setTypeIdAndData(0, (byte)0, false);
		
		setup();
	}
	
	public void respawn() 
	{
		try
		{
			timer = 15;
			pickedUp = false;
			riding = null;
			toloc = returnto.clone();
			myloc = toloc.clone();
			setFlag();
		}
		catch(Exception e) 
		{
			plugin.getLogger().severe("Error respawning flag: " + e.getMessage());
		}
	}
	
	public void notifyTime() 
	{
		if (timer % 5 == 0 || timer < 10) 
		{
			sayTimeLeft();
		}
	}
	
	public void sayTimeLeft() 
	{
		arena.tellPlayers("&d{0} &7seconds left until &6{1} &7flag returns!", timer, flagType);
	}
	
	public void setup()
	{
		final Block current = myloc.getBlock();
		lastBlockDat = current.getData();
		lastBlockType = current.getTypeId();
		colorize();
	}
	
	public void colorize() 
	{
		Block current = myloc.getBlock();
		if (team == 1) 
		{
			color = 14; // red team
			flagType = ChatColor.RED + "RED";
		}
		else
		{
			color = 11; //blue team
			flagType = ChatColor.BLUE + "BLUE";
		}
		
		setFlagBlock(current);
	}
	
	public void fall() 
	{
		arena.tellPlayers("&b{0} &7has dropped the &6{1} &7flag!", riding.getName(), flagType);
		timer = 15;
		toloc = riding.getLocation();
		pickedUp = false;
		riding = null;
		
		myloc = toloc.clone();
	    		
		int count = 0;
		boolean can = true;
		for (int i = 1; i < 128; i++) 
		{
			if (can) 
			{
				Block BlockUnder = ((myloc.clone()).subtract(0,i,0)).getBlock(); 
				if (BlockUnder != null) 
				{
					if (BlockUnder.getType().equals(Material.AIR) || BlockUnder.getType().equals(Material.WATER))
					{
						count++;
					}
					else
					{
						can = false; 
					}
				}
			}
		}
	    		
		toloc = myloc.clone().subtract(0, count, 0);
		setFlag();
	}
	
	public void checkNear(List<ArenaPlayer> arenaplayers)
	{
		if (stopped)
			return;
		
		if (!pickedUp) 
		{
			for (int i = 0; i < arenaplayers.size(); i++)
			{
				Player pl = arenaplayers.get(i).player;
				if (pl != null)
				{
					if (Util.pointDistance(pl.getLocation(), myloc) < 1.75 && pl.getHealth() > 0)
					{
						if (!arenaplayers.get(i).out)
						{
							if (arenaplayers.get(i).team != team) 
							{
								//if the guy is on the other team
								pickedUp = true;
								riding = pl;
								pl.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * (60 * 4), 1));
								pl.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * (60 * 4), 1));
								arena.tellPlayers("&b{0} &7picked up the &6{1} &7flag!", arenaplayers.get(i).player.getName(), flagType);
								return;
							}
							else
							{
								if (!myloc.equals(returnto)) 
								{ 
									//if the flag is not at its flagstand
									pl.sendMessage(ChatColor.GRAY + "Flag Returned! " + ChatColor.RED + " +50 XP");
									arenaplayers.get(i).gameXP += 50;
									arena.tellPlayers("&b{0} &7returned the &6{1} &7flag!", pl.getName(), flagType);
									respawn();
									return;
								}
							}
						}
					}
				}
			}
		}
		if (pickedUp)
		{
			if (riding.isOnline() && !riding.isDead()) 
			{
				//if player is alive
				toloc = riding.getLocation().clone().add(0, 3, 0);
			}
			else
			{
				fall();
			}
			
			myloc = toloc.clone();
			setFlag();
		}
	}
	
	public void despawn() 
	{
		stopped = true;
		
		Block last = lastloc.getBlock();
		last.setTypeIdAndData(lastBlockType, lastBlockDat, false);
	}
	
	public void tick()
	{
		if (stopped)
			return;
		
		if (!pickedUp) 
		{
			if (!myloc.equals(returnto)) 
			{
				//if the flag is not at its flagstand
				timer--;
				if (timer <= 0) 
				{
					respawn();
					
					arena.tellPlayers("&7The {0} &7flag has respawned!", flagType);
				}
				else
				{
					notifyTime();
				}
			}
		}
	}
	
	public void setFlag() 
	{
		if (stopped)
			return;
		
		Block last = lastloc.getBlock();
		Block current = myloc.getBlock();

    	if (!locequals(lastloc, myloc))
    	{
	    	last.setTypeIdAndData(lastBlockType, lastBlockDat, true);
	    	lastBlockDat = current.getData();
			lastBlockType = current.getTypeId();
			lastloc = myloc.clone();
	    	//last.setTypeIdAndData(0, (byte) 0, true);
			setFlagBlock(current);
    	}
	}
	
	private void setFlagBlock(Block c) 
	{
		if (color == 11)
			c.setTypeIdAndData(Material.LAPIS_BLOCK.getId(), (byte)0, true);
		if (color == 14)
			c.setTypeIdAndData(Material.NETHERRACK.getId(), (byte)0, true);
	}

	public boolean locequals(Location loc, Location loc2) 
	{
		return (loc.getBlockX() == loc2.getBlockX() &&
			loc.getBlockY() == loc2.getBlockY() &&
			loc.getBlockZ() == loc2.getBlockZ() &&
			loc.getWorld().equals(loc2.getWorld()));	
	}
}