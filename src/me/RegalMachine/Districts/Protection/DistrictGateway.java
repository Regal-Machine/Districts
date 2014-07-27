package me.RegalMachine.Districts.Protection;

import java.util.List;

import org.bukkit.Location;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

import me.RegalMachine.Districts.Main;
import me.RegalMachine.Districts.Players.Wizard;
import me.RegalMachine.Districts.util.Permissions;

public class DistrictGateway {
	
	public static boolean canCreateDistrict(Wizard wizard, int radius){
		Location center = wizard.getPlayer().getLocation();
		ProtectedCuboidRegion region = new ProtectedCuboidRegion("test",
				new BlockVector(new Vector(center.getBlockX() - radius, 0, center.getBlockZ() - radius)),
				new BlockVector(new Vector(center.getBlockX() + radius, 127, center.getBlockZ() + radius)));
		if(Main.guard.worldGuard().getRegionManager(wizard.getPlayer().getWorld()).getApplicableRegions(region).size() != 0){
			return false;
		}
		return true;
	}
	
	public static boolean hasEnoughClaimBlocks(Wizard wizard, int radius){
		int maxClaims = Permissions.getClaimableBlocks(wizard) + wizard.getBlockBoost();
		int hasClaimed = 0;
		List<District> claims = wizard.districtObjectsOwned();
		
		for(District d: claims){
			hasClaimed = hasClaimed + ((d.getRadius()*2) * (d.getRadius()*2));
		}
		
		if(hasClaimed + ((radius*2)*(radius*2)) > maxClaims){
			return false;
		}
		return true;
	}
	
	public static int claimsLeft(Wizard wizard){
		int maxClaims = Permissions.getClaimableBlocks(wizard) + wizard.getBlockBoost();
		int hasClaimed = 0;
		List<District> claims = wizard.districtObjectsOwned();
		
		for(District d: claims){
			hasClaimed = hasClaimed + ((d.getRadius()*2) * (d.getRadius()*2));
		}
		
		return maxClaims - hasClaimed;
		
	}
	
	public static int getBlocksClaimed(Wizard wizard){
		int hasClaimed = 0;
		List<District> claims = wizard.districtObjectsOwned();
		
		for(District d: claims){
			hasClaimed = hasClaimed + ((d.getRadius()*2) * (d.getRadius()*2));
		}
		return hasClaimed;
	}
	
	
}


// Do math in this class to see if the player can claim that big of a district based on their amount of claim blocks.