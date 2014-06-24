package net.dmulloy2.ultimatearena.commands;

import net.dmulloy2.ultimatearena.UltimateArena;
import net.dmulloy2.ultimatearena.types.LeaveReason;

/**
 * @author dmulloy2
 */

public class CmdLeave extends UltimateArenaCommand
{
	public CmdLeave(UltimateArena plugin)
	{
		super(plugin);
		this.name = "leave";
		this.aliases.add("l");
		this.description = "leave an arena";
		this.mustBePlayer = true;
	}

	@Override
	public void perform()
	{
		plugin.getArenaPlayer(player).leaveArena(LeaveReason.COMMAND);
	}
}