package me.RegalMachine.Districts.Players;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import me.RegalMachine.Districts.Main;
import me.RegalMachine.Districts.Players.Wizard;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WizardBag {
	
	private static Map<Player, Wizard> wizards = new HashMap<Player, Wizard>();
	
	public static void addWizard(Player p){
		if(wizards.containsKey(p))
			wizards.remove(p);
		wizards.put(p, new Wizard(p));
	}
	
	public static void addWizard(String uuid){
		wizards.put(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getPlayer(), new Wizard(uuid));
	}
	
	public static Wizard getWizard(Player p){
		if(wizards.containsKey(p))
			return wizards.get(p);
		addWizard(p);
		return wizards.get(p);
	}
	
	public static Wizard getWizard(CommandSender s){
		Player p = (Player) s;
		if(wizards.containsKey(p))
			return wizards.get(p);
		addWizard(p);
		return wizards.get(p);
	}
	
	public static void clearWizard(Player player){
		wizards.remove(player);
	}
	
	public static void loadWizards(){
		// TODO Load all the Wizards from the config.
		wizards.clear();
		
		Set<String> uuids = Main.players.getConfig().getKeys(false);
		for(String uuid: uuids){
			wizards.put(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getPlayer(), new Wizard(uuid));
		}
		
		
	}
	
	
	
}
