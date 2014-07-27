package me.RegalMachine.Districts;

import me.RegalMachine.Districts.Commands.DistrictCommand;
import me.RegalMachine.Districts.Events.AnnounceOwnersDistrict;
import me.RegalMachine.Districts.Events.JoinEvent;
import me.RegalMachine.Districts.Events.LeaveEvent;
import me.RegalMachine.Districts.Players.WizardBag;
import me.RegalMachine.Districts.Protection.DistrictBag;
import me.RegalMachine.Districts.Protection.WorldGuard.WorldGuardSetup;
import me.RegalMachine.Districts.util.ConfigFile;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	
	public static Main instance;
	
	public static ConfigFile players;
	public static ConfigFile districts;
	public static ConfigFile general;
	
	public static WorldGuardSetup guard;
	
	
	@Override
	public void onEnable() {
		instance = this;
		
		players = new ConfigFile(this, "players.yml");
		districts = new ConfigFile(this, "districts.yml");
		general = new ConfigFile(this, "general.yml");
		
		players.saveDefaultConfig();
		districts.saveDefaultConfig();
		general.saveDefaultConfig();
		
		guard = new WorldGuardSetup();
		
		WizardBag.loadWizards();
		DistrictBag.loadDistricts();
		
		Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);
		Bukkit.getPluginManager().registerEvents(new LeaveEvent(), this);
		//For Removal of Dependencies
		//Bukkit.getPluginManager().registerEvents(new WGRegionEventUtil(), this);
		// TODO Fix the Redstone Block Border
		Bukkit.getPluginManager().registerEvents(new AnnounceOwnersDistrict(), this);
		//Bukkit.getPluginManager().registerEvents(new RedstoneDistrictBoarder(), this);
		
		getCommand("district").setExecutor(new DistrictCommand());
		
	}
	
	
	
	@Override
	public void onDisable() {
		
	}
	

}
