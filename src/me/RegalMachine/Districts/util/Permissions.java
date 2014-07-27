package me.RegalMachine.Districts.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import me.RegalMachine.Districts.Main;
import me.RegalMachine.Districts.Players.Wizard;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class Permissions {
	
	public static final Permission basic = new Permission("District.Player");
	public static final Permission admin = new Permission("District.Admin");
	public static final Permission CLAIM_INFINITE = new Permission("District.claim.infinite");
	
	public static List<BlockNumberPermission> claimPerms = new ArrayList<BlockNumberPermission>();
	
	public static void loadDistrictAreaPermissions(){
		Set<String> areaLabels = Main.general.getConfig().getConfigurationSection("blocks-allowed").getKeys(false);
		 
		for(String s: areaLabels){
			int num = Main.general.getConfig().getInt("blocks-allowed." + s);
			claimPerms.add(new BlockNumberPermission("District.claim." + s, num));
		}
		
	}
	
	public static int getClaimableBlocks(Player p){
		int x = 0;
		for(BlockNumberPermission b: claimPerms){
			if(p.hasPermission(b.getPermission())){
				x = b.getBlocksAllowed();
				break;
			}
		}
		return x;
	}
	
	public static int getClaimableBlocks(Wizard w){
		Player p = w.getPlayer();
		int x = 0;
		for(BlockNumberPermission b: claimPerms){
			if(p.hasPermission(b.getPermission())){
				x = b.getBlocksAllowed();
				break;
			}
		}
		return x;
	}

}
