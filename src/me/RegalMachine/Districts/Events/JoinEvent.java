package me.RegalMachine.Districts.Events;

import java.util.UUID;

import me.RegalMachine.Districts.Players.Wizard;
import me.RegalMachine.Districts.Players.WizardBag;
import me.RegalMachine.Districts.Protection.District;
import me.RegalMachine.Districts.Protection.DistrictBag;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener{
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent e){
		WizardBag.addWizard(e.getPlayer());
		fixDistricts(e.getPlayer());
	}

	private void fixDistricts(Player wizard) {
		Wizard w = WizardBag.getWizard(wizard);
		for(String uuid: w.districtsOwned()){
			DistrictBag.addDistrict(new District(UUID.fromString(uuid)));
		}
		
	}
	
	
	
}
