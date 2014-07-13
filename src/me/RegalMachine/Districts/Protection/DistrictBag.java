package me.RegalMachine.Districts.Protection;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
