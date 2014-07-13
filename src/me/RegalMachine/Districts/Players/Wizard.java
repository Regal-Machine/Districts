package me.RegalMachine.Districts.Players;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.RegalMachine.Districts.Main;
import me.RegalMachine.Districts.Protection.District;
import me.RegalMachine.Districts.Protection.DistrictBag;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Wizard {
	@SuppressWarnings("unused")
	private Wizard(){}
	
	
	
	private Player player;
	private UUID id;
	private List<String> districtUUIDs;
	
	public Wizard(Player player){
		this.player = player;
		id = player.getUniqueId();
		//Add the player to the config if they aren't already there
		if(!Main.players.getConfig().contains(id.toString())){
			//with the default values
			Main.players.getConfig().createSection(id.toString());
			Main.players.getConfig().addDefault(id.toString()+ ".name", player.getName());
			Main.players.getConfig().addDefault(id.toString() + ".districts", new ArrayList<String>());
			
			Main.players.getConfig().options().copyDefaults(true);
			Main.players.saveConfig();
		}
		
		districtUUIDs = Main.players.getConfig().getStringList(id.toString() + ".districts");
		
	}

	public List<String> districtsOwned(){
		return districtUUIDs;
	}
	
	
	public Wizard(String uuid){
		player = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getPlayer();
		id = UUID.fromString(uuid);
	}
	
	public void grantDistrict(District d){
		districtUUIDs.add(d.getUUID().toString());
		Main.players.getConfig().set(id.toString() + ".districts", districtUUIDs);
		
		Main.players.getConfig().options().copyDefaults(true);
		Main.players.saveConfig();
	}
	
	public Player getPlayer(){
		return player;
	}
}