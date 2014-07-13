package me.RegalMachine.Districts.Events.Custom;

import org.bukkit.entity.Player;

import me.RegalMachine.Districts.Events.Custom.Util.MovementWay;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RegionLeftEvent extends RegionEvent{
	public RegionLeftEvent(ProtectedRegion r, Player p, MovementWay m){
		super(r,p,m);
	}

}
