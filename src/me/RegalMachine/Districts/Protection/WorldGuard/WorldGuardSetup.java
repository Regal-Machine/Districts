package me.RegalMachine.Districts.Protection.WorldGuard;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class WorldGuardSetup {
	
	public static WorldGuardPlugin worldGuard;
	public static WorldEditPlugin worldEdit;
	
	
	public WorldGuardSetup(){
		worldEdit = getWorldEdit();
		worldGuard = getWorldGuard();
		
		
		
	}
	
	public WorldEditPlugin worldEdit(){
		return worldEdit;
	}
	
	public WorldGuardPlugin worldGuard(){
		return worldGuard;
	}
	
	private WorldGuardPlugin getWorldGuard(){
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		if(plugin == null || !(plugin instanceof WorldGuardPlugin)){
			return null;
		}
		
		return (WorldGuardPlugin) plugin;
	}
	
	private WorldEditPlugin getWorldEdit(){
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		if(plugin == null || !(plugin instanceof WorldEditPlugin)){
			return null;
		}
		
		return (WorldEditPlugin) plugin;
	}
	
	

}
