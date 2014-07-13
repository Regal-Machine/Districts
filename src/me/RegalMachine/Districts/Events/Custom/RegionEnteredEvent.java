package me.RegalMachine.Districts.Events.Custom;

import me.RegalMachine.Districts.Events.Custom.Util.MovementWay;

import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RegionEnteredEvent extends RegionEvent{
	public RegionEnteredEvent(ProtectedRegion region, Player player,MovementWay movement){
		super(region,player,movement);
	}

}
