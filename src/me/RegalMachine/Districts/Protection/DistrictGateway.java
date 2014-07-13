package me.RegalMachine.Districts.Protection;

import org.bukkit.Location;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

import me.RegalMachine.Districts.Main;
import me.RegalMachine.Districts.Players.Wizard;

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
	
	
}


// Do math in this class to see if the player can claim that big of a district based on their amount of claim blocks.