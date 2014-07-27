package me.RegalMachine.Districts.Events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.RegalMachine.Districts.Protection.DistrictBag;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.mewin.WGRegionEvents.events.RegionEnteredEvent;
import com.mewin.WGRegionEvents.events.RegionLeftEvent;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RedstoneDistrictBoarder implements Listener{
	
	private Map<Player, ArrayList<Block>> borderBlocks = new HashMap<Player, ArrayList<Block>>();
	
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
				borderBlocks.put(p, new ArrayList<Block>());
			}
			for(int x = region.getMinimumPoint().getBlockX(); x<= region.getMaximumPoint().getBlockX(); x++){
				borderBlocks.get(p).add(world.getBlockAt(x, world.getHighestBlockYAt(new Location(world, x, 0, region.getMinimumPoint().getBlockZ())) - 1, region.getMinimumPoint().getBlockZ()));
				borderBlocks.get(p).add(world.getBlockAt(x, world.getHighestBlockYAt(new Location(world, x, 0, region.getMaximumPoint().getBlockZ())) - 1, region.getMaximumPoint().getBlockZ()));
			}
			for(int z = region.getMinimumPoint().getBlockZ(); z<= region.getMaximumPoint().getBlockZ(); z++){
				borderBlocks.get(p).add(world.getBlockAt(region.getMinimumPoint().getBlockX(), world.getHighestBlockYAt(new Location(world, region.getMinimumPoint().getBlockX(), 0, z)) - 1, z));
				borderBlocks.get(p).add(world.getBlockAt(region.getMaximumPoint().getBlockX(), world.getHighestBlockYAt(new Location(world, region.getMaximumPoint().getBlockX(), 0, z)) - 1, z));
			}
			for(Block block: borderBlocks.get(p)){
				p.sendBlockChange(block.getLocation(), Material.REDSTONE_BLOCK, block.getData());
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void onDistrictExit(RegionLeftEvent e){
		Player p = e.getPlayer();
		ProtectedRegion region = e.getRegion();
		if(DistrictBag.districts.containsKey(UUID.fromString(region.getId()))){
			if(borderBlocks.containsKey(p)){
				for(Block block: borderBlocks.get(p)){
					p.sendBlockChange(block.getLocation(), block.getType(), block.getData());
				}
				borderBlocks.remove(p);
			}
		}
	}
	

}
