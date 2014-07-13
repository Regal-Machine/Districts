package me.RegalMachine.Districts.Events.Custom.Util;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import me.RegalMachine.Districts.Main;
import me.RegalMachine.Districts.Events.Custom.RegionEnterEvent;
import me.RegalMachine.Districts.Events.Custom.RegionEnteredEvent;
import me.RegalMachine.Districts.Events.Custom.RegionLeaveEvent;
import me.RegalMachine.Districts.Events.Custom.RegionLeftEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WGRegionEventUtil implements Listener{
	
	private Map<Player, Set<ProtectedRegion>> playerRegions;
	
	public WGRegionEventUtil(){
		playerRegions = new HashMap<Player, Set<ProtectedRegion>>();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@EventHandler
	public void onPlayerKick(PlayerKickEvent e){
		Set<ProtectedRegion> regions = (Set)this.playerRegions.remove(e.getPlayer());
		if(regions != null){
			for(ProtectedRegion region: regions){
				RegionLeaveEvent leaveEvent = new RegionLeaveEvent(region, e.getPlayer(), MovementWay.DISCONNECT);
				RegionLeftEvent leftEvent = new RegionLeftEvent(region, e.getPlayer(), MovementWay.DISCONNECT);
				Main.instance.getServer().getPluginManager().callEvent(leaveEvent);
				Main.instance.getServer().getPluginManager().callEvent(leftEvent);
			}
		}
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		
		Set<ProtectedRegion> regions = (Set)this.playerRegions.remove(e.getPlayer());
		if (regions != null){
			for (ProtectedRegion region : regions){
				RegionLeaveEvent leaveEvent = new RegionLeaveEvent(region, e.getPlayer(), MovementWay.DISCONNECT);
				RegionLeftEvent leftEvent = new RegionLeftEvent(region, e.getPlayer(), MovementWay.DISCONNECT);
				
				Main.instance.getServer().getPluginManager().callEvent(leaveEvent);
				Main.instance.getServer().getPluginManager().callEvent(leftEvent);
			}
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		
		e.setCancelled(updateRegions(e.getPlayer(), MovementWay.MOVE, e.getTo()));
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e){
		
		e.setCancelled(updateRegions(e.getPlayer(), MovementWay.TELEPORT, e.getTo()));
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent ee){
		
		updateRegions(ee.getPlayer(), MovementWay.SPAWN, ee.getPlayer().getLocation());
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent ee){
		
		updateRegions(ee.getPlayer(), MovementWay.SPAWN, ee.getRespawnLocation());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private synchronized boolean updateRegions(final Player player, final MovementWay movement, Location to){
		Set<ProtectedRegion> regions;
		Set<ProtectedRegion> oldRegions;
		
		if(this.playerRegions.get(player) == null){
			regions = new HashSet();
		}else{
			regions = new HashSet((Collection)this.playerRegions.get(player));
		}
		
		oldRegions = new HashSet(regions);
		
		RegionManager manager = Main.guard.worldGuard().getRegionManager(to.getWorld());
		
		if(manager == null){
			return false;
		}
		
		ApplicableRegionSet appRegions = manager.getApplicableRegions(to);
		
		for(final ProtectedRegion region: appRegions){
			if(!regions.contains(region)){
				RegionEnterEvent e = new RegionEnterEvent(region, player, movement);
				Main.instance.getServer().getPluginManager().callEvent(e);
				
				
				if(e.isCancelled()){
					regions.clear();
					regions.addAll(oldRegions);
					return true;
				}
				
				new Thread(){
					public void run(){
						try{
							sleep(50L);
						}catch(InterruptedException e){}
						
						RegionEnteredEvent e = new RegionEnteredEvent(region, player, movement);
						Main.instance.getServer().getPluginManager().callEvent(e);
						
						
					}
				}.start();
				
			}
		}
		
		Collection<ProtectedRegion> app = (Collection)getPrivateValue(appRegions, "applicable");
		Iterator<ProtectedRegion> itr = regions.iterator();
		while(itr.hasNext()){
			final ProtectedRegion region = (ProtectedRegion)itr.next();
			if(!app.contains(region)){
				if(manager.getRegion(region.getId()) != region){
					itr.remove();
				}else{
					RegionLeaveEvent e = new RegionLeaveEvent(region, player, movement);
					
					Main.instance.getServer().getPluginManager().callEvent(e);
					Bukkit.getLogger().info("RegionLeaveEvent Thrown");
					
					if(e.isCancelled()){
						regions.clear();
						regions.addAll(oldRegions);
						return true;
					}
					
					new Thread(){
						public void run(){
							try{
								sleep(50L);
							}catch(InterruptedException e){}
							
							RegionLeftEvent e = new RegionLeftEvent(region, player, movement);
							Main.instance.getServer().getPluginManager().callEvent(e);
							Bukkit.getLogger().info("RegionLeftEvent Thrown");
							
						}
					}.start();
					itr.remove();
					
					
				}
			}
		}
		this.playerRegions.put(player, regions);
		return false;
	}
	
	private Object getPrivateValue(Object obj, String name){
		try{
			Field f = obj.getClass().getDeclaredField(name);
			f.setAccessible(true);
			return f.get(obj);
		}catch(Exception e){
			return null;
		}
	}
}
