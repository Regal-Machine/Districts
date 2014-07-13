package me.RegalMachine.Districts.Events;

import java.util.UUID;

import me.RegalMachine.Districts.Events.Custom.RegionEnterEvent;
import me.RegalMachine.Districts.Events.Custom.RegionLeaveEvent;
import me.RegalMachine.Districts.Protection.DistrictBag;
import me.RegalMachine.Districts.util.Lang;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class AnnounceOwnersDistrict implements Listener{
	
	@EventHandler
	public void onDistrictEnter(RegionEnterEvent e){
		ProtectedRegion p = e.getRegion();
		if(DistrictBag.districts.containsKey(UUID.fromString(p.getId()))){
			e.getPlayer().sendMessage(Lang.DISTRICTS + "Entered " + DistrictBag.districts.get(UUID.fromString(p.getId())).getOwner().getPlayer().getName() + "'s district.");
		}
	}
	
	@EventHandler
	public void onDistrictEnter(com.mewin.WGRegionEvents.events.RegionEnteredEvent e){
		ProtectedRegion p = e.getRegion();
		if(DistrictBag.districts.containsKey(UUID.fromString(p.getId()))){
			e.getPlayer().sendMessage(Lang.DISTRICTS + "Entered " + DistrictBag.districts.get(UUID.fromString(p.getId())).getOwner().getPlayer().getName() + "'s district.");
		}
	}
	
	@EventHandler
	public void onDistrictExit(RegionLeaveEvent e){
		e.getPlayer().sendMessage("exit");
		ProtectedRegion p = e.getRegion();
		if(DistrictBag.districts.containsKey(UUID.fromString(p.getId()))){
			e.getPlayer().sendMessage(Lang.DISTRICTS + "Left " + DistrictBag.districts.get(UUID.fromString(p.getId())).getOwner().getPlayer().getName() + "'s district.");
		}
	}
	
	@EventHandler
	public void onDistrictExit(com.mewin.WGRegionEvents.events.RegionLeaveEvent e){
		ProtectedRegion p = e.getRegion();
		if(DistrictBag.districts.containsKey(UUID.fromString(p.getId()))){
			e.getPlayer().sendMessage(Lang.DISTRICTS + "Left " + DistrictBag.districts.get(UUID.fromString(p.getId())).getOwner().getPlayer().getName() + "'s district.");
		}
	}
	
}
