package me.RegalMachine.Districts.Protection;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.World;

import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.RegalMachine.Districts.Main;

public class DistrictBag {
	
	public static Map<UUID,District> districts = new HashMap<UUID,District>();
	
	public static void addDistrict(District district){
		if(!districts.containsKey(district.getUUID())){
			districts.put(district.getUUID(), district);
		}else{
			districts.remove(district.getUUID());
			districts.put(district.getUUID(), district);
		}
	}
	
	public static void remove(District d){
		ProtectedRegion pr = d.getRegion();
		World world = d.getCenter().getWorld();
		RegionManager rm = Main.guard.worldGuard().getRegionManager(world);
		districts.remove(d);
		rm.removeRegion(pr.getId());
		try {
			rm.save();
		} catch (ProtectionDatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static District getDistrict(UUID id){
		return districts.get(id);
	}
	
	public static District getDistrict(String id){
		return districts.get(UUID.fromString(id));
	}
	
	public static void loadDistricts(){
		districts.clear();
		Set<String> uuids = Main.districts.getConfig().getKeys(false);
		for(String uuid: uuids){
			districts.put(UUID.fromString(uuid), new District(uuid));
		}
	}
	
	private DistrictBag(){}
}
