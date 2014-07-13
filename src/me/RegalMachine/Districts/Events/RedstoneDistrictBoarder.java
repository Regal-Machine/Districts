package me.RegalMachine.Districts.Events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.RegalMachine.Districts.Protection.DistrictBag;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.mewin.WGRegionEvents.events.RegionEnteredEvent;
import com.mewin.WGRegionEvents.events.RegionLeftEvent;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RedstoneDistrictBoarder implements Listener{
	
	private Map<Player, ArrayList<Location>> borderBlocks = new HashMap<Player, ArrayList<Location>>();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDistrictEnter(RegionEnteredEvent e){
		Player p = e.getPlayer();
		ProtectedRegion region = e.getRegion();
		if(DistrictBag.districts.containsKey(UUID.fromString(region.getId()))){
			//get the blocks at the top level of the world and at the borders of the region.
			World world = p.getWorld();
			if(!DistrictBag.getDistrict(region.getId()).getOwner().getPlayer().getUniqueId().toString().equalsIgnoreCase(p.getUniqueId().toString())){
				return;
			}
			if(!borderBlocks.containsKey(p)){
				borderBlocks.put(p, new ArrayList<Location>());
			}
			for(int x = region.getMinimumPoint().getBlockX(); x<= region.getMaximumPoint().getBlockX(); x++){
				borderBlocks.get(p).add(world.getHighestBlockAt(new Location(world, x, 0, region.getMinimumPoint().getBlockZ())).getLocation());
				borderBlocks.get(p).add(world.getHighestBlockAt(new Location(world, x, 0, region.getMaximumPoint().getBlockZ())).getLocation());
			}
			for(int z = region.getMinimumPoint().getBlockZ(); z<= region.getMaximumPoint().getBlockZ(); z++){
				borderBlocks.get(p).add(world.getHighestBlockAt(new Location(world, region.getMinimumPoint().getBlockX(), 0, z)).getLocation());
				borderBlocks.get(p).add(world.getHighestBlockAt(new Location(world, region.getMaximumPoint().getBlockX(), 0, z)).getLocation());
			}
			for(Location loc: borderBlocks.get(p)){
				p.sendBlockChange(new Location(loc.getWorld(), loc.getX(), loc.getY()-1, loc.getZ()), Material.REDSTONE_BLOCK, (byte) 0);
			}
		}
	}
	
	public void onDistrictExit(RegionLeftEvent e){
		Player p = e.getPlayer();
		ProtectedRegion region = e.getRegion();
		if(DistrictBag.districts.containsKey(UUID.fromString(region.getId()))){
			if(borderBlocks.containsKey(p)){
				for(Location loc: borderBlocks.get(p)){
					Location locc = new Location(loc.getWorld(),loc.getX(),loc.getY(),loc.getZ());
					p.sendBlockChange(locc, Material.GRASS, (byte) 0);
				}
				borderBlocks.remove(p);
			}
		}
	}
	

}
