package me.RegalMachine.Districts.Events.Custom;

import me.RegalMachine.Districts.Events.Custom.Util.MovementWay;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public abstract class RegionEvent extends PlayerEvent{
	
	private static final HandlerList handlerList = new HandlerList();
	
	private ProtectedRegion region;
	private MovementWay movement;
	
	public RegionEvent(ProtectedRegion region, Player player, MovementWay movement){
		super(player);
		this.region = region;
		this.movement = movement;
	}
	
	public HandlerList getHandlers(){
		return handlerList;
	}
	
	public ProtectedRegion getRegion(){
		return this.region;
	}
	
	public static HandlerList  getHandlerList(){
		return handlerList;
	}
	
	public MovementWay getMovementWay(){
		return this.movement;
	}

}
