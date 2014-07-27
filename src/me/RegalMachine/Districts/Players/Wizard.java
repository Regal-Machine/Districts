package me.RegalMachine.Districts.Players;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.RegalMachine.Districts.Main;
import me.RegalMachine.Districts.Protection.District;
import me.RegalMachine.Districts.Protection.DistrictBag;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Wizard {
	@SuppressWarnings("unused")
	private Wizard(){}
	
	
	
	private Player player;
	private UUID id;
	private List<String> districtUUIDs;
	private List<String> trustedDistrictUUID;
	private int blockClaimBoost;
	
	public Wizard(Player player){
		this.player = player;
		id = player.getUniqueId();
		//Add the player to the config if they aren't already there
		if(!Main.players.getConfig().contains(id.toString())){
			//with the default values
			Main.players.getConfig().createSection(id.toString());
			Main.players.getConfig().addDefault(id.toString()+ ".name", player.getName());
			Main.players.getConfig().addDefault(id.toString() + ".districts", new ArrayList<String>());
			Main.players.getConfig().addDefault(id.toString()+ ".trustedIn", new ArrayList<String>());
			Main.players.getConfig().addDefault(id.toString() + ".claimBoost", 0);
			
			Main.players.getConfig().options().copyDefaults(true);
			Main.players.saveConfig();
		}
		
		districtUUIDs = Main.players.getConfig().getStringList(id.toString() + ".districts");
		trustedDistrictUUID = Main.players.getConfig().getStringList(id.toString() + ".trustedIn");
		blockClaimBoost = Main.players.getConfig().getInt(id.toString() + ".claimBoost");
	}

	public List<String> districtsOwned(){
		return districtUUIDs;
	}
	
	public List<ProtectedRegion> districtRegionsOwned(){
		List<ProtectedRegion> regionsOwned = new ArrayList<ProtectedRegion>();
		for(String string: districtUUIDs){
			regionsOwned.add(DistrictBag.getDistrict(string).getRegion());
		}
		return regionsOwned;
	}
	
	public List<District> districtObjectsOwned(){
		List<District> districtsOwned = new ArrayList<District>();
		for(String id: districtUUIDs){
			districtsOwned.add(DistrictBag.getDistrict(id));
		}
		return districtsOwned;
	}
	
	public List<String> districtsTrusted(){
		return trustedDistrictUUID;
	}
	
	public int getBlockBoost(){
		return blockClaimBoost;
	}
	
	public Wizard(String uuid){
		player = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getPlayer();
		id = UUID.fromString(uuid);
		districtUUIDs = Main.players.getConfig().getStringList(id.toString() + ".districts");
		trustedDistrictUUID = Main.players.getConfig().getStringList(id.toString() + ".trusteIn");
		blockClaimBoost = Main.players.getConfig().getInt(id.toString() + ".claimBoost");
	}
	
	public void trustIn(District d){
		trustedDistrictUUID.add(d.getUUID().toString());
		Main.players.getConfig().set(id.toString() + ".trustedIn", trustedDistrictUUID);
		
		Main.players.getConfig().options().copyDefaults(true);
		Main.players.saveConfig();
	}
	
	public void distrustIn(District d){
		trustedDistrictUUID.remove(d.getUUID().toString());
		Main.players.getConfig().set(id.toString() + ".trustedIn", trustedDistrictUUID);
		
		Main.players.getConfig().options().copyDefaults(true);
		Main.players.saveConfig();
	}
	
	public void grantDistrict(District d){
		districtUUIDs.add(d.getUUID().toString());
		Main.players.getConfig().set(id.toString() + ".districts", districtUUIDs);
		
		Main.players.getConfig().options().copyDefaults(true);
		Main.players.saveConfig();
	}
	
	public void removeDistrict(District d){
		districtUUIDs.remove(d.getUUID().toString());
		Main.players.getConfig().set(id.toString() + ".districts", districtUUIDs);
		
		Main.players.getConfig().options().copyDefaults(true);
		Main.players.saveConfig();
	}
	
	public void blockBoost(int x){
		blockClaimBoost = blockClaimBoost + x;
		Main.players.getConfig().set(id.toString() + ".claimBoost", blockClaimBoost);
		
		Main.players.getConfig().options().copyDefaults(true);
		Main.players.saveConfig();
	}
	
	public Player getPlayer(){
		return player;
	}
}