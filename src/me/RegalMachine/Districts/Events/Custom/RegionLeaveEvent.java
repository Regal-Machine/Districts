package me.RegalMachine.Districts.Events.Custom;

import me.RegalMachine.Districts.Events.Custom.Util.MovementWay;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RegionLeaveEvent extends RegionEvent implements Cancellable{
	
	private boolean cancelled;
	private boolean cancellable;
	
	public RegionLeaveEvent(ProtectedRegion region, Player player, MovementWay movement){
		super(region,player,movement);
		this.cancelled = false;
		this.cancellable = true;
		
		if((movement == MovementWay.SPAWN) || (movement == MovementWay.DISCONNECT)){
			this.cancellable = false;
		}
		
	}
	
	public void setCancelled(boolean cancelled){
		if(!this.cancellable){
			return;
		}
		this.cancelled = cancelled;
	}
	
	public boolean isCancelled(){
		return this.cancelled;
	}
	
	public boolean isCancellable(){
		return this.cancellable;
	}
	
	protected void setCancellable(boolean cancellable){
		this.cancellable = cancellable;
		
		if(!this.cancellable){
			this.cancelled = false;
		}
	}
	
	
}
