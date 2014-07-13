package me.RegalMachine.Districts.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.RegalMachine.Districts.Main;

import org.bukkit.permissions.Permission;

public class Permissions {
	
	public static final Permission basic = new Permission("District.Player");
	public static final Permission admin = new Permission("District.Admin");
	
	public static final Map<Permission, Integer> claimPermissions = new HashMap<Permission,Integer>();
	
	
	public static void loadDistrictAreaPermissions(){
		Set<String> areaLabels = Main.general.getConfig().getConfigurationSection("blocks-allowed").getKeys(false);
		
		for(String string: areaLabels){
			Integer num = new Integer(Main.general.getConfig().getInt("blocks-allowed." + string));
			claimPermissions.put(new Permission("District.Claim." + string), num);
		}
		
	}

}
